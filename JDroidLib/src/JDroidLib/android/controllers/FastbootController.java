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

package JDroidLib.android.controllers;

import JDroidLib.enums.RebootTo;
import JDroidLib.util.CaptainKirk;

import java.io.*;
import net.lingala.zip4j.exception.ZipException;

/**
 * FastbootController - The right way to do anything fastboot.
 * FastbootController contains all the methods and code, needed to perform (almost) every task, you'll require fastboot to do.
 * These include:
 *  - Flashing
 *  - Updating
 *  - Rebooting
 *  - Device listing.
 * @author beatsleigher
 */
public class FastbootController {
    
    CaptainKirk controller = null;
    
    /**
     * Recommended constructor.
     * Always used by ADBController, use method @see #code ADBController.getFastbootController() to get an instance of this class.
     * @param controller 
     */
    public FastbootController(CaptainKirk controller) {
        this.controller = controller;
    }
    
    /**
     * Emergency constructor. 
     * This constructor will attempt to install ADB and fastboot, and will cause problems when ADBController is in use.
     * Unless your program is specifically designed to work <i>only</i> with fastboot, use @see #code ADBController.getFastbootController().
     * @throws IOException
     * @throws ZipException
     * @throws InterruptedException 
     */
    public FastbootController() throws IOException, ZipException, InterruptedException { controller = new CaptainKirk(); }
    
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
    public String rebootDeviceFastboot(String deviceSerial, RebootTo mode) throws IOException {
        return controller.fastboot_rebootDevice(deviceSerial, mode);
    }
    
}
