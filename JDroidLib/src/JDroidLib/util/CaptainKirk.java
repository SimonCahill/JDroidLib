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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import JDroidLib.android.controllers.ADBController;
import JDroidLib.android.device.Device;
import JDroidLib.enums.OS;
import JDroidLib.enums.RebootTo;

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
     * @throws IOException if something happens while extracting the files to the hard drive...
     * @throws ZipException if something happens while un-zipping the extracted files...
     * @throws InterruptedException if the thread's sleep(s) get interrupted. Don't ask why it needs to sleep. Let's just say it'll get cranky if it doesn't.
     */
    public CaptainKirk() throws IOException, ZipException, InterruptedException {
        resMan = new ResourceManager();
        if (System.getProperty("os.name").toLowerCase().equals("linux")) {
            resMan.install(OS.LINUX, "Default");
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            resMan.install(OS.MAC_OS, "Default");
        } else {
            resMan.install(OS.WINDOWS, "Default");
        }
        if (System.getProperty("os.name").toLowerCase().equals("linux") || System.getProperty("os.name").toLowerCase().contains("mac")) {
            adb = new File(System.getProperty("user.home") + "/.jdroidlib/bin/adb");
            adb.deleteOnExit();
            fastboot = new File(System.getProperty("user.home") + "/.jdroidlib/bin/fastboot");
            fastboot.deleteOnExit();
        } else {
            adb = new File(System.getProperty("user.home") + "/.jdroidlib/bin/adb.exe");
            adb.deleteOnExit();
            fastboot = new File(System.getProperty("user.home") + "/.jdroidlib/bin/fastboot.exe");
            fastboot.deleteOnExit();
        }
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
            args.add(adb.toString());
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
            processReader.close();
            args.clear();
            process = new ProcessBuilder();
        }

        ////////////////////
        // Execute command/
        //////////////////
        args.add(adb.toString());
        if (deviceSerial != null) {
            args.add("-s");
            args.add(deviceSerial);
        }
        if (shell) {
            args.add("shell");
        }
        args.addAll(Arrays.asList(commands));
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
        args.add(adb.toString());
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
     * @return devices and device states.
     * @throws IOException if something went wrong.
     */
    public List<Device> getConnectedDevices(ADBController controller) throws IOException {
        List<Device> devices = new ArrayList<>();
        String[] cmd = {"devices"};

        String raw = executeADBCommand(false, false, null, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("List ")) {
                continue;
            }
            if(line.trim().isEmpty())//ignore empty lines
            {
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
     * Restarts the ADB deamon as a root user, and allows for more administrative functions to work.
     * @param serial of the device to root.
     * @return the ADB output.
     * @throws IOException if something went wrong.
     */
    public String restartADBAsRoot(String serial) throws IOException {
        return executeADBCommand(false, false, serial, new String[]{"root"});
    }
    

}

/*Please ignore this. This is just here, so I don't always have to open methods, but can just C&P comments I need.*/
///////////////////
// Variables /////
/////////////////
////////////////////
// Execute command/
//////////////////
