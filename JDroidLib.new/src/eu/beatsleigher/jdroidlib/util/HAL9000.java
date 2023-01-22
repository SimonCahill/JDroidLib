/*
 * Copyright (C) 2015 Simon.
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
package eu.beatsleigher.jdroidlib.util;

import eu.beatsleigher.jdroidlib.android.Command;
import eu.beatsleigher.jdroidlib.events.*;
import eu.beatsleigher.jdroidlib.exception.InstallationFailedException;
import java.io.*;
import java.util.*;

/**
 * Good afternoon, gentlemen. I am a HAL 9000 computer.
 * @author Simon
 */
public class HAL9000 {
    
    //<editor-fold defaultstate="collapsed" desc="Static Members">
    private static HAL9000 instance;
    
    /**
     * Gets an instance of this class.
     * If no instance is present, a new instance is created.
     * @return A new instance of this class if none is present, the instance of this class if present.
     * @throws IOException if an error occurs during installation.
     * @throws InterruptedException if an error occurs during installation.
     * @throws InstallationFailedException if files were not installed (correctly).
     */
    public static HAL9000 getInstance() throws IOException, InterruptedException, InstallationFailedException {
       return instance == null ? instance = new HAL9000() : instance; 
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Instance Members">
    //<editor-fold defaultstate="collapsed" desc="Variables">
    List<CommandOutputChangedEventListener> commandOutputChangedEventHandlers = new ArrayList<>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Ctor" >
    /**
     * Creates a new instance of this class.
     * Initializes JDroidLib.
     * @author Simon
     * @throws IOException if an error occurs during installation.
     * @throws InterruptedException if an error occurs during installation.
     * @throws InstallationFailedException if files were not installed (correctly).
     */
    private HAL9000() throws IOException, InterruptedException, InstallationFailedException {
        System.out.println(String.format("Installing ADB... %s", ResourceManager.Installer.installBinaries()));
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter Methods">
    /**
     * Gets and returns a {@link java.io.File} object representing the ADB executable installed to this system.
     * @return The path to the ADB executable.
     */
    public File getAdb() { return ResourceManager.Installer.getAdbExe(); }
    
    /**
     * Gets and returns a {@link java.io.File} object representing the fastboot executable installed to this system.
     * @return The path to the fastboot executable.
     */
    public File getFastboot() { return ResourceManager.Installer.getFastbootExe(); }
    
    /**
     * Gets and returns a {@link java.io.File} object representing the DDMS executable installed to this system.
     * @return The path to the DDMS executable.
     */
    public File getDdms() { return ResourceManager.Installer.getDdmsJar(); }
    
    /**
     * Gets the root path for JDroidLib.
     * @return A {@link java.io.File} object pointing to JDroidLib's dir on the filesystem.
     */
    public File getJDroidLibPath() { return ResourceManager.Installer.getInstallPath().getParentFile(); }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Process Execution">
    /**
     * Executes a passed command without returning anything.
     * This method runs synchronously and the calling method locks it to a thread.
     * @param command The command to execute.
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public void executeNoReturn(Command command) throws IOException, InterruptedException {
        Process pr = command.getProcess().start();
        pr.waitFor(command.getTimeout(), command.getTimeUnits());
    }
    
    /**
     * Executed a passed command and returns the exit value of the process.
     * @param command The command to execute.
     * @return The return value of the executed command.
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public int executeReturnExitValue(Command command) throws IOException, InterruptedException {
        Process pr = command.getProcess().start();
        pr.waitFor(command.getTimeout(), command.getTimeUnits());
        return pr.exitValue();
    }
    
    /**
     * Executes a passed command and returns the output of the process.
     * @param command The command to execute.
     * @return The output of the executed process.
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public String executeReturnOutput(Command command) throws IOException, InterruptedException {
        StringBuilder sBuilder = new StringBuilder();
        Process pr = command.getProcess().start();
        BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String inLine;
        
        while (inStreamReader.read() != -1) {
            inLine = inStreamReader.readLine();
            if (inLine.isEmpty()) continue;
            
            sBuilder.append(inLine);
            if (!commandOutputChangedEventHandlers.isEmpty())
                for (CommandOutputChangedEventListener handler : commandOutputChangedEventHandlers)
                    handler.onCommandOutputChangedEventListener(
                            new eu.beatsleigher.jdroidlib.events.CommandOuputChangedEvent(this, command, inLine, sBuilder.toString())); // Don't change! It works!
        }
        
        return sBuilder.toString();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Event Lists">
    /**
     * Adds a new {@link eu.beatsleigher.jdroidlib.events.CommandOuputChangedEventListener} to the list of listeners in this class.
     * @param listener The listener to add.
     * @return A value indicating whether the addition was successful or not.
     */
    public boolean addCommandOutputChangedEventListener(CommandOutputChangedEventListener listener) {
        return commandOutputChangedEventHandlers.add(listener);
    }
    
    /**
     * Removes a {@link eu.beatsleigher.jdroidlib.events.CommandOutputChangedEventListener} from the list of listeners in this class.
     * @param listener The listener to remove.
     * @return A value indicating whether removal was successful or not.
     */
    public boolean removeCommandOutputChangedEventListener(CommandOutputChangedEventListener listener) {
        return commandOutputChangedEventHandlers.remove(listener);
    }
    //</editor-fold>
    //</editor-fold>
    
}
