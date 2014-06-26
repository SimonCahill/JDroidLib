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
package JDroidLib.util;

import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.util.*;

import JDroidLib.android.controllers.ADBController;
import JDroidLib.android.device.Device;
import JDroidLib.enums.*;
import JDroidLib.exceptions.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * This is Captain Kirk! Say hello! He will be our commander and captain,
 * throughout this journey. He will defeat those meanies and protect your device
 * from unwanted stuff. Or, he will at some point in time!
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend"})
public class CaptainKirk {

    ResourceManager resMan = null;
    private File adb = null;
    private File fastboot = null;

    /**
     * Default constructor: Installs ADB/fastboot and gets other data.
     *
     * @throws IOException if something happens while extracting the files to
     * the hard drive...
     * @throws ZipException if something happens while un-zipping the extracted
     * files...
     * @throws InterruptedException if the thread's sleep(s) get interrupted.
     * Don't ask why it needs to sleep. Let's just say it'll get cranky if it
     * doesn't.
     * @throws JDroidLib.exceptions.OSNotSupportedException If JDroi8dLib detects an unsupported OS.
     */
    public CaptainKirk() throws IOException, ZipException, InterruptedException, OSNotSupportedException {
        System.out.println("Preparing resources...");
        resMan = new ResourceManager();
        System.out.println("Determining install path for ADB binaries...");
        if (System.getProperty("os.name").toLowerCase().equals("linux")) {
            System.out.println("Installing Linux binaries...");
            resMan.install(OS.LINUX, "Default");
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.out.println("Installing Mac OS X binaries...");
            resMan.install(OS.MAC_OS, "Default");
        } else {
            System.out.println("Installing Windows binaries...");
            resMan.install(OS.WINDOWS, "Default");
        }
        
        System.out.println("Getting files and paths...");
        adb = resMan.getADB(); adb.deleteOnExit();
        fastboot = resMan.getFastboot(); fastboot.deleteOnExit();
        
        if (!fastboot.exists()) 
            if (System.getProperty("os.name").toLowerCase().contains("windows"))
                try {
                    System.out.println("Fastboot binary not found. Downloading from server...");
                    ReadableByteChannel channel = Channels.newChannel(new URL("http://team-m4gkbeatz.eu/download/binaries/adb-win/fastboot.exe").openStream());
                    FileOutputStream output = new FileOutputStream(fastboot);
                    // Write to file
                    output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                } finally {
                    System.out.println("Fastboot: File transfer complete!");
                }
            else if (System.getProperty("os.name").toLowerCase().equals("linux")) 
                try {
                    System.out.println("Fastboot binary not found. Downloading from server...");
                    ReadableByteChannel channel = Channels.newChannel(new URL("http://team-m4gkbeatz.eu/download/binaries/adb-linux/fastboot").openStream());
                    FileOutputStream output = new FileOutputStream(fastboot);
                    // Write to file
                    output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                } finally {
                    System.out.println("Fastboot: File transfer complete!");
                }
            else // Assume it's a Mac - BSD isn't supported by JDroidLib, so it's pretty safe to make this assumption
                try {
                    System.out.println("Fastboot binary not found. Downloading from server...");
                    ReadableByteChannel channel = Channels.newChannel(new URL("http://team-m4gkbeatz.eu/download/binaries/adb-mac/fastboot").openStream());
                    FileOutputStream output = new FileOutputStream(fastboot);
                    // Write to file
                    output.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
                } finally {
                    System.out.println("Fastboot: File transfer complete!");
                }
        
        System.out.println("ADB Location: " + adb.getAbsolutePath() + "\nFastboot Location: " + fastboot.getAbsolutePath());
        System.out.println("Resources initialized...");
    }

    /**
     * Executes an ADB command:
     *
     * @param shell Issue a shell command.
     * @param remount the device.
     * @param deviceSerial Issue the command to a specific device. If this is
     * set to "", or null, it will be ignored and the command will be issued
     * globally.
     * @param commands to be executed. (Also used for process args when using
     * shell).
     * @return ADB output as String.
     * @throws IOException when something went wrong.
     */
    public String executeADBCommand(boolean shell, boolean remount, String deviceSerial, String[] commands) throws IOException {
        ///////////////////
        // Variables /////
        /////////////////
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        Process pr = null;
        BufferedReader processReader = null;
        List<String> args = new ArrayList<>();
        String line = "";

        ////////////////////
        // Remount device//
        //////////////////
        if (remount) {
            args.add(adb.getAbsolutePath());
            if (deviceSerial != null && !deviceSerial.isEmpty()) {
                args.add("-s " + deviceSerial);
            }
            
            args.add("remount");
            process.command(args);
            pr = process.start();
            processReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            while ((line = processReader.readLine()) != null) {
                // Wait for remounting to finish.
            }
            
            pr.destroy();
            pr = null;
            processReader.close();
            args.clear();
            process = new ProcessBuilder();
        }

        ////////////////////
        // Execute command/
        //////////////////
        args.add(adb.getAbsolutePath());
        if (deviceSerial != null && !deviceSerial.equals("")) {
            args.add("-s");
            args.add(deviceSerial);
        }
        if (shell) 
            args.add("shell");
        
        for (String arg : commands) {
            System.out.println("Found arg: " + str);
            args.add(arg);
        }
        
        process.command(args);
        pr = process.start();
        processReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = processReader.readLine()) != null) {
            System.out.println(line);
            if (!line.startsWith("*"))
                str.append(line + "\n");
        }
        pr.destroy();
        processReader.close();
        args.clear();
        line = null;

        return str.toString();
    }
    
    /**
     * Executes an ADB command with the provided commands/arguments.
     * Please note: JDroidLib takes care of the initial arguments, such as the ADB command itself and the <b>specific device</b> command and (if applicable) the <i>shell</i> command..
     * Please only provide this method with secondary commands, for example: 
     * <b>EXAMPLE METHOD:</b>
     * List< String > args = new ArrayList<>();
     * ADBController adbController = new ADBController();
     * Device aDevice = adbController.getDevice("a device's serial");
     * 
     * args.add("su"); // Initial command is set to su (super user binary)
     * args.add("-v"); // Secondary command is set to su's -v arg (version)
     * 
     * 
     * adbController.executeADBCommand(true, false, <i>aDevice</i>, args); // Collects all necessary arguments and then issues the command to the specific device.
     * @param asShell Issue command as a shell command
     * @param remount Remount the device prior to executing the command
     * @param device A device object. (The specific device to which to issue the command).
     * @param command A list of commands/arguments to execute (please do <b>NOT</b> add <i>adb</i> or any direct ADB commands!
     * @return ADB's output
     * @throws IOException If something goes wrong when executing the process or reading from the process.
     * @throws NullPointerException If no device or command is provided.
     */
    public String executeADBCommand(boolean asShell, boolean remount, Device device, List<String> command) throws IOException, NullPointerException {
        //# =============== Variables =============== #\\
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        Process pr = null;
        BufferedReader reader = null;
        String line = null;
        List<String> args = new ArrayList<>();
        
        if (device == null)
            throw new NullPointerException("Device object not set to the instance of an object.");
        if (command.isEmpty())
            throw new NullPointerException("No commands/arguments were issued.");
        
        if (remount)
            remountDevice(device);
        
        args.add(adb.getAbsolutePath());
        args.add("-s");
        args.add(device.toString());
        if (asShell)
            args.add("shell");
        for (String s : command)
            args.add(s);
        
        //# =============== Execute Commands =============== #\\
        process.command(args);
        process.redirectErrorStream(true);
        pr = process.start();
        reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = reader.readLine()) != null)
            if (!line.startsWith("*"))
                str.append(line + "\n");
        if (str.toString().trim().equals(""))
            return null;
        else return str.toString();
    }
    
    /**
     * Executes an ADB command with the provided commands/arguments.
     * Please note: JDroidLib takes care of the initial arguments, such as the ADB command itself and the <b>specific device</b> command and (if applicable) the <i>shell</i> command..
     * Please only provide this method with secondary commands, for example: 
     * <b>EXAMPLE METHOD:</b>
     * List< String > args = new ArrayList<>();
     * ADBController adbController = new ADBController();
     * Device aDevice = adbController.getDevice("a device's serial");
     * 
     * args.add("su"); // Initial command is set to su (super user binary)
     * args.add("-v"); // Secondary command is set to su's -v arg (version)
     * 
     * 
     * adbController.executeADBCommand(true, false, <i>aDevice</i>, args); // Collects all necessary arguments and then issues the command to the specific device.
     * @param asShell Issue command as a shell command
     * @param remount Remount the device prior to executing the command
     * @param serial A String object. (The specific device to which to issue the command).
     * @param commands A list of commands/arguments to execute (please do <b>NOT</b> add <i>adb</i> or any direct ADB commands!
     * @return ADB's output
     * @throws IOException If something goes wrong when executing the process or reading from the process.
     * @throws NullPointerException If no device or command is provided.
     */
    public String executeADBCommand(boolean asShell, boolean remount, String serial, List<String> commands) throws IOException {
        //# =============== Variables =============== #\\
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        Process pr = null;
        BufferedReader reader = null;
        String line = null;
        List<String> args = new ArrayList<>();
        
        if (serial == null)
            throw new NullPointerException("Device object not set to the instance of an object.");
        if (commands.isEmpty())
            throw new NullPointerException("No commands/arguments were issued.");
        
        if (remount)
            remountDevice(serial);
        
        args.add(adb.getAbsolutePath());
        args.add("-s");
        args.add(serial);
        if (asShell)
            args.add("shell");
        for (String s : commands)
            args.add(s);
        
        //# =============== Execute Commands =============== #\\
        process.command(args);
        process.redirectErrorStream(true);
        pr = process.start();
        reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = reader.readLine()) != null)
            if (!line.startsWith("*"))
                str.append(line + "\n");
        if (str.toString().trim().equals(""))
            return null;
        else return str.toString();
    }
    
    public String executeADBCommand(boolean asShell, boolean remount, Device device, String[] commands) throws IOException, NullPointerException {
        //# =============== Variables =============== #\\
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        Process pr = null;
        BufferedReader reader = null;
        String line = null;
        List<String> args = new ArrayList<>();
        
        if (device == null)
            throw new NullPointerException("Device object not set to the instance of an object.");
        if (commands.length == 0)
            throw new NullPointerException("No commands/arguments were issued.");
        
        if (remount)
            remountDevice(device);
        
        args.add(adb.getAbsolutePath());
        args.add("-s");
        args.add(device.toString());
        if (asShell)
            args.add("shell");
        args.addAll(Arrays.asList(commands));
        
        //# =============== Execute Commands =============== #\\
        process.command(args);
        process.redirectErrorStream(true);
        pr = process.start();
        reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = reader.readLine()) != null)
            if (!line.startsWith("*"))
                str.append(line + "\n");
        if (str.toString().trim().equals(""))
            return null;
        else return str.toString();
    }
    
    /**
     * Remounts a given device.
     * @param device The device to remount
     * @throws IOException If something goes wrong while executing the process.
     * @throws NullPointerException If no device is issued.
     */
    public void remountDevice(Device device) throws IOException, NullPointerException {
        
        if (device == null)
            throw new NullPointerException("No device object provided");
        
        //# =============== Execute Command =============== #\\
        List<String> args = new ArrayList<>();
        args.add("remount");
        executeADBCommand(false, false, device, args);
    }
    
    /**
     * Remounts a given device.
     * @param serial The device to remount
     * @throws IOException If something goes wrong while executing the process.
     * @throws NullPointerException If no device is issued.
     */
    public void remountDevice(String serial) throws IOException {        
        //# =============== Execute Command =============== #\\
        List<String> args = new ArrayList<>();
        if (serial != null && !serial.equals("")) {
            args.add("-s");
            args.add(serial);
        }
        args.add("remount");
        executeADBCommand(false, false, serial, args);
    }

    /**
     * Executes a fastboot command:
     *
     * @param deviceSerial the target device's serial. If this is set to "" or
     * null, it will be ignored and the command will be issued globally.
     * @param commands the commands to be executed.
     * @return the output.
     * @throws IOException if something went wrong.
     */
    public String executeFastbootCommand(String deviceSerial, String[] commands) throws IOException {
        ///////////////////
        // Variables /////
        /////////////////
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        Process pr = null;
        BufferedReader processReader = null;
        List<String> args = new ArrayList<>();
        String line = "";

        ////////////////////
        // Execute command/
        //////////////////
        args.add(fastboot.toString());
        if (deviceSerial != null) {
            args.add("-s");
            args.add(deviceSerial);
        }
        args.addAll(Arrays.asList(commands));
        process.command(args);
        pr = process.start();
        processReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = processReader.readLine()) != null) {
            if (!line.startsWith("*"))
                str.append(line + "\n");
        }
        pr.destroy();
        processReader.close();
        args.clear();
        line = null;

        return str.toString();
    }

    /**
     * Reboots device to desired mode -- Will not work if device is in fastboot!
     * For fastboot-reboots, use the respective method!
     *
     * @param deviceSerial The specific device to reboot. Will be ignored if ""
     * or null!
     * @param mode The mode to reboot into.
     * @return the process output.
     * @throws IOException if something went wrong.
     */
    public String ADB_rebootDevice(String deviceSerial, RebootTo mode) throws IOException {
        ///////////////////
        // Variables /////
        /////////////////
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        List<String> args = new ArrayList<>();
        Process pr = null;
        BufferedReader processReader = null;
        String line = "";

        ////////////////////
        // Execute command/
        //////////////////
        args.add(adb.getAbsolutePath());
        if (deviceSerial != null && !deviceSerial.isEmpty()) {
            args.add("-s");
            args.add(deviceSerial);
        }
        switch (mode) {
            case ANDROID:
                args.add("reboot");
                break;
            case RECOVERY:
                args.add("reboot");
                args.add("recovery");
                break;
            case BOOTLOADER:
                args.add("reboot-bootloader");
                break;
        }
        process.command(args);
        pr = process.start();
        processReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = processReader.readLine()) != null) {
            if (!line.startsWith("*"))
                str.append(line + "\n");
        }
        pr.destroy();
        processReader.close();
        args.clear();
        line = null;

        return str.toString();
    }

    /**
     * Reboots device to desired mode -- Will NOT work if device is booted to
     * recovery or Android! Use ADB:rebootDevice() for those reboots!
     *
     * @param deviceSerial The specific device.
     * @param mode The mode to reboot the device to.
     * @return the output.
     * @throws IOException if something went wrong.
     */
    public String fastboot_rebootDevice(String deviceSerial, RebootTo mode) throws IOException {
        ///////////////////
        // Variables /////
        /////////////////
        StringBuilder str = new StringBuilder();
        ProcessBuilder process = new ProcessBuilder();
        List<String> args = new ArrayList<>();
        Process pr = null;
        BufferedReader processReader = null;
        String line = "";

        ////////////////////
        // Execute command/
        //////////////////
        args.add(fastboot.toString());
        if (deviceSerial != null && !deviceSerial.isEmpty()) {
            args.add("-s " + deviceSerial);
        }
        switch (mode) {
            case ANDROID:
                args.add("reboot");
                break;
            case RECOVERY:
                args.add("reboot");
                break;
            case BOOTLOADER:
                args.add("reboot-bootloader");
                break;
        }
        process.command(args);
        pr = process.start();
        processReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = processReader.readLine()) != null) {
            str.append(line + "\n");
        }
        pr.destroy();
        processReader.close();
        args.clear();
        line = null;

        return str.toString();
    }

    /**
     * Gets a List(String) of devices and their respective states.
     *
     * @param controller
     * @return devices and device states.
     * @throws IOException if something went wrong.
     */
    public List<Device> getConnectedDevices(ADBController controller) throws IOException {
        List<Device> devices = new ArrayList<>();
        String[] cmd = new String[]{"devices"};

        String raw = executeADBCommand(false, false, "", cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("List ")) {
                continue;
            }
            if (line.trim().isEmpty())/*Ignore empty lines*/ {
                continue;
            }
            Device device = new Device(line, controller);
            devices.add(device);
        }

        return devices;
    }

    /**
     * Gets a list (String) of devices connected to the computer in fastboot
     * mode.
     *
     * @return list of devices and respective modes.
     * @throws IOException if something went wrong.
     */
    public List<String> getConnectedFastbootDevices() throws IOException {
        List<String> devs = new ArrayList<>();

        String raw = executeFastbootCommand(null, new String[]{"devices"});
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("List ")) {
                continue;
            }
            if (line.equals("")) {
                continue;
            }
            devs.add(line);
        }
        return devs;
    }

    /**
     * Restarts the ADB deamon as a root user, and allows for more
     * administrative functions to work.
     *
     * @param serial of the device to root.
     * @return the ADB output.
     * @throws IOException if something went wrong.
     */
    public String restartADBAsRoot(String serial) throws IOException {
        return executeADBCommand(false, false, serial, new String[]{"root"});
    }
    
    /**
     * Returns the currently used ADB file.
     * @return 
     */
    public File getADB() { return adb; }

}

/*Please ignore this. This is just here, so I don't always have to open methods, but can just C&P comments I need.*/
///////////////////
// Variables /////
/////////////////
////////////////////
// Execute command/
//////////////////
