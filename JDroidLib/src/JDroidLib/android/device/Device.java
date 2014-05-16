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


import JDroidLib.android.controllers.*;
import JDroidLib.enums.DeviceState;
import JDroidLib.exceptions.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class Device {

    /**
     * Represents the device's SU-installation.
     * @see SU
     */
    private SU su = null;
    /**
     * Represents the device's busybox-installation.
     * @see BusyBox
     */
    private BusyBox busybox = null;
    /**
     * Represents the device's battery.
     * @see Battery
     */
    private Battery battery = null;
    /**
     * Represents the device's build properties.
     * @see BuildProp
     */
    private BuildProp buildProp = null;
    /**
     * The device's current state. @see DeviceState.
     */
    private DeviceState state = null;
    /**
     * Represents the device's @see CPU
     */
    private CPU cpu = null;
    /**
     * The device's serial.
     */
    private String serial = null;
    /**
     * The @see ADBController instance which powers all the different components here.
     */
    private ADBController adbController = null;

    /**
     * Constructor for this @see Device-instance.
     * Prepares this class for use with <b>one</b> specific device.
     * @param device The serial number of the device to represent.
     * @param adbController An instance of ADBController to use in this class (<b>never creates a new instance of @see ADBController</b>)
     */
    public Device(String device, ADBController adbController) {
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
    }

    /**
     * Refreshes the device's current state.
     * @return Returns the device's current state (after the state was refreshed)
     * @throws IOException If something goes wrong (which shouldn't happen, really)
     */
    public DeviceState getState() throws IOException {
        state = DeviceState.UNKNOWN;
        String output = adbController.executeADBCommand(false, false, serial, new String[]{"devices"});
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
     * Returns the serial number of the device currently represented by this Device object.
     * @return (See description)
     */
    @Override
    public String toString() { return serial; }
    
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
        List<String> args = new ArrayList<>();
        args.add("install");
        args.add(apk);
        String output = adbController.executeADBCommand(false, false, this, args);
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
    public void reboot(DeviceState stateToBootInto) throws IOException, InvalidModeException {
        if (stateToBootInto == null)
            throw new NullPointerException("Invalid parameter: stateToBooInto must not be null!");
        
        if (stateToBootInto == DeviceState.BOOTLOADER || stateToBootInto == DeviceState.FASTBOOT || stateToBootInto == DeviceState.RECOVERY || stateToBootInto == DeviceState.DEVICE) {
            if (stateToBootInto == DeviceState.DEVICE) {
                adbController.executeADBCommand(false, false, this, new String[]{"reboot"});
                return;
            }
            adbController.executeADBCommand(false, false, this, new String[]{"reboot", DeviceState.getState(stateToBootInto)});
        } else throw new InvalidModeException("The requested reboot-mode is not vald. Requested mode: " + stateToBootInto);
    }

}
