/*
 * Copyright (C) 2014 beatsleigher.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package JDroidLib.android.device;

import JDroidLib.android.controllers.ADBController;

import java.io.*;

/**
 *
 * @author beatsleigher
 */
public class SU {
    
    String serial = "";
    ADBController adbController = null;
    
    boolean isInstalled = false;
    boolean hasRoot = false;
    String suVersion = "";
    
    public SU(String serial, ADBController adbController) {
        this.serial = serial;
        this.adbController = adbController;
    }
    
    private void update() throws IOException {
        String[] cmd = {"su", "-v"};
        String raw = adbController.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains("su: not found")) {
                isInstalled = false;
                hasRoot = false;
                suVersion = "n/a";
                break;
            }
            String[] arr = line.split("\\ ");
            isInstalled = true;
            hasRoot = true;
            suVersion = arr[0];
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
    
}
