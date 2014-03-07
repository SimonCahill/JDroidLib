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

import java.io.*;

/**
 *
 * @author beatsleigher
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class BusyBox {
    private String serial = "";
    
    private boolean isInstalled = false;
    private String busyboxVersion = "";
    
    private ADBController adbController = null;
    
    public BusyBox(String serial, ADBController adbController) {
        this.serial = serial;
        this.adbController = adbController;
    }
    
    private void update() throws IOException {
        String[] cmd = {"busybox"};
        String raw = adbController.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains("busybox: not found")) {
                isInstalled = false;
                busyboxVersion = "n/a";
                break;
            } else if (line.startsWith("BusyBox")) {
                isInstalled = true;
                String[] arr = line.split("\\ ");
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
