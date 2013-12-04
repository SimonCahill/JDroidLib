/*
 * Copyright (C) 2013 Simon.
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

package JDroidLib.Android;

import java.util.*;
import JDroidLib.installers.*;
import JDroidLib.exceptions.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Simon
 */
public final class Controller {
    Adb adb = new Adb();
    AdbInstaller installerAdb = new AdbInstaller();
    FastbootInstaller installerFastboot = new FastbootInstaller();
    
    private Controller instance;
    private List<String> connectedDevices;
    
    public Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            try {
                instance.install();
            } catch (    InvalidOSException | IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            adb.startServer();
        }
        return instance;
    }
    
    private void install() throws InvalidOSException, MalformedURLException, IOException {
     installerAdb.installAdb();
     installerFastboot.installFastboot();
    }
    
    public List<String> getConnectedDevices() {
        updateDeviceList();
        return connectedDevices;
    }
    
    public void updateDeviceList() {
        String deviceList = "";
        
        connectedDevices.clear();
    }
}
