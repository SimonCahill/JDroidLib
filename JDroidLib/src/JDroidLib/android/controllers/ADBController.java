/* Copyright (C) 2014 beatsleigher.
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

package JDroidLib.android.controllers;

import java.util.*;
import java.io.*;

import JDroidLib.util.*;
import JDroidLib.android.device.*;
import JDroidLib.enums.RebootTo;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public final class ADBController {
    
    CaptainKirk controller = null;
    Device device = null;
    
    /**
     * Retrieves a list of connected devices.
     * @return list of devics.
     * @throws IOException 
     */
    List<String> connectedDevices() throws IOException {
        ////////////////////
        // Get devices ////
        //////////////////
        
        return controller.getConnectedDevices();
    }
    
    public void startServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"start-server"});
    }
    
    public void stopServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"stop-server"});
    }
    
    public void restartServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"stop-server"});
        controller.executeADBCommand(false, false, null, new String[]{"start-server"});
    }
    
    public List<String> getConnectedDevices() throws IOException {
        return connectedDevices();
    }
    
    /**
     * gets a list of all connected fasboot devices (device connected to computer via fastboot.)
     * @return
     * @throws IOException 
     */
    public List<String> getConnectedFastbootDevices() throws IOException {
        return controller.getConnectedFastbootDevices();
    }
    
    /**
     * Default constructor.
     * @throws IOException 
     */
    public ADBController() throws IOException {
        controller = new CaptainKirk();
        startServer();
    }
    
    /**
     * Disposes of all the class variables uses, to prevent memory leaks.
     * @throws IOException 
     */
    public void dispose() throws IOException {
        stopServer();
        controller = null;
        device = null;
    }
    
    public Device getDevice(String serial) {
        return new Device(serial);
    }
    
    /**
     * Allows execution of custom ADB commands, used for data capsuling, so brainy Brian don't get too much attention. He got ADHD. Poor fella.
     * @param asShell Execute as shell command.
     * @param remountDevice Self explanatory...
     * @param serial of the device. (Set to null, if not device specific.)
     * @param cmds you want to execute.
     * @return ADB output.
     * @throws IOException if something went wrong. 
     */
    public String executeADBCommand(boolean asShell, boolean remountDevice, String serial, String[] cmds) throws IOException {
        return controller.executeADBCommand(asShell, remountDevice, serial, cmds);
    }
    
    /**
     * Allows for execution of custom fastboot commands, used for data capsuling.
     * @param deviceSerial for device-specific commands. Set to null, if not device specific.
     * @param cmds you want to execute.
     * @return fastboot output.
     * @throws IOException if something went wrong.
     */
    public String executeFastbootCommand(String deviceSerial, String[] cmds) throws IOException {
        return controller.executeFastbootCommand(deviceSerial, cmds);
    }
    
    /**
     * Allows for easy rebooting of device.
     * Just import the enum RebootTo, and then choose your fait.
     * @param deviceSerial for specific device reboots.
     * @param mode to reboot the device to.
     * @return ADB output.
     * @throws IOException if something went wrong.
     */
    public String rebootDevice(String deviceSerial, RebootTo mode) throws IOException {
        return controller.ADB_rebootDevice(deviceSerial, mode);
    }
    
    /**
     * Allows for easy rebooting of device.
     * Just import the enum RebootTo, and then choose your fait.
     * @param deviceSerial for specific device reboots.
     * @param mode to reboot the device to.
     * @return ADB output.
     * @throws IOException if something went wrong.
     */
    public String rebootDeviceFastboot(String deviceSerial, RebootTo mode) throws IOException {
        return controller.fastboot_rebootDevice(deviceSerial, mode);
    }
    
}
