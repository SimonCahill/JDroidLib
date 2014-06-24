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

import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.util.*;

import JDroidLib.android.device.Device;
import JDroidLib.enums.RebootTo;
import JDroidLib.exceptions.*;
import JDroidLib.util.CaptainKirk;

/**
 * JDroidLib's main class.
 * Always create an instance of this class and derive other classes from here.
 * {@see CaptainKirk} is <u>not</u> meant to be used by anything other than JDroidLib - Only use in emergencies!
 * 
 * To "start" JDroidLib, use the following code:
 * <pre>
 * {@code
 * public class YourClass {
 *      private final ADBController adbController; // Make this final when possible, you're not going to need to change this. <b>ONLY CREATE ONE INSTANCE PER PROGRAM!</b>
 * 
 *      public YourClass() {
 *          try {
 *              adbController = new ADBController(); // Assign final variable adbController value of new object, type ADBController
 *          } catch (IOException | ZipException | InterruptedException ex) { // ADBController's constructor throws these exceptions
 *              System.err.println("Error starting JDroidLib (CODE: BLA)); // You don't have to do this..
 *              ex.printStackTrace(System.err);
 *          }
 *      }
 * 
 * }
 * 
 * }
 * </pre>
 * 
 * That code initializes JDroidLib and automatically tells JDroidLib to copy any and all needed files to the local hard drive. These are deleted once the JVM exits.
 * 
 * To get an instance of FastbootController, use the following code (make sure you have a running instance of ADBController):
 * <pre>
 * {@code
 * private final FastbootController fbController = adbController.getFastbootController(); // Make it final where you can, you won't need to change it.
 * }
 * </pre>
 *
 * @author Beatsleigher
 * @since beta
 *
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public final class ADBController {

    private CaptainKirk controller = null;
    private FastbootController fbController = null;

    /**
     * Retrieves a list of connected devices.
     *
     * @return list of Device-instances corresponding to each connected (physical) device.
     * @throws IOException
     * @author Beatsleigher, pedja1
     * @since beta
     *
     */
    private List<Device> connectedDevices() throws IOException { return controller.getConnectedDevices(this); }

    /**
     * Starts the ADB server on local machine.
     * Attempts to start a local ADB deamon on the host machine.
     * @throws IOException
     * @author Beatsleigher
     * @since beta
     *
     */
    public void startServer() throws IOException { controller.executeADBCommand(false, false, "", new String[]{"start-server"}); }

    /**
     * Kills the ADB server running on local machine.
     * Attempts to stop the local ADB-deamon on the host machine.
     * @throws IOException
     * @author Beatsleigher
     * @since beta
     *
     */
    public void stopServer() throws IOException { controller.executeADBCommand(false, false, "", new String[]{"stop-server"}); }

    /**
     * Executes @see #startServer() and @see #stopServer().
     * Attempts to stop the local ADB deamon and then attempts to start a new deamon.
     * @throws IOException
     * @author Beatsleigher
     * @since beta
     *
     */
    public void restartServer() throws IOException {
        stopServer();
        startServer();
    }

    /**
     * Returns a list of connected devices via ADB.
     * Generates a list of @see Device, where each @see Device instance represents a connected device.
     * @return
     * @throws IOException
     * @author Beatsleigher, pedja1
     * @since beta
     *
     */
    public List<Device> getConnectedDevices() throws IOException { return connectedDevices(); }

    /**
     * Gets a list of all connected fasboot devices (device connected to
     * computer via fastboot.) Deprecated: Please use FastbootController for
     * these operations. These methods may be removed in near future.
     *
     * @return
     * @throws IOException
     * @author Beatsleigher
     * @since beta
     *
     */
    @Deprecated
    public List<String> getConnectedFastbootDevices() throws IOException { return controller.getConnectedFastbootDevices(); }

    /**
     * Default constructor.
     * Please use this constructor <b>only</b>. Do <b>not</b> directly invoke @see CaptainKirk!
     * This constructor creates a new instance of @see CaptainKirk, installs all necessary files and starts the ADB deamon.
     * 
     * @throws IOException If a process could not be executed properly.
     * @throws ZipException If an error occurred while extracting ADB binaries.
     * @throws InterruptedException If something prevented the thread from correctly sleeping.
     * @author Beatsleigher That would be my humble self :)
     * @throws JDroidLib.exceptions.OSNotSupportedException If JDroidLib detects an unsupported OS.
     * @since beta
     *
     */
    public ADBController() throws IOException, ZipException, InterruptedException, OSNotSupportedException {
        System.out.println("Waking Captain Kirk...");
        controller = new CaptainKirk();
        System.out.println("Starting ADB server...");
        startServer();
        System.out.append("Preparing JDroidLib for fastboot...");
        fbController = new FastbootController(controller);
    }

    /**
     * Disposes of all the class variables uses, to prevent memory leaks.
     * Invoke this method, when your application is exiting, so that the garbage collector can clean things up and prevent any memory leaks.
     * 
     * @throws IOException
     * @author Beatsleigher
     * @since beta
     *
     */
    public void dispose() throws IOException {
        stopServer();
        controller = null;
        fbController = null;
    }

    /**
     * Allows execution of custom ADB commands, used for data capsuling, so
     * brainy Brian don't get too much attention. He got ADHD. Poor fella.
     *
     * @param asShell Execute as shell command.
     * @param remountDevice Self explanatory...
     * @param serial of the device. (Set to null, if not device specific.)
     * @param cmds you want to execute.
     * @return ADB output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     * @deprecated This method is deprecated, and it is recommended, you use the method, which uses a List of String, instead of a String array.
     *
     */
    @Deprecated
    public String executeADBCommand(boolean asShell, boolean remountDevice, String serial, String[] cmds) throws IOException {
        return controller.executeADBCommand(asShell, remountDevice, serial, cmds);
    }
    
    /**
     * Allows execution of custom ADB commands, used for data capsuling, so
     * brainy Brian don't get too much attention. He got ADHD. Poor fella.
     *
     * @param asShell Execute as shell command.
     * @param remountDevice Self explanatory...
     * @param serial of the device. (Set to null, if not device specific.)
     * @param cmds you want to execute.
     * @return ADB output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    public String executeADBCommand(boolean asShell, boolean remountDevice, Device serial, List<String> cmds) throws IOException, NullPointerException {
        return controller.executeADBCommand(asShell, remountDevice, serial, cmds);
    }
    
    /**
     * Allows execution of custom ADB commands, used for data capsuling, so
     * brainy Brian don't get too much attention. He got ADHD. Poor fella.
     *
     * @param asShell Execute as shell command.
     * @param remountDevice Self explanatory...
     * @param device The device to issue the command to.
     * @param cmds you want to execute.
     * @return ADB output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    public String executeADBCommand(boolean asShell, boolean remountDevice, Device device, String[] cmds) throws IOException, NullPointerException {
        return controller.executeADBCommand(asShell, remountDevice, device, cmds);
    }

    /**
     * Allows for execution of custom fastboot commands, used for data
     * capsuling.
     *
     * @param deviceSerial for device-specific commands. Set to null, if not
     * device specific.
     * @param cmds you want to execute.
     * @return fastboot output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    @Deprecated
    public String executeFastbootCommand(String deviceSerial, String[] cmds) throws IOException {
        return controller.executeFastbootCommand(deviceSerial, cmds);
    }

    /**
     * Allows for easy rebooting of device. Just import the enum RebootTo, and
     * then choose your fait.
     *
     * @param deviceSerial for specific device reboots.
     * @param mode to reboot the device to.
     * @return ADB output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    public String rebootDevice(Device deviceSerial, RebootTo mode) throws IOException {
        return controller.ADB_rebootDevice(deviceSerial.getSerial(), mode);
    }

    /**
     * Allows for easy rebooting of device. Just import the enum RebootTo, and
     * then choose your fait.
     *
     * @param deviceSerial for specific device reboots.
     * @param mode to reboot the device to.
     * @return ADB output.
     * @throws IOException if something went wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    @Deprecated
    public String rebootDeviceFastboot(String deviceSerial, RebootTo mode) throws IOException {
        return controller.fastboot_rebootDevice(deviceSerial, mode);
    }

    /**
     * Returns an instance of FastbootController. All methods concerning
     * fastboot in this class are deprecated and will be removed in near future.
     *
     * @return instance of @see FastbootController
     * @author Beatsleigher
     * @since beta
     *
     */
    public FastbootController getFastbootController() { return fbController; }

    /**
     * Restarts the ADB daemon as root.
     *
     * @throws IOException
     */
    public void rootServer() throws IOException { executeADBCommand(false, false, "", new String[]{"root"}); }
    
    /**
     * Returns a new instance of a device.
     * If one other son of a bitch deletes this method, I <i><b>WILL</b></i> go fucking crazy!
     * @param serial
     * @return 
     */
    public Device getDevice(String serial) { return new Device(serial, this); }
    
    /**
     * Returns the currently in-use ADB file being used by JDroidLib.
     * This is handy for when you need to do things, which JDroidLib is not capable of.
     * @return current ADB file.
     */
    public File getADB() { return controller.getADB(); }
    
    /**
     * Remounts the device's internal flash memory to R/W, so more advanced operations can be performed.
     * @param dev The device to remount
     * @throws IOException If something goes wrong.
     */
    public void remountDevice(Device dev) throws IOException { controller.remountDevice(dev);  }

}
