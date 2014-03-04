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

import JDroidLib.util.*;
import JDroidLib.exceptions.*;

import java.io.*;
import java.util.*;

/**
 *
 * @author beatsleigher
 */
public class FileSystem {
    
    private String serial = "";
    private CaptainKirk commander = null;
    
    /**
     * Basic constructor.
     * @param serial of the device.
     * @param commander the instance of CaptainKirk. Do NOT use this constructor! NEVER use any of these constructors!
     */
    public FileSystem(String serial, CaptainKirk commander) {
        this.serial = serial;
        this.commander = commander;
    }
    
    /**
     * Gets and returns a list of all the files and folders in the requested directory.
     * @param location to list files and folders from.
     * @return list of files and folders.
     * @throws IOException if something goes wrong.
     */
    public List<String> list(String location) throws IOException {
        List<String> listOfFiles = new ArrayList();
        String list = commander.executeADBCommand(true, false, serial, new String[]{"ls", location});
        BufferedReader reader = new BufferedReader(new StringReader(list));
        String line = "";
        
        while ((line = reader.readLine()) != null) {
            listOfFiles.add(line);
        }
        
        return listOfFiles;
    }
    
    public String delete(String file) throws IOException, DeleteFailedException {
        return commander.executeADBCommand(true, true, serial, new String[]{"rm", file});
    }
    
}
