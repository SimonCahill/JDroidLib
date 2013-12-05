/*
 * Copyright (C) 2013 Simon.
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

package JDroidLib.Android;

import JDroidLib.utils.*;
import java.io.*;
import JDroidLib.exceptions.*;
import JDroidLib.enums.*;
import JDroidLib.installers.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public final class Fastboot {
    
    private final String mbDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
    private final String osName = System.getProperty("os.name");
    private String getFastboot() {
        String str = "";
        if (osName.contains("Windows")) {
            str = mbDir + "fastboot.exe";
        } else {
            str = mbDir + "fastboot";
        }
        File fastboot = new File(str);
        if (!fastboot.exists()) {
            FastbootInstaller fbInst = new FastbootInstaller();
            try {
                fbInst.installFastboot();
            } catch (    InvalidOSException | IOException ex) {
                Logger.getLogger(Fastboot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return str;
    }
    private final Command cmd = new Command();
    
    /**
     * Executes fastboot command without returning executable's output.
     * @param command
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public void executeFastbootCommandNoReturn(String command) throws InvalidCommandException, IOException{
        if (!command.equals("")) {
        cmd.executeProcessNoReturn(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an invalid command! Please enter a valid command.");
    }
    
    /**
     * Executes fastboot command, returns last output line
     * @param command
     * @return
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public String executeFastbootCommandReturnLastLine(String command) throws InvalidCommandException, IOException {
        if (!command.equals("")) {
            return cmd.executeProcessReturnLastLine(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an empty command! Please enter a valid command.");
    }
    
    /**
     * Executes fastboot command, returns executable's entire output as StringBuilder
     * @param command
     * @return
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public StringBuilder executeFastbootCommandReturnEntireOutput(String command) throws InvalidCommandException, IOException {
        if (!command.equals("")) {
            return cmd.executeProcessReturnAllOutput(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an empty command! Please enter a valid command.");
    }
    
    /**
     * Unlocks fastboot bootloaders using the oem unlock command.
     * @throws java.io.IOException
     */
    public void oemUnlockBootloader() throws IOException {
        cmd.executeProcessNoReturn(getFastboot(), "oem unlock");
    }
    
    /**
     * Locks fastboot bootloader using the oem lock command
     * @throws IOException 
     */
    public void oemLockBootloader() throws IOException {
        cmd.executeProcessNoReturn(getFastboot(), "oem lock");
    }
    
    /**
     * Reboots device from fastboot to Android OS
     * @throws IOException 
     */
    public void rebootToAndroid() throws IOException {
        cmd.executeProcessNoReturn(getFastboot(), "reboot");
    }
    
    /**
     * Reboot the device's bootloader.
     * @throws IOException 
     */
    public void rebootBootloader() throws IOException {
        cmd.executeProcessNoReturn(getFastboot(), "reboot-bootloader");
    }
    
    /**
     * Attempts to flash Linux system image via fastboot. 
     * Returns executable's error.
     * If returned value = "", then no error occurred while flashing image.
     * 
     * @param imgType
     * @param imgLocation
     * @return
     * @throws IOException
     * @throws InvalidImageException 
     */
    public String flashImg(FastbootImgType imgType, String imgLocation) throws IOException, InvalidImageException {
        // Get image type
        String _imgType = imgType.toString().toLowerCase();
        // Detect img
        if (!imgLocation.contains(_imgType) && !imgLocation.endsWith(".img")) throw new InvalidImageException();
        // Execute process and return value.
        return cmd.executeProcessReturnError(getFastboot(), _imgType + " " + imgLocation);
    }
    
}
