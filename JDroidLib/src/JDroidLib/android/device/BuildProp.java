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

import JDroidLib.util.CaptainKirk;

import java.io.*;

/**
 * This class allows you to easily grab build properties from the Android OS.
 * @author beatsleigher
 */
public class BuildProp {
    
    String serial = "";
    String propFile = "";
    CaptainKirk commander = null;
    
    public BuildProp(String serial) {
        this.serial = serial;
        commander = new CaptainKirk();
    }
    
    /**
     * Gets build property from Android device.
     * @param prop the property to be found and returned.
     * @return the desired property, or "Not Found" if property wasn't found.
     * @throws IOException  if something went wrong.
     */
    public String getProp(String prop) throws IOException {
        String returnVal = "Not Found.";
        pullProp(System.getProperty("user.home") + "/.jdroidlib/tmp");
        
        BufferedReader reader = new BufferedReader(new FileReader(new File(propFile)));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains(prop)) {
                String[] found = line.split("\\=");
                returnVal = found[1];
                break;
            }
        }
        reader.close();
        
        return returnVal;
    }
    
    /**
     * Pulls the build.prop file from the Android device to a desired location on the hard drive.
     * @param dest on the hard drive, to pull the file to. ONLY USE DIRECTORIES THAT EXIST!
     * @throws IOException if something goes wrong.
     */
    public void pullProp(String dest) throws IOException {
        String[] commands = {"pull", "/system/build.prop", dest + "/build.prop"};
        String str = commander.executeADBCommand(false, false, serial, commands);
        propFile = dest + "/build.prop";
    }
    
    /**
     * Pushes a (for example) modified build.prop file to Android device.
     * @param prop to be pushed to device.
     * @throws IOException if something went wrong.
     */
    public void pushProp(String prop) throws IOException {
        String[] commands = {"push", prop, "/System/"};
        commander.executeADBCommand(false, true, serial, commands);
    }
    
}
