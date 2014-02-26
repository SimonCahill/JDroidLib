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

import java.io.*;
import java.util.*;

import JDroidLib.enums.*;
import JDroidLib.util.*;

/**
 *
 * @author beatsleigher
 */
public class Device {
    
    private CaptainKirk controller = null;
    
    /*  private SU su = null;
        private BusyBox busybox = null;
        private Battery battery = null;*/
    private DeviceState state = null;
    private String serial = null;
    
    public Device(String deviceSerial) {
        this.serial = deviceSerial;
        
        controller = new CaptainKirk();
    }
    
}
