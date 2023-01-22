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

import JDroidLib.enums.RebootTo;
import JDroidLib.exceptions.*;
import JDroidLib.android.controllers.ADBController;
import JDroidLib.enums.*;

import java.io.*;

/**
 * This class allows you to easily grab build properties from the Android OS. Represents a device's build properties.
 * @author beatsleigher
 */
public class BuildProp {
    
    private final Device device;
    private String propFile = "";
    private final ADBController adbController;
    
    /**
     * Default constructor for @see BuildProp.
     * @param device The device this class should represent.
     * @param adbController The ADBController that makes this thing work.
     */
    BuildProp(Device device, ADBController adbController) {
        this.device = device;
        this.adbController = adbController;
    }
    
    /**
     * Gets build property from Android device.
     * @param prop the property to be found and returned.
     * @return the desired property, or "Not Found" if property wasn't found.
     * @throws IOException  if something went wrong.
     */
    @Deprecated
    public String getProp(String prop) throws IOException {
        String returnVal = "Not Found.";
        pullProp(System.getProperty("user.home") + "/.jdroidlib/tmp");
        
        BufferedReader reader = new BufferedReader(new FileReader(new File(propFile)));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains(prop)) {
                String[] found = line.split("=");
                returnVal = found[1];
                break;
            }
        }
        reader.close();
        
        return returnVal;
    }
    
    /**
     * Gets a list of all build properties in Android.
     * Uses system-own getProp-method.
     * Use this method instead of the deprecated one.
     * @return
     * @throws IOException 
     */
    public String getProp() throws IOException {
        String output = adbController.executeCommand(device, true, true, "getprop");
        String toReturn = "";
        BufferedReader reader = new BufferedReader(new StringReader(output));
        String line = "";
        while ((line = reader.readLine()) != null) {
            String s1 = line.trim();
            String s2 = s1.replaceAll("\\[", "");
            String s3 = s2.replace("]", "");
            String s4 = s3.replace(":", " = ");
            toReturn += s4 + "\n";
        }
        return toReturn;
    }
    
    /**
     * Pulls a list of properties from the device, then checks for the desired property.
     * @param key of the property (property name).
     * @return the desired property
     * @throws IOException 
     * @throws JDroidLib.exceptions.PropertyNotFoundException 
     */
    public String getSingleProperty(String key) throws IOException, PropertyNotFoundException {
        String returnString = "";
        String properties = getProp();
        BufferedReader reader = new BufferedReader(new StringReader(properties));
        String line = "";
        
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(key)) {
                String[] prop = line.split(" = ");
                return prop[1];
            }
        }
        
        throw new PropertyNotFoundException("Could not find property \"" + key + "\".");
    }
    
    /**
     * Attempts to set a build property in the Android system.
     * @param key to set value of.
     * @param value to set.
     * @param rebootAfter setting value.
     * @throws IOException if something went wrong.
     */
    public void setProp(String key, String value, boolean rebootAfter) throws IOException {
        adbController.executeCommand(device, true, true, "setprop", key, value);
        if (rebootAfter)
            device.reboot(DeviceState.DEVICE);
    }
    
    /**
     * Pulls the build.prop file from the Android device to a desired location on the hard drive.
     * @param dest on the hard drive, to pull the file to. ONLY USE DIRECTORIES THAT EXIST!
     * @throws IOException if something goes wrong.
     */
    public void pullProp(String dest) throws IOException {
        String[] commands = {"/system/build.prop", dest + "/build.prop"};
        adbController.executeCommand(device, false, false, "pull", commands);
        propFile = dest + "/build.prop";
    }
    
    /**
     * Pushes a (for example) modified build.prop file to Android device.
     * @param prop to be pushed to device.
     * @throws IOException if something went wrong.
     */
    public void pushProp(String prop) throws IOException {
        adbController.executeCommand(device, false, false, "push", prop, "/System/");
    }
    
}
