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

import JDroidLib.android.controllers.ADBController;

import java.io.*;

/**
 * Represents a device's Busybox-installation.
 * @author beatsleigher
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class BusyBox {
    
    private final Device device;
    
    private boolean isInstalled = false;
    private String busyboxVersion = "";
    
    private final ADBController adbController;
    
    BusyBox(Device device, ADBController adbController) {
        this.device = device;
        this.adbController = adbController;
    }
    
    /**
     * Updates all the information represented by this class.
     * @throws IOException If an error occurs while executing ADB commands.
     */
    private void update() throws IOException {
        String raw = adbController.executeCommand(device, true, true, "busybox");
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains("busybox: not found")) {
                isInstalled = false;
                busyboxVersion = "n/a";
                break;
            } else if (line.startsWith("BusyBox")) {
                isInstalled = true;
                String[] arr = line.split("\\s");
                busyboxVersion = arr[1];
                break;
            }
        }
        reader.close();
    }
    
    /**
     * Determines whether BusyBOx is installed or not, and 
     * @return true if it is installed, false if otherwise.
     * @throws IOException if something went wrong.
     */
    public boolean isInstalled() throws IOException { update(); return isInstalled;}
    
    /**
     * Gets the version of the installed busybox binary and
     * @return version, if available, or n/a if otherwise.
     * @throws IOException  if something went wrong.
     */
    public String getVersion() throws IOException { update(); return busyboxVersion; }
}
