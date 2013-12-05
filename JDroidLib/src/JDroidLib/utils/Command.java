/*
 * Copyright (C) 2013 Simon.
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

package JDroidLib.utils;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public final class Command {
    
    BufferedReader prReader = null;
    ProcessBuilder process = null;
    Process pr = null;
    Date timeNow = new Date();
    String osName = System.getProperty("os.name");
    
    public void executeProcessNoReturn(String _process, String arg) throws IOException {
        process = new ProcessBuilder(_process, arg);
        pr = process.start();
    }
    
    public String executeProcessReturnLastLine(String _process, String arg) throws IOException {
        process = new ProcessBuilder(_process, arg);
        pr = process.start();
        prReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line;
        while ((line = prReader.readLine()) != null) {
            // Wait for input to end.
        }
        return line;
    }
    
    public StringBuilder executeProcessReturnAllOutput(String _process, String arg) throws IOException {
        process = new ProcessBuilder(_process, arg);
        pr = process.start();
        prReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        StringBuilder output = null;
        String line;
        while ((line = prReader.readLine()) != null) {
            output.append(line);
        }
        return output;
    }
    
    public boolean isProcessRunning(String processName) throws IOException {
        boolean value = false;
        if (osName.equals("Linux") | osName.contains("Mac")) {
            process = new ProcessBuilder("ps", "-e");
            pr = process.start();
            String line;
            prReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            while ((line = prReader.readLine()) != null) {
                if (line.contains(processName)) { value = true; break; }
            }
        } else {
            String winDir = System.getenv("windir") + "/System32/tasklist.exe";
            process = new ProcessBuilder(winDir);
            pr = process.start();
            String line;
            prReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            while ((line = prReader.readLine()) != null) {
                if (line.contains(processName)) { value = true; break; }
            }
        }
        
        return value;
        
    }
    
    public String executeProcessReturnError(String processName, String arg) throws IOException {
        process = new ProcessBuilder(processName, arg);
        process.redirectError();
        pr = process.start();
        prReader = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
        String line;
        String output = "";
        while ((line = prReader.readLine()) != null) {
            output += line + "\n";
        }
        return output;
    }
}
