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

import java.util.*;
import java.io.*;

import JDroidLib.util.*;
import JDroidLib.android.device.*;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public class ADBController {
    
    CaptainKirk controller = null;
    Device device = null;
    
    /**
     * Retrieves a list of connected devices.
     * @return list of devics.
     * @throws IOException 
     */
    List<String> connectedDevices() throws IOException {
        ////////////////////
        // Get devices ////
        //////////////////
        
        return controller.getConnectedDevices();
    }
    
    public void startServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"start-server"});
    }
    
    public void stopServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"stop-server"});
    }
    
    public void restartServer() throws IOException {
        controller.executeADBCommand(false, false, null, new String[]{"stop-server"});
        controller.executeADBCommand(false, false, null, new String[]{"start-server"});
    }
    
    public List<String> getConnectedDevices() throws IOException {
        return connectedDevices();
    }
    
    public List<String> getConnectedFastbootDevices() throws IOException {
        return controller.getConnectedFastbootDevices();
    }
    
    public ADBController() throws IOException {
        controller = new CaptainKirk();
    }
    
    public Device getDevice(String serial) {
        return new Device(serial);
    }
    
}
