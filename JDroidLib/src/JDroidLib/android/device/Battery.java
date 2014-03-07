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
public class Battery {
    private boolean isACPowered = false;
    private boolean isUSBPowered = false;
    private boolean isWirelessPowered = false;
    private boolean isPresent = false;
    
    private int batteryStatus = 0;
    private int batteryHealth = 0;
    private int batteryLevel = 0;
    private int batteryScale = 0;
    private int batteryVoltage = 0;
    private int currentBatteryCurrent = 0;
    private int batteryTemp = 0;
    
    private String batteryTechnology = "";
    private String serial = "";
    
    private ADBController adbController = null;
    
    public Battery(String serial, ADBController adbController) {
        this.serial = serial;
        this.adbController = adbController;
    }
    
    public boolean isACPowered() throws IOException { update(); return isACPowered; }
    
    public boolean isUSBPowered() throws IOException { update(); return isUSBPowered; }
    
    public boolean isWirelessPowered() throws IOException { update(); return isWirelessPowered; }
    
    public boolean isPresent() throws IOException { update(); return isPresent; }
    
    public int getStatus() throws IOException { update(); return batteryStatus; }
    
    public int getHealth() throws IOException { update(); return batteryHealth; }
    
    public int getLevel() throws IOException { update(); return batteryLevel; }
    
    public int getScale() throws IOException { update(); return batteryScale; }
    
    public int getVoltage() throws IOException { update(); return batteryVoltage; }
    
    public int getCurrent() throws IOException { update(); return currentBatteryCurrent; }
    
    public int getTemp() throws IOException { update(); return batteryTemp; }
    
    public String getTechnology() throws IOException { update(); return batteryTechnology; }
    
    private void update() throws IOException {
        String[] commands = {"dumpsys", "battery"};
        String raw = adbController.executeADBCommand(true, false, serial, commands);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.equals("Current Battery Service State:")) continue;
                if (line.contains("AC powered:")) {
                    String[] arr = line.split(": ");
                    isACPowered = Boolean.valueOf(arr[1]);
                }
                if (line.contains("USB powered: ")) {
                    String[] arr = line.split(": ");
                    isUSBPowered = Boolean.valueOf(arr[1]);
                }
                if (line.contains("Wireless powered: ")) {
                    String[] arr = line.split(": ");
                    isWirelessPowered = Boolean.valueOf(arr[1]);
                }
                if (line.contains("status: ")) {
                    String[] arr = line.split(": ");
                    batteryStatus = Integer.valueOf(arr[1]);
                }
                if (line.contains("health: ")) {
                    String[] arr = line.split(": ");
                    batteryHealth = Integer.valueOf(arr[1]);
                }
                if (line.contains("present: ")) {
                    String[] arr = line.split(": ");
                    isPresent = Boolean.valueOf(arr[1]);
                }
                if (line.contains("level:")) {
                    String[] arr = line.split(": ");
                    batteryLevel = Integer.valueOf(arr[1]);
                }
                if (line.contains("scale: ")) {
                    String[] arr = line.split(": ");
                    batteryScale = Integer.valueOf(arr[1]);
                }
                if (line.contains("voltage: ")) {
                    String[] arr = line.split(": ");
                    batteryVoltage = Integer.valueOf(arr[1]);
                }
                if (line.contains("current now: ")) {
                    String[] arr = line.split(": ");
                    currentBatteryCurrent = Integer.valueOf(arr[1]);
                }
                if (line.contains("temperatur: ")) {
                    String[] arr = line.split(": ");
                    batteryTemp = Integer.valueOf(arr[1]);
                }
                if (line.contains("technology: ")) {
                    String[] arr = line.split(": ");
                    batteryTechnology = arr[1];
                }
            }
    }
}
