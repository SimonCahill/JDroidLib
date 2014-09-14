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


import JDroidLib.android.controllers.*;
import JDroidLib.enums.DeviceState;
import JDroidLib.exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Device class. 
 * This class represents a physical Android device and contains methods which allow you to perform different tasks with any connected Android device.
 * This class also contains several methods redirecting you to other classes which represent other parts of the Android device.
 * 
 * This is a singleton-class.
 * @author beatsleigher
 */
public class Device {

    //<editor-fold defaultstate="collapsed" desc="Variables and other boring crap">
    /**
     * Represents the device's SU-installation.
     * @see SU
     */
    private final SU su;
    /**
     * Represents the device's busybox-installation.
     * @see BusyBox
     */
    private final BusyBox busybox;
    /**
     * Represents the device's battery.
     * @see Battery
     */
    private final Battery battery;
    /**
     * Represents the device's build properties.
     * @see BuildProp
     */
    private final BuildProp buildProp;
    /**
     * The device's current state. @see DeviceState.
     */
    private DeviceState state;
    /**
     * Represents the device's @see CPU
     */
    private final CPU cpu;
    /**
     * The device's serial.
     */
    private final String serial;
    /**
     * The @see ADBController instance which powers all the different components here.
     */
    private final ADBController adbController;
    /**
     * The @see FileSystem of the device, which this class represents.
     */
    private final FileSystem fileSystem;
    
    private static List<Device> instances = null;
    //</editor-fold>

    /**
     * Constructor for this @see Device-instance.
     * Prepares this class for use with <b>one</b> specific device.
     * @param device The serial number of the device to represent.
     * @param adbController An instance of ADBController to use in this class (<b>never creates a new instance of @see ADBController</b>)
     */
    Device(String device, ADBController adbController) {
        String[] arr = device.split("\t");
        System.out.println("Device: " + device + ", array length: " + arr.length);
        
        this.adbController = adbController;
        this.serial = arr[0];
        su = new SU(this, adbController);
        busybox = new BusyBox(this, adbController);
        battery = new Battery(this, adbController);
        cpu = new CPU(this, adbController);
        buildProp = new BuildProp(this, adbController);
        if (!(arr.length <= 1))
            state = DeviceState.getState(arr[1].toLowerCase());
        else
            state = DeviceState.getState(device); 
        fileSystem = new FileSystem(this, adbController);
    }
    
    /**
     * Queries through an internal list of @see Device instances and checks for previously created instances with the 
     * serial parameter. If one is found, that @see Device instance will be returned. If no previously created instances match the provided serial,
     * a new instance is created, added to the list and then returned.
     * @param serial The serial number of the device. Used to either query for previously created instances or to create a new instance of @see Device.
     * @param adbController An ADBController instance. Required for the @see Device object to perform different tasks.
     * @return Returns a @see Device instance.
     */
    public static Device getDevice(String serial, ADBController adbController) {
        if (instances == null)
            instances = new ArrayList<>();
        
        if (!instances.isEmpty())
            for (Device instance : instances)
                if (instance.getSerial().equals(serial))
                    return instance;
        Device instance = new Device(serial, adbController);
        instances.add(instance);
        return instance;
        
    }

    /**
     * Refreshes the device's current state.
     * @return Returns the device's current state (after the state was refreshed)
     * @throws IOException If something goes wrong (which shouldn't happen, really)
     */
    public DeviceState getState() throws IOException {
        state = DeviceState.UNKNOWN;
        String output = adbController.executeCommand(true, "devices");
        BufferedReader reader = new BufferedReader(new StringReader(output));
        String line = "";
        while ((line = reader.readLine()) != null)
            if (line.contains(serial)) {
                String[] arr = line.split("\t");
                if (arr.length != 0)
                    state = DeviceState.valueOf(arr[1].toUpperCase());
            }
        
        return state;
    }

    /**
     * Returns an instance of @see SU which corresponds to this device.
     * @return Returns an instance of @see SU, which represents the device's SU-installation
     */
    public SU getSU() { return su; }

    /**
     * Returns an instance of @see BusyBox.
     * @return Instance of @see BusyBox, which represents the device's busybox-installation.
     */
    public BusyBox getBusybox() { return busybox; }

    /**
     * Returns an instance of @see Battery.
     * @return Returns an instance of @see Battery, which represents the device's battery.
     */
    public Battery getBattery() { return battery; }

    /**
     * Returns an instance of @see CPU
     * @return Returns an instance of @see CPU, which represents the device's CPU.
     */
    public CPU getCPU() { return cpu; }

    /**
     * Returns the device's serial-number.
     * @return Returns the device's serial number as a @see String.
     */
    public String getSerial() { return serial; }

    /**
     * Returns an instance of @see BuildProp.
     * @return Returns an instance of @see BuildProp, which represents the device's build properties.
     */
    public BuildProp getBuildProp() { return buildProp; }

    
    /**
     * @return Returns the description of this object with some basic information about the device this object represents (serial #, state, rooted?, battery level)
     */
    @Override
    public String toString() { 
            try {
                return String.format("Device. Serial: {0}, State {1}, Is Rooted: {2}, Battery Level: {3}", 
                             serial, DeviceState.getState(state), String.valueOf(hasRoot()), getBattery().getLevel());
            } catch (IOException ex) {
                return String.format("Device. Serial: {0}, State {1}, Is Rooted: {2}, Battery Level: {3}", 
                             serial, DeviceState.getState(state), "n/a", "n/a");
            }
    }
    
    /**
     * Returns an instance of @see PackageController.
     * @return Returns an instance of @see PackageController, which represents the device's package manager.
     */
    public PackageController getPackageController() { return new PackageController(adbController, this); }
    
    /**
     * Installs an application to this device (the device represented by this object)
     * @param apk The application to install
     * @return True, if installation was a success, false is otherwise.
     * @throws IOException If something goes wrong.
     * @throws NullPointerException ^
     */
    public boolean installApplication(String apk) throws IOException, NullPointerException {
        String output = adbController.executeCommand(this, false, true, "install", apk);
        BufferedReader reader = new BufferedReader(new StringReader(output));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("success"))
                return true;
        }
        return false;
    }
    
    /**
     * Reboots the device into the specified device state (Choose between: RECOVERY, FASTBOOT, DEVICE), basically.
     * @param stateToBootInto The state to which the device should be rebooted into...
     * @throws IOException If something goes wrong... Shouldn't happen. TOOOTALLY confident about that :)
     * @throws JDroidLib.exceptions.InvalidModeException If the requested reboot is not available
     * 
     */
    public void reboot(DeviceState stateToBootInto) throws IOException {
        if (stateToBootInto == null)
            throw new IllegalArgumentException("Invalid parameter: stateToBootInto must not be null!");
        
        if (stateToBootInto == DeviceState.BOOTLOADER || stateToBootInto == DeviceState.FASTBOOT || stateToBootInto == DeviceState.RECOVERY || stateToBootInto == DeviceState.DEVICE) {
            if (stateToBootInto == DeviceState.DEVICE) {
                adbController.executeCommand(this, false, true, "reboot");
                return;
            }
            adbController.executeCommand(this, false, true,"reboot", DeviceState.getState(stateToBootInto));
        } else throw new IllegalArgumentException("The requested reboot-mode is not vald. Requested mode: " + stateToBootInto);
    }

    /**
     * Checks whether the device has root access.
     * @return True, if root access is available, false if not.
     * @throws IOException If something goes wrong.
     */
    public boolean hasRoot() throws IOException { return su.hasRoot(); }
    
    /**
     * Gets an instance of the device's file system.
     * @return @see FileSystem
     */
    public FileSystem getFileSystem() { return fileSystem; }
    
    /**
     * Remounts the device's filesystem, so files can be pushed and pulled to and from the filesystem.
     * @throws IOException If something goes wrong during process execution.
     */
    public void remountDevice() throws IOException {
        adbController.executeCommand(this, false, false, "remount");
    }
    
}
