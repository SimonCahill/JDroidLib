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
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public final class ADBController {
    
    CaptainKirk controller = null;
    Device device = null;
    FastbootController fbController = null;
    
    /**
     * Retrieves a list of connected devices.
     * @return list of devics.
     * @throws IOException 
     */
    private List<String> connectedDevices() throws IOException {
        ////////////////////
        // Get devices ////
        //////////////////
        
        return controller.getConnectedDevices();
    }
    
    /**
     * Starts the ADB server on local machine.
     * @throws IOException 
     */
    public void startServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"start-server"});
    }
    
    /**
     * Kills the ADB server running on local machine.
     * @throws IOException 
     */
    public void stopServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"stop-server"});
    }
    
    /**
     * Executes @see #code startServer() and @see #code stopServer().
     * @throws IOException 
     */
    public void restartServer() throws IOException {
        stopServer();
        startServer();
    }
    
    /**
     * Returns a list of connected devices via ADB.
     * @return
     * @throws IOException 
     */
    public List<String> getConnectedDevices() throws IOException {
        return connectedDevices();
    }
    
    /**
     * Gets a list of all connected fasboot devices (device connected to computer via fastboot.)
     * Deprecated: Please use FastbootController for these operations. These methods may be removed in near future.
     * @return
     * @throws IOException 
     */
    @Deprecated
    public List<String> getConnectedFastbootDevices() throws IOException {
        return controller.getConnectedFastbootDevices();
    }
    
    /**
     * Default constructor.
     * @throws IOException 
     * @throws ZipException 
     * @throws InterruptedException 
     */
    public ADBController() throws IOException, ZipException, InterruptedException {
        controller = new CaptainKirk();
        startServer();
        fbController = new FastbootController(controller);
    }
    
    /**
     * Disposes of all the class variables uses, to prevent memory leaks.
     * @throws IOException 
     */
    public void dispose() throws IOException {
        stopServer();
        controller = null;
        device = null;
        fbController = null;
    }
    
    /**
     * Returns an instance of Device, for requested serial number.
     * @param serial
     * @return 
     */
    public Device getDevice(String serial) {
        return new Device(serial, this);
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
    @Deprecated
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
    @Deprecated
    public String rebootDeviceFastboot(String deviceSerial, RebootTo mode) throws IOException {
        return controller.fastboot_rebootDevice(deviceSerial, mode);
    }
    
    /**
     * Installs a desired application to selected device.
     * @param asSystemApp Install the application to /system/app?
     * @param deviceSerial The specific device to install the app to. Set to #code null to issue globally.
     * @param apkLocation The apk itself.
     * @return ADB output.
     * @throws IOException if something goes wrong.
     */
    public String installApplication(boolean asSystemApp, String deviceSerial, String apkLocation) throws IOException {
         if (asSystemApp)
             return executeADBCommand(false, true, deviceSerial, new String[]{"push", apkLocation, "/system/app/" + apkLocation});
         else
             return executeADBCommand(false, false, deviceSerial, new String[]{"install", apkLocation});
    }
    
    /**
     * Uninstalls a desired application from selected device.
     * @param systemApp Should a system app be removed?
     * @param deviceSerial The specific device. Set to #code <i>null</i> to issue command globally.
     * @param apkLocation The application to be removed. <b><u>Always</b></u> enter <b><u>just</b></u> the application filename!
     * @return ADB output.
     * @throws IOException if something goes wrong.
     */
    public String uninstallApplication(boolean systemApp, String deviceSerial, String apkLocation) throws IOException {
        if (systemApp) 
            return executeADBCommand(true, true, deviceSerial, new String[]{"rm", "/system/app/" + apkLocation});
        else
            return executeADBCommand(false, false, deviceSerial, new String[]{"uninstall", apkLocation});
    }
    
    /**
     * Returns an instance of FastbootController.
     * All methods concerning fastboot in this class are deprecated and will be removed in near future.
     * @return 
     */
    public FastbootController getFastbootController() { return fbController; }
    
}
