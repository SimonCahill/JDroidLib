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

import JDroidLib.android.controllers.*;
import JDroidLib.exceptions.*;

import java.io.*;
import java.util.*;

/**
 *
 * @author beatsleigher
 */
public class FileSystem {
    
    private final Device device;
    private final ADBController adbController;
    
    /**
     * Basic constructor.
     * @param device the device for which to represent the file system
     * @param adbController an instance of ADBController. ALWAYS USE THE FIRST OBJECT CREATED!
     */
    public FileSystem(Device device, ADBController adbController) {
        this.device = device;
        this.adbController = adbController;
    }
    
    /**
     * Gets and returns a list of all the files and folders in the requested directory.
     * @param location to list files and folders from.
     * @param useBusybox Use Busybox for advanced listing (recommended)
     * @return list of files and folders.
     * @throws IOException if something goes wrong.
     */
    public List<String> list(String location, boolean useBusybox) throws IOException {
        List<String> listOfFiles = new ArrayList();
        List<String> args = new ArrayList<>();
       
        adbController.rootServer();
        
        if (useBusybox && device.getBusybox().isInstalled()) {
            String output = null;
            String line = null;
            args.add("busybox");
            args.add("ls");
            args.add("-a");
            args.add("-p");
            args.add("-1");
            args.add(location);
            
            output = adbController.executeADBCommand(true, true, device, args);
            
            BufferedReader reader = new BufferedReader(new StringReader(output));
            while ((line = reader.readLine()) != null) {
                if (!line.equals("") || !line.equals("./"))
                    listOfFiles.add(line);
            }
            reader.close();
        } else {
            String output = null;
            String line = null;
            args.add("ls");
            args.add("-a");
            args.add(location);
            
            output = adbController.executeADBCommand(true, true, device, args);
            
            BufferedReader reader = new BufferedReader(new StringReader(output));
            while ((line = reader.readLine()) != null) {
                listOfFiles.add(line);
            }
            reader.close();
        }
        
        return listOfFiles;
    }
    
    /**
     * Deletes a file from the {@see device}.
     * @param file The file or folder to delete.
     * @return ADB's output
     * @throws IOException If something goes wrong.
     * @throws DeleteFailedException Obsolete.
     */
    public String delete(String file) throws IOException, DeleteFailedException {
        return adbController.executeADBCommand(true, true, device, new String[]{"rm", file});
    }
    
    /**
     * Creates a new directory in the specified location.
     * @param location The location of the folder. (Where it should be created).
     * @param name The name of the folder.
     * @return ADB's output
     * @throws IOException If something goes wrong.
     */
    public String mkDir(String location, String name) throws IOException {
        
        if (location == null || location.equals(""))
            throw new NullPointerException("No location was specified!");
        if (name == null || name.equals(""))
            throw new NullPointerException("No filename was specified!");
        
        if (!location.startsWith("/")) {
            String newLoc = "/" + location;
            location = newLoc;
        }
                    
        if (!location.endsWith("/") && !name.startsWith("/"))
            location += "/"; // Check if the path is correct
        
        String returnVal = null;
        String adbOutput = null;
        
        adbController.rootServer();
        
        try {
        adbOutput = adbController.executeADBCommand(true, true, device, new String[]{"mkdir", location + name});
        } finally {
            if (!adbOutput.equals(""))
                returnVal = adbOutput;
            else
                returnVal = "No output";
        }
        
        return returnVal;
    }
    
    /**
     * Creates a new file on the device's file system.
     * @param location The location of the new file.
     * @param name The name of the new file.
     * @return ADB's output
     * @throws IOException If something goes wrong.
     */
    public String touch(String location, String name) throws IOException {
        
        if (location == null || location.equals(""))
            throw new NullPointerException("No location was specified!");
        if (name == null || name.equals(""))
            throw new NullPointerException("No filename was specified!");
        
        if (!location.startsWith("/")) {
            String newLoc = "/" + location;
            location = newLoc;
        }
        if (!location.endsWith("/") && !name.startsWith("/"))
            location += "/";
        
        String returnVal = null;
        String adbOutput = null;
        
        adbController.rootServer();
        
        try {
            adbOutput = adbController.executeADBCommand(true, true, device, new String[]{"touch", location + name});
        } finally {
            if (!adbOutput.equals(""))
                returnVal = adbOutput;
            else
                returnVal = "No output";
        }
        
        return returnVal;
        
    }
    
    /**
     * Pulls a file from the device to the local hard drive.
     * @param source The file to be pull from the device.
     * @param dest The destination folder.
     * @return ADB'S output.
     * @throws IOException 
     */
    public String pull(String source, File dest) throws IOException {
        String returnVal = null;
        String adbOutput = null;
        
        if (source == null || source.equals(""))
            throw new NullPointerException("Source must not be null!");
        if (dest == null || dest.getName().equals(""))
            throw new NullPointerException("Destination must not be null!");
        if (!dest.isDirectory())
            throw new IllegalStateException("Destination must be a folder!");
                
        adbController.rootServer();
        
        try {
            adbOutput = adbController.executeADBCommand(false, true, device, new String[]{"pull", source, dest.getAbsolutePath()});
        } finally {
            if (adbOutput != null || !adbOutput.equals(""))
                returnVal = adbOutput;
            else
                returnVal = "No output";
        }
        
        return returnVal;
    }
    
    /**
     * Pushes a file to the device's local file system.
     * @param source The source file/folder to send to device.
     * @param dest The destination folder on the device.
     * @return ADB's output.
     * @throws IOException If something goes wrong.
     */
    public String push(File source, String dest) throws IOException {
        String returnVal = null;
        String adbOutput = null;
        
        if (source == null || source.getName().equals(""))
            throw new NullPointerException("Source must not be null!");
        if (dest == null || dest.equals(""))
            throw new NullPointerException("Destination must not be null!");
                
        adbController.rootServer();
        
        try {
            adbOutput = adbController.executeADBCommand(false, true, device, new String[]{"pull", source.getAbsolutePath(), dest});
        } finally {
            if (adbOutput != null || !adbOutput.equals(""))
                returnVal = adbOutput;
            else
                returnVal = "No output";
        }
        
        return returnVal;
    }
    
}
