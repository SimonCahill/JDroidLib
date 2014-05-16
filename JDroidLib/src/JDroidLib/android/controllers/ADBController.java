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
     * @since beta
     *
     */
    public ADBController() throws IOException, ZipException, InterruptedException {
        controller = new CaptainKirk();
        startServer();
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
    public String rebootDevice(String deviceSerial, RebootTo mode) throws IOException {
        return controller.ADB_rebootDevice(deviceSerial, mode);
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
     * Installs a desired application to selected device.
     *
     * @param asSystemApp Install the application to /system/app?
     * @param deviceSerial The specific device to install the app to. Set to
     * #code null to issue globally.
     * @param apkLocation The apk itself.
     * @return ADB output.
     * @throws IOException if something goes wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    public String installApplication(boolean asSystemApp, String deviceSerial, String apkLocation) throws IOException {
        if (asSystemApp) {
            return executeADBCommand(false, true, deviceSerial, new String[]{"push", apkLocation, "/system/app/" + apkLocation});
        } else {
            return executeADBCommand(false, false, deviceSerial, new String[]{"install", apkLocation});
        }
    }

    /**
     * Uninstalls a desired application from selected device.
     *
     * @param systemApp Should a system app be removed?
     * @param deviceSerial The specific device. Set to #code <i>null</i> to
     * issue command globally.
     * @param apkLocation The application to be removed. <b><u>Always</b></u>
     * enter <b><u>just</b></u> the application filename!
     * @return ADB output.
     * @throws IOException if something goes wrong.
     * @author Beatsleigher
     * @since beta
     *
     */
    public String uninstallApplication(boolean systemApp, String deviceSerial, String apkLocation) throws IOException {
        if (systemApp) {
            return executeADBCommand(true, true, deviceSerial, new String[]{"rm", "/system/app/" + apkLocation});
        } else {
            return executeADBCommand(false, false, deviceSerial, new String[]{"uninstall", apkLocation});
        }
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
     * Attempts to back up selected device.
     *
     * @param backupAPKs Backup actual .apk files on device
     * @param backupOBB Backup .obb extensions
     * @param backupShared Backup SD card data
     * @param backupSystem Backup all system apps
     * @param backupAll Backup all installed applications
     * @param deviceSerial Specific device (set to null to issue command
     * globally)
     * @param backupFile Please make sure you make this a file, which is
     * accepted by the filesystem. Also, please make sure the file ends in .ab!
     * @param specificPackages Specify a list of specific packages to be backed
     * up. These will be included, even if backupSystem is false.
     * @throws IOException if something goes wrong when executing the process
     * @throws DeviceNotFoundException if ADB returns, that the device could not
     * be found.
     * @throws UnableToConnectForBackupException if ADB reports, that it was
     * unable to connect to a device to back up data.
     * @author Beatsleigher
     * @since beta
     *
     */
    @SuppressWarnings("null")
    public void backupDevice(boolean backupAPKs, boolean backupOBB, boolean backupShared, boolean backupSystem, boolean backupAll, List<String> specificPackages, String deviceSerial, String backupFile)
            throws IOException, DeviceNotFoundException, UnableToConnectForBackupException {
        List<String> args = new ArrayList();
        args.add("backup");
        args.add(backupFile);
        if (backupAPKs) {
            args.add("-apk");
        } else {
            args.add("-noapk");
        }
        if (backupOBB) {
            args.add("-obb");
        } else {
            args.add("-noobb");
        }
        if (backupShared) {
            args.add("-shared");
        } else {
            args.add("-noshared");
        }
        if (backupAll) {
            args.add("-all");
        }
        if (backupSystem) {
            args.add("-system");
        } else {
            args.add("-nosystem");
        }
        if (specificPackages != null || !specificPackages.isEmpty()) {
            for (String specificPackage : specificPackages) {
                args.add(specificPackage);
            }
        }
        String[] cmds = {"backup", Arrays.deepToString(args.toArray())};
        String result = executeADBCommand(false, false, deviceSerial, cmds);
        if (result.contains("device not found")) {
            throw new DeviceNotFoundException(result);
        }
        if (result.contains("unable to connect for backup")) {
            throw new UnableToConnectForBackupException(result);
        }
    }

    /**
     * Attempts to restore a connected (and selected) device to a previous state
     * via an Android backup file (*.ab). This method will not return any value,
     * instead it will process any output of ADB and throw the respective
     * exceptions.
     *
     * @param deviceSerial The specific device to restore file to (set to null
     * if only one device is connected/you don't have a serial.)
     * @param backupFile The actual *.ab file to restore the device from.
     * @throws IOException If something goes wrong when executing the process.
     * @throws DeviceNotFoundException if ADB returns that the device could not
     * be found.
     * @throws BackupFileNotAccessibleException if ADB reports, that a specified
     * backup file could not be found.
     * @throws UnableToConnectForBackupException if ADB reports, that it was
     * unable to connect to a device to back up data.
     * @author Beatsleigher
     * @since beta
     *
     */
    public void restoreDevice(String deviceSerial, String backupFile)
            throws IOException, DeviceNotFoundException, BackupFileNotAccessibleException, UnableToConnectForBackupException {
        String[] cmds = {"restore", backupFile};
        String result = executeADBCommand(false, false, deviceSerial, cmds);
        if (result.contains("device not found")) {
            throw new DeviceNotFoundException(result);
        }
        if (result.contains("unable to connect for backup")) {
            throw new UnableToConnectForBackupException(result);
        }
    }

    /**
     * Attempts to back up device's EFS partition. This will only work on
     * SAMSUNG devices. Any other device will throw an EFSNotFoundException! The
     * backup will be saved in a folder in the specified directory. The folder
     * name will look similar to this: efs-20140312-2142 First four digits are
     * the year. The four digits after are the month and day. The ninth and
     * tenth digits are the current hours (in 24-hour). The last two digits are
     * the minutes of the hour past.
     *
     * @param deviceSerial of the specific device to back up. Set to <i>null</i>
     * to issue command globally.
     * @param location on the hard drive, where the backup will be saved to.
     * @throws IOException If something goes wrong in the process department of
     * things.
     * @throws DeviceNotFoundException If supplied device could not be found.
     * @throws EFSNotFoundException if /efs partition could not be found
     * @return The created instance of the folder containing the backup.
     */
    public File backupEFS(String deviceSerial, String location) throws IOException, DeviceNotFoundException, EFSNotFoundException {
        File f = new File(location + "/efs-" + new Date().getYear() + new Date().getMonth() + new Date().getDay() + "-" + new Date().getHours() + new Date().getMinutes());
        f.mkdirs();
        String result = executeADBCommand(false, false, deviceSerial, new String[]{"pull", "/efs", f.getAbsolutePath()});
        if (result.contains("device not found")) {
            throw new DeviceNotFoundException(result);
        }
        if (result.contains("does not exist")) {
            throw new EFSNotFoundException(result);
        }
        return f;
    }

    /**
     * Attempts to restore the device's EFS-partition from a previously backed
     * up copy. This, as @see backupEFS() does, only works on SAMSUNG devices!
     *
     * @param deviceSerial The specific device to restore. Set to null, to issue
     * command globally.
     * @param location The directory containing the backed-up files.
     * @throws IOException If something goes wrong in the process department.
     * @throws DeviceNotFoundException If ADB reports that the device could not
     * be found.
     */
    public void restoreEFS(String deviceSerial, String location) throws IOException, DeviceNotFoundException {
        String result = executeADBCommand(false, true, deviceSerial, new String[]{"push", location, "/efs"});
        if (result.contains("device not found")) {
            throw new DeviceNotFoundException(result);
        }
    }

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
