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
       
        if (useBusybox && device.getBusybox().isInstalled()) {
            String output = null;
            String line = null;
            args.add("busybox");
            args.add("ls");
            args.add("-a");
            args.add("-p");
            args.add("-1");
            
            output = adbController.executeADBCommand(true, true, device, args);
            
            BufferedReader reader = new BufferedReader(new StringReader(output));
            while ((line = reader.readLine()) != null) {
                listOfFiles.add(line);
            }
            reader.close();
        } else {
            String output = null;
            String line = null;
            args.add("ls");
            args.add("-a");
            
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
    
}
