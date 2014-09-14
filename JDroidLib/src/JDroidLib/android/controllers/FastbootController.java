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

package JDroidLib.android.controllers;

import JDroidLib.enums.RebootTo;
import JDroidLib.exceptions.*;
import JDroidLib.util.*;
import JDroidLib.util.CaptainKirk;

import java.io.*;
import java.util.*;

/**
 * FastbootController - The right way to do anything fastboot.
 * FastbootController contains all the methods and code, needed to perform
 * (almost) every task, you'll require fastboot to do. These include: - Flashing
 * - Updating - Rebooting - Device listing.
 *
 * @author beatsleigher
 */
public class FastbootController {

    CaptainKirk controller = null;
    static FastbootController instance = null;
    
    /**
     * Returns an existing or creates and returns an instance of FastbootController.
     * @return An instance of Fastboot controller.
     * @throws IOException If an error occurs during instantiation.
     * @throws InterruptedException If an error occurs during instantiation.
     * @throws OSNotSupportedException If an error occurs during instantiation.
     */
    public static FastbootController getFastbootController() throws IOException, InterruptedException, OSNotSupportedException {
        if (instance != null) return instance; 
        else return (instance = new FastbootController()); 
    }
    
    private FastbootController() throws IOException, InterruptedException, OSNotSupportedException {
        controller = CaptainKirk.getInstance();
    }

    /**
     * Allows for execution of custom fastboot commands, used for data
     * capsuling.
     *
     * @param deviceSerial The serial of the device you wish to issue the command to.
     * @param cmds A list of commands (and/or) parameters you'd like to execute.
     * @param returnOutput Set to true if you want to process the command's output manually.
     * @return If returnOutput is set to <u>true</u>, the fastboot command's output will be returned.
     * @throws IOException if something went wrong.
     */
    public String executeCommand(String deviceSerial, String[] cmds, boolean returnOutput) throws IOException, IllegalArgumentException {
        if (deviceSerial == null || deviceSerial.isEmpty())
            throw new IllegalArgumentException("deviceSerial must not be null or empty!");
        if (cmds == null || cmds.length == 0)
            throw new IllegalArgumentException("cmds must not be null or empty!");
        
        if (returnOutput)
            return controller.executeCommand(Command.getCommand(Command.CommandType.FASTBOOT_COMMAND, deviceSerial, Command.convertArrayToList(cmds), true));
        else
            return null;
    }
    
    /**
     * Allows for execution of custom fastboot commands, used for data
     * capsuling.
     *
     * @param deviceSerial The serial of the device you wish to issue the command to.
     * @param cmds A list of commands (and/or) parameters you'd like to execute.
     * @param returnOutput Set to true if you want to process the command's output manually.
     * @return If returnOutput is set to <u>true</u>, the fastboot command's output will be returned.
     * @throws IOException if something went wrong.
     */
    public String executeCommand(String deviceSerial, List<String> cmds, boolean returnOutput) throws IOException {
        if (deviceSerial == null || deviceSerial.isEmpty())
            throw new IllegalArgumentException("deviceSerial must not be null or empty!");
        if (cmds == null || cmds.isEmpty())
            throw new IllegalArgumentException("cmds must not be null or empty!");
        
        if (returnOutput)
            return controller.executeCommand(Command.getCommand(Command.CommandType.FASTBOOT_COMMAND, deviceSerial, cmds, true));
        else
            return null;
    }

    /**
     * Allows for easy rebooting of device. Just import the enum RebootTo, and
     * then choose your fait.
     *
     * @param deviceSerial for specific device reboots.
     * @param mode to reboot the device to.
     * @return ADB output.
     * @throws IOException if something went wrong.
     */
    public void rebootDeviceFastboot(String deviceSerial, RebootTo mode) throws IOException {
        switch (mode) {
            case ANDROID:
                controller.executeCommand(
                Command.getCommand(Command.CommandType.FASTBOOT_COMMAND, deviceSerial, 
                                                                         Command.convertArrayToList("reboot"), false));
            default:
                controller.executeCommand(
                Command.getCommand(Command.CommandType.FASTBOOT_COMMAND, deviceSerial, 
                                                                         Command.convertArrayToList("reboot-bootloader"), false));
        }
    }
    
    /**
     * Retrieves a list of all connected devices currently running in fastboot-mode.
     * @return A list containing String data types, each item representing one device. 
     * @throws IOException If something goes wrong.
     */
    public List<String> getConnectedDevices() throws IOException {
        List<String> devices = new ArrayList<>();
        String rawOutput = null;
        BufferedReader reader = null;
        String line;
        
        rawOutput = controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("devices"), true, false)); 
        
        reader = new BufferedReader(new StringReader(rawOutput));
        while ((line = reader.readLine()) != null)
            devices.add((line.split("\\s"))[0]);
        reader.close();
        
        return devices;
    }

}
