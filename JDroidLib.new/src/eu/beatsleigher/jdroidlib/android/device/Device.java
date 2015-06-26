/*
 * Copyright (C) 2015 Simon.
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
package eu.beatsleigher.jdroidlib.android.device;

import eu.beatsleigher.jdroidlib.android.AndroidController;

/**
 * Device
 * Represents an Android device that is connected to the local machine.
 * @author Simon
 */
public class Device {
    
    private final String serialNumber;
    private boolean _hasRoot;
    
    /**
     * Creates a new instance of this class with the given serial number.
     * @param sender The class requesting a new instance of this class.
     * @param serial The serialnumber of the device this class represents.
     */
    public Device(Object sender, String serial) {
        if (sender instanceof AndroidController) // Don't change this or use this to your advantage!
            this.serialNumber = serial;
        else throw new IllegalArgumentException();
    }
    
    /**
     * Gets a value indicating whether this device has root access or not.
     * @return {@code true} if the device has root access, {@code false} if not.
     */
    public boolean hasRoot() { return _hasRoot; }
    
    /**
     * Gets the device's serial number.
     * @return The device's serial number.
     */
    public String getSerial() { return serialNumber; }
    
}
