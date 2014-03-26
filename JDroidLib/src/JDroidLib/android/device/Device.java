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
import JDroidLib.android.controllers.ADBController;
import JDroidLib.enums.DeviceState;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class Device {

    private SU su = null;
    private BusyBox busybox = null;
    private Battery battery = null;
    private BuildProp buildProp = null;
    private DeviceState state = null;
    private CPU cpu = null;
    private String serial = null;
    
    public Device(String device, ADBController adbController) {
        String[] arr = device.split("\t");

        this.serial = arr[0];
        su = new SU(serial, adbController);
        busybox = new BusyBox(serial, adbController);
        battery = new Battery(serial, adbController);
        cpu = new CPU(serial, adbController);
        buildProp = new BuildProp(serial, adbController);
        state = DeviceState.getState(arr[1]);
    }
    
    /**
     * Gets the devices current state and
     * @return device state.
     */
    public DeviceState getState() {
        return state;
    }
    
    public SU getSU() { return su; }
    
    public BusyBox getBusybox() { return busybox; }
    
    public Battery getBattery() { return battery; }
    
    public CPU getCPU() { return cpu; }
    
    public String getSerial() { return serial; }
    
    public BuildProp getBuildProp() { return buildProp; }

    @Override
    public String toString()
    {
        return String.format("[%s] %s", DeviceState.getState(state), serial);
    }
}
