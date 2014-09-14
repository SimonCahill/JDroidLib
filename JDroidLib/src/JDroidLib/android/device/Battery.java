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
 * Represents a device's battery.
 * @author beatsleigher
 */
@SuppressWarnings({"FieldMayBeFinal"})
public class Battery {
    
    /**
     * Contains all possible battery statuses.
     */
    public enum BatteryStatus {
        /**
         * Battery status is unknown - Could indicate the battery is dead.
         */
        BATTERY_STATUS_UNKNOWN,
        
        /**
         * The battery is currently charging.
         */
        BATTERY_STATUS_CHARGING,
        
        /**
         * The battery is currently discharging.
         */
        BATTERY_STATUS_DISCHARGING,
        
        /**
         * The battery is not charging, it's idle.
         */
        BATTERY_STATUS_NOT_CHARGING,
        
        /**
         * The battery is fully charged.
         */
        BATTERY_STATUS_FULL;
    }
    
    /**
     * Contains all possible battery health stuff...
     */
    public enum BatteryHealth {
        /**
         * The health of the battery is unknown - Could indicate a dead battery.
         */
        BATTERY_HEALTH_UNKOWN,
        
        /**
         * The battery is in good health. Good on the phone-holder. Pat him on the back.
         */
        BATTERY_HEALTH_GOOD,
        
        /**
         * The battery is over heating!
         */
        BATTERY_HEALTH_OVERHEAT,
        
        /**
         * The battery is dead, replace at will.
         */
        BATTERY_HEALTH_DEAD,
        
        /**
         * The battery is getting too many volts.
         */
        BATTERY_HEALTH_OVER_VOLTAGE,
        
        /**
         * The battery has failed, but the failure doesn't ring any bells, sorry.
         */
        BATTERY_HEALTH_UNSPECIFIED_FAILURE,
        
        /**
         * The battery is cold. It's like cold-starting an engine. They don't like it.
         */
        BATTERY_HEALTH_COLD;
    }
    
    private boolean isACPowered = false;
    private boolean isUSBPowered = false;
    private boolean isWirelessPowered = false;
    private boolean isPresent = false;
    
    private BatteryStatus batteryStatus = BatteryStatus.BATTERY_STATUS_UNKNOWN;
    private BatteryHealth batteryHealth = BatteryHealth.BATTERY_HEALTH_UNKOWN;
    private int batteryLevel = 0;
    private int batteryScale = 0;
    private int batteryVoltage = 0;
    private int currentBatteryCurrent = 0;
    
    private String batteryTemp = "0";
    private String batteryTechnology = "";
    
    private final Device device;
    
    private ADBController adbController = null;
    
    Battery(Device device, ADBController adbController) {
        this.device = device;
        this.adbController = adbController;
    }
    
    /**
     * Determines whether the device is powered via AC or not.
     * @return {@code true} if the device is being powered via AC, {@code false} if not.
     * @throws IOException If an error occurs while updating the information.
     */
    public boolean isACPowered() throws IOException { update(); return isACPowered; }
    
    /**
     * Determines whether the device is powered via USB or not.
     * @return {@code true} if the device is being powered via USB, {@code false} if not.
     * @throws IOException If an error occurs while updating the information.
     */
    public boolean isUSBPowered() throws IOException { update(); return isUSBPowered; }
    
    /**
     * Determines whether the device is powered via a wireless charging station or not.
     * @return {@code true} if the device is powered via a wireless station, {@code false} if not.
     * @throws IOException 
     */
    public boolean isWirelessPowered() throws IOException { update(); return isWirelessPowered; }
    
    /**
     * Determines whether the battery is inserted into the device.
     * @return {@code true} is the battery is inserted, {@code false} if not.
     * @throws IOException If an error occurs while updating the information.
     */
    public boolean isPresent() throws IOException { update(); return isPresent; }
    
    /**
     * Determines the current status of the battery.
     * @return
     * @throws IOException 
     */
    public BatteryStatus getStatus() throws IOException { update(); return batteryStatus; }
    
    /**
     * Determines the current health of the battery.
     * @return Return the health of the battery.
     * @throws IOException If an error occurs while updating the information.
     */
    public BatteryHealth getHealth() throws IOException { update(); return batteryHealth; }
    
    /**
     * Determines the current battery level.
     * @return The battery level.
     * @throws IOException If an error occurs while updating the information.
     */
    public int getLevel() throws IOException { update(); return batteryLevel; }
    
    /**
     * Determines the battery's scale (The maximum percentage of charge [default: 0-100]).
     * @return The battery scale.
     * @throws IOException If an error occurs while updating the information.
     */
    public int getScale() throws IOException { update(); return batteryScale; }
    
    /**
     * Determines the current battery voltage.
     * @return The current voltage.
     * @throws IOException 
     */
    public int getVoltage() throws IOException { update(); return batteryVoltage; }
    
    /**
     * Determines the current battery current (mAmps).
     * @return The battery current (mAmps).
     * @throws IOException If an error occurs while updating the information.
     */
    public int getCurrent() throws IOException { update(); return currentBatteryCurrent; }
    
    /**
     * Determines the current battery temperature.
     * @return The battery temperature.
     * @throws IOException if an error occurs while updating the information.
     */
    public double getTemp() throws IOException {
        update();
        String temp = batteryTemp;
        double val = 0;
        val = (Double.valueOf(temp) / 10);
        return val;
    }
    
    /**
     * Determines the battery's technology.
     * @return The battery's technology.
     * @throws IOException If an error occurs while updating the information.
     */
    public String getTechnology() throws IOException { update(); return batteryTechnology; }
    
    /**
     * Updates the information stored in this class' variables.
     * @throws IOException If an error occurs while executing the ADB process.
     */
    private void update() throws IOException {
        String raw = adbController.executeCommand(device, true, true, "dumpsys", "battery");
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
                    switch (arr[1]) {
                        case "1":
                            batteryStatus = BatteryStatus.BATTERY_STATUS_UNKNOWN;
                            break;
                        case "2":
                            batteryStatus = BatteryStatus.BATTERY_STATUS_CHARGING;
                            break;
                        case "3":
                            batteryStatus = BatteryStatus.BATTERY_STATUS_DISCHARGING;
                            break;
                        case "4":
                            batteryStatus = BatteryStatus.BATTERY_STATUS_NOT_CHARGING;
                            break;
                        case "5":
                            batteryStatus = BatteryStatus.BATTERY_STATUS_FULL;
                            break;
                        default:
                            batteryStatus = BatteryStatus.BATTERY_STATUS_UNKNOWN;
                    }
                }
                if (line.contains("health: ")) {
                    String[] arr = line.split(": ");
                    switch (arr[1]) {
                        case "1":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_UNKOWN;
                            break;
                        case "2":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_GOOD;
                            break;
                        case "3":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_OVERHEAT;
                            break;
                        case "4":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_DEAD;
                            break;
                        case "5":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_OVER_VOLTAGE;
                            break;
                        case "6":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_UNSPECIFIED_FAILURE;
                            break;
                        case "7":
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_COLD;
                            break;
                        default:
                            batteryHealth = BatteryHealth.BATTERY_HEALTH_UNKOWN;
                    }
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
                if (line.contains("temperature: ")) {
                    String[] arr = line.split(": ");
                    batteryTemp = arr[1].trim();
                }
                if (line.contains("technology: ")) {
                    String[] arr = line.split(": ");
                    batteryTechnology = arr[1];
                }
            }
    }
}
