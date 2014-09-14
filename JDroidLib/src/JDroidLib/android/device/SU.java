/*
 * Copyright (C) 2014 beatsleigher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package JDroidLib.android.device;

import JDroidLib.android.controllers.ADBController;
import JDroidLib.exceptions.*;

import java.io.*;

/**
 * Represents a device's SuperUser installation.
 * @author beatsleigher
 */
public class SU {
    
    private final Device device;
    private final ADBController adbController;
    
    boolean isInstalled = false;
    boolean hasRoot = false;
    String suVersion = "";
    
    /**
     * The constructor for this class.
     * @param device The device to represent
     * @param adbController The @see ADBController which powers everything.
     */
    SU(Device device, ADBController adbController) {
        this.device = device;
        this.adbController = adbController;
    }
    
    /**
     * Updates the information represented by this class.
     * @throws IOException If an error occurs while executing the ADB commands.
     */
    private void update() throws IOException {
        String raw = adbController.executeCommand(device, true, true, "su", "-v");
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains("su: not found")) {
                isInstalled = false;
                hasRoot = false;
                suVersion = "n/a";
                break;
            }
            isInstalled = true;
            hasRoot = true;
            suVersion = line;
        }
        reader.close();
    }
    
    /**
     * Updates information, and determines whether device is rooted or not.
     * @return true, if SU is installed. False, if otherwise.
     * @throws IOException if something went wrong.
     */
    public boolean isInstalled() throws IOException { update(); return isInstalled; }
    
    /**
     * Updates information, and determines whether device is rooted or not.
     * @return true, if device is rooted, false if otherwise.
     * @throws IOException 
     */
    public boolean hasRoot() throws IOException { update(); return hasRoot; }
    
    /**
     * Updates information, and determines whether device is rooted or not.
     * @return SU version.
     * @throws IOException 
     */
    public String getSUVersion() throws IOException { update(); return suVersion; }
    
    /**
     * Executes a command as the root user on the device associated with this class.
     * @param remount Remount the device?
     * @param cmds The command (and arguments) to be executed.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     * @throws JDroidLib.exceptions.DeviceHasNoRootException if the device does <i>not</i> have root.
     */
    public String executeSUCommand(boolean remount, String[] cmds) throws IOException, DeviceHasNoRootException {
        if (!hasRoot())
            throw new DeviceHasNoRootException("Cannot execute root commands without root access.");
        
        if (remount)
            device.remountDevice();
        
        return adbController.executeCommand(device, true, true, "su", cmds);
    }
    
}
