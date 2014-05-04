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


import JDroidLib.android.device.*;

import java.io.*;
import java.util.*;


/**
 *
 * @author beatsleigher
 */
public class PackageController {
    
    private Device device = null;
    private ADBController adbController = null;
    
    public PackageController(ADBController adbController, Device device) {
        this.adbController = adbController;
        this.device = device;
    }
    
    /**
     * Lists all permission groups from the device associated with this object.
     * @return A list of permissions (Only permission names) derived from the device associated with this object.
     * @throws IOException If something goes wrong.
     */
    public List<String> listPermissionGroups() throws IOException {
        List<String> permGroups = new ArrayList<>();
        
        String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "list", "permission-groups"});
        String[] parsed0 = output.split("\n");
        for (String str : parsed0) {
            if (str.toLowerCase().startsWith("permission")) {
                String[] parsed1 = str.split("-group.");
                permGroups.add(parsed1[1]);
            }
        }
        
        return permGroups;
    }
    
    /**
     * Lists all permissions from the group provided in the parameters.
     * @param group The group from which to receive the permissions from.
     * @return A list of permissions derived from the provided group.
     * @throws IOException If something goes wrong.
     */
    public List<String> getGroupPermissions(String group) throws IOException {
        List<String> perms = new ArrayList<>();
        if (!(group == null || group.equals(""))) {

            String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "list", "permissions", });
            String[] parsed0 = output.split("\n");
            for (String str : parsed0) {
                if (!str.toLowerCase().startsWith("all permissions") || str.equals("")) {
                    String[] parsed1 = str.split("permission.");
                    perms.add(parsed1[1]);
                }
            }
            
            return perms;
        } else return null;
    }
    
    /**
     * Lists all test packages available on the device.
     * @param apk
     * @param testPackage
     * @return
     * @throws IOException 
     */
    public List<String> getInstrumentation(String apk, String testPackage) throws IOException {
        List<String> instrumentation = new ArrayList<>();
        
        String[] args = new String[10];
        args[0] = "pm";
        args[1] = "list";
        args[2] = "instrumentation";
        if (!apk.equals("") || apk != null) {
            args[3] = "-f";
            args[4] = apk;
            if (!(testPackage.equals("") || testPackage == null)) 
                args[5] = testPackage;
        } else {
            if (!(testPackage.equals("") || testPackage == null)) 
                args[3] = testPackage;
        }
        
        String output = adbController.executeADBCommand(true, false, device.toString(), args);
        if (!output.equals("")) {
            String[] parsed0 = output.split("\n");

            for (String str : parsed0) {
                instrumentation.add(str);
            }
            
            return instrumentation;
        }
        
        return null;
    }
    
    /**
     * Lists all the device's features.
     * @return Returns the features of the device as a List of String.
     * @throws IOException If something goes wrong.
     */
    public List<String> getFeatures() throws IOException {
        List<String> features = new ArrayList<>();
        
        String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "list", "features"});
        String[] parsed0 = output.split("\n");
        
        for (String str : parsed0) {
            String[] parsed1 = str.split(":");
            features.add(parsed1[1]);
        }
        
        return features;
    }
    
    /**
     * Lists all available libs (libraries) on the current device.
     * @return A List of String containing all libraries.
     * @throws IOException If something goes wrong.
     */
    public List<String> getLibs() throws IOException {
        List<String> libs = new ArrayList<>();
        
        String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "list", "libraries"});
        String[] parsed0 = output.split("\n");
        
        for (String str : parsed0) {
            String[] parsed1 = str.split(":");
            libs.add(parsed1[1]);
        }
        
        return libs;
    }
    
    /**
     * Lists all the users on the device.
     * @return The list of users (unformatted).
     * @throws IOException If something goes wrong.
     */
    public List<String> getUsers() throws IOException {
        List<String> users = new ArrayList<>();
        
        String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "list", "users"});
        String[] parsed0 = output.split("\n");
        
        for (String str : parsed0) {
            users.add(str);
        }
        
        return users;
    }
    
    /**
     * Gets and returns path of specified APK.
     * @param apk
     * @return
     * @throws IOException 
     */
    public String getPathOfAPK(String apk) throws IOException {
        return adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "path", apk});
    }
    
    /**
     * Gets and returns a dump of the specified application.
     * @param apk
     * @return
     * @throws IOException 
     */
    public String getAPKDump(String apk) throws IOException {
        return adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "dump", apk});
    }
    
    /**
     * Installs an application to the device provided {@see PackageController}.
     * @param forwardLock Installs the package with a FORWARD_LOCK
     * @param reinstall Reinstalls the application, keeping the old application's data.
     * @param allowTestApps Allows test applications to be installed.
     * @param APK The application to install.
     * @param installToSDCard Installs the application to the device's SD card if available. (If this is set to true, {@see installToInternalFlash} must be set to false!
     * @param installToInternalFlash Installs the application to the device's internal flash memory.
     * @param allowDowngrade Allows application version downgrading.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String installApplication
        (boolean forwardLock, boolean reinstall, boolean allowTestApps, File APK, boolean installToSDCard, boolean installToInternalFlash, boolean allowDowngrade) throws IOException {
            List<String> args = new ArrayList<>();
            args.add("pm");
            args.add("install");
            if (forwardLock)
                args.add("-l");
            if (reinstall)
                args.add("-r");
            if (allowTestApps)
                args.add("-t");
            args.add("-i");
            args.add(APK.getAbsolutePath());
            if (installToSDCard)
                args.add("-s");
            if (installToInternalFlash)
                args.add("-f");
            if (allowDowngrade)
                args.add("-d");
            String[] _args = new String[10];
            for (int i = 0; i < args.size(); i++)
                _args[i] = args.get(i);
                
            return adbController.executeADBCommand(true, false, device.toString(), _args);
        }
        
    /**
     * Uninstalls an application from the provided device.
     * @param keepData If <i>true</i>, all the application's data will be kept.
     * @param _package The application to remove.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String uninstallApplication(boolean keepData, String _package) throws IOException {
        String[] args = new String[10];
        args[0] = "pm";
        args[1] = "uninstall";
        if (keepData) {
            args[2] = "-k";
            args[3] = _package;
        } else {
            args[2] = _package;
        }
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Deletes all data associated with a package.
     * @param userID (Optional) The user for which to perform this task.
     * @param _package The package from which to delete the data.
     * @return ADB's output
     * @throws IOException If something goes wrong.
     */
    public String clearData(String userID, String _package) throws IOException {
        String[] args = new String[10];
        args[0] = "pm";
        args[1] = "clear";
        if (!userID.equals("") || userID != null) {
            args[2] = "--user";
            args[3] = userID;
            args[4] = _package;
        } else {
            args[2] = _package;
        }
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Enables a component or package for a specific user.
     * @param userID (Optional) The user for which to enable this component.
     * @param component The component to enable.
     * @return ADB's output
     * @throws IOException If something goes wrong.
     */
    public String enableComponent(String userID, String component) throws IOException {
        String[] args = new String[10];
        args[0] = "pm";
        args[1] = "enable";
        if (!(userID.equals("") || userID == null)) {
            args[2] = "--user";
            args[3] = userID;
            args[4] = component;
        } else {
            args[2] = component;
        }
        
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Disables a component or package for a specific user.
     * @param userID (Optional) The user for which to disable the component.
     * @param component The component to be disabled.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String disableComponent(String userID, String component) throws IOException {
        String[] args = new String[10];
        args[0] = "pm";
        args[1] = "disable";
        if (!(userID.equals("") || userID == null)) {
            args[2] = "--user";
            args[3] = userID;
            args[4] = component;
        } else {
            args[2] = component;
        }
        
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Grants a specified permission to a package.
     * @param _package The package to be modified.
     * @param permission The permission to apply to @param _package.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String grantPermission(String _package, String permission) throws IOException{
        if ((_package.equals("") || _package == null) || (permission.equals("") || permission == null))
            return null;
        
        String[] args = {"pm", "grant", _package, permission};
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Revokes a specified permission from a package.
     * @param _package The package to modify.
     * @param permission The permission to remove.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String revokePermission(String _package, String permission) throws IOException {
        if ((_package.equals("") || _package == null) || (permission.equals("") || permission == null))
            return null;
        
        String[] args = {"pm", "revoke", _package, permission};
        return adbController.executeADBCommand(true, false, device.toString(), args);
    }
    
    /**
     * Creates a new user on the device.
     * @param userName The desired name for the new user.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String createUser(String userName) throws IOException {
        if (userName.equals("") || userName == null)
            return null;
        
        return adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "create-user", userName});
    }
    
    /**
     * Removes a user from the device.
     * @param userID The user's ID.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String removeUser(String userID) throws IOException {
        if (userID.equals("") || userID == null)
            return null;
        
        return adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "remove-user", userID});
    }
    
    /**
     * Gets the maximum amount of users allowed on the device.
     * @return The maximum amount of users.
     * @throws IOException If something goes wrong.
     */
    public long getMaxUserCount() throws IOException {
        String output = adbController.executeADBCommand(true, false, device.toString(), new String[]{"pm", "get-max-users"});
        String[] maxAmount = output.split(": ");
        return Long.valueOf(maxAmount[1]);
    }
    
}
