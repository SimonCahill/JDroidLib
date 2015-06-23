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
package eu.beatsleigher.jdroidlib.android;

import eu.beatsleigher.jdroidlib.android.device.Device;
import eu.beatsleigher.jdroidlib.events.CommandExecutionCompletedEvent;
import eu.beatsleigher.jdroidlib.events.CommandExecutionCompletedEventListener;
import eu.beatsleigher.jdroidlib.events.CommandOutputChangedEventListener;
import eu.beatsleigher.jdroidlib.exception.DeviceHasNoRootException;
import eu.beatsleigher.jdroidlib.exception.InstallationFailedException;
import eu.beatsleigher.jdroidlib.util.HAL9000;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * AndroidController
 * JDroidLib's main class.
 * JDroidLib's AndroidController is the main class of this library, and the only class to be initialized by third-party software!
 * 
 * This class contains the methods to create and execute ADB commands, get (list) devices and do anything else with an Android device.
 * @author Simon
 */
public class AndroidController {
    
    //<editor-fold defaultstate="collapsed" desc="Static Members">
    private static AndroidController _instance;
    
    /**
     * Singleton method.
     * Creates a new instance of this class, if necessary.
     * @return An instance of this class.
     * @throws java.io.IOException This exception is thrown if an error occurs during installation of the ADB and fastboot binaries.
     * @throws java.lang.InterruptedException This exception is thrown if an error occurs during installation of the ADB and fastboot binaries.
     * @throws eu.beatsleigher.jdroidlib.exception.InstallationFailedException This exception is thrown if an error occurs during installation of the ADB and fastboot binaries.
     */
    public static AndroidController getInstance() throws IOException, InterruptedException, InstallationFailedException {
        return _instance == null ? _instance = new AndroidController(): _instance;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Instance Members">
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private HAL9000 _helloGentlemen;
    private final Object _lock = "I'm sorry, Dave. I'm afraid I can't do that. ";
    private List<CommandExecutionCompletedEventListener> executionCompleteEventListeners;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Ctor">
    /**
     * Creates a new instance of this class and initializes all required classes within the library, to function correctly.
     * @throws IOException
     * @throws InterruptedException
     * @throws InstallationFailedException 
     */
    private AndroidController() throws IOException, InterruptedException, InstallationFailedException {
        _helloGentlemen = HAL9000.getInstance();
        this.executionCompleteEventListeners = new ArrayList<>();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Command Stuffs">
    //<editor-fold defaultstate="collapsed" desc="Command Creation">
    /**
     * Creates an instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class with the given parameters.
     * @param command The command to execute (must not be adb!
     * @param params The parameters and flags for the command.
     * @return An instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class.
     */
    public AdbCommand formAdbCommandNoDevice(String command, String... params) {
        return new AdbCommand(command, params);
    }
    
    /**
     * Creates an instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class with the given parameters.
     * @param device The device on which to execute the command.
     * @param command The command to execute (must not be adb!)
     * @param params The parameters and flags for the command.
     * @return An instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class.
     */
    public AdbCommand formAdbCommand(Device device, String command, String... params) throws IllegalArgumentException {
        return new AdbCommand(device, command, params);
    }
    
    /**
     * Creates an instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class with the given parameters.
     * @param device The device on which to execute the command.
     * @param rootShell set to {@code true} if the shell should be running as root.
     * @param command The command to execute (must not be adb or shell!)
     * @param params The command parameters and/or flags
     * @return An instance of the {@link eu.beatsleigher.jdroidlib.android.AdbCommand} class.
     * @throws IllegalArgumentException This exception is thrown when a passed parameter contains invalid data.
     * @throws eu.beatsleigher.jdroidlib.exception.DeviceHasNoRootException This exception occurs when a device without root access is passed as a parameter when the shell is to run as root.
     */
    public AdbCommand formAdbShellCommand(Device device, boolean rootShell, String command, String... params) throws IllegalArgumentException, DeviceHasNoRootException {
        return new AdbCommand(device, true, rootShell, command, params);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Command Execution">
    /**
     * Executes a given {@link eu.beatsleigher.jdroidlib.android.AdbCommand} without returning anything.
     * @param command The {@link eu.beatsleigher.jdroidlib.android.AdbCommand} to execute.
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public void executeAdbCommandNoReturn(AdbCommand command) throws IOException, InterruptedException {
        synchronized (_lock) {
            _helloGentlemen.executeNoReturn(command);
        }
    }
    
    /**
     * Executes a given {@link eu.beatsleigher.jdroidlib.android.AdbCommand} and returns the exit value.
     * @param command The {@link eu.beatsleigher.jdroidlib.android.AdbCommand} to execute.
     * @return The exit value of the command {@code int}
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public int executeAdbCommandReturnExitValue(AdbCommand command) throws IOException, InterruptedException {
        synchronized (_lock) { return _helloGentlemen.executeReturnExitValue(command); }
    }
    
    /**
     * Executes a given {@link eu.beatsleigher.jdroidlib.android.AdbCommand} and returns its output.
     * @param command The {@link eu.beatsleigher.jdroidlib.android.AdbCommand} to execute.
     * @return The output of the command.
     * @throws IOException This exception is thrown if an IO error occurs and process execution cannot continue.
     * @throws InterruptedException This exception is thrown if an error occurs while waiting for the command to exit.
     */
    public String executeAdbCommandReturnOutput(AdbCommand command) throws IOException, InterruptedException {
        synchronized (_lock) { return _helloGentlemen.executeReturnOutput(command); }
    }
    
    /**
     * Executes an {@link eu.beatsleigher.jdroidlib.android.AdbCommand} asynchronously.
     * @param command
     * @throws IOException
     * @throws InterruptedException 
     */
    public void executeAdbCommandNoReturnAsync(AdbCommand command) throws IOException, InterruptedException {
        Future future = new FutureTask(() -> {
            synchronized (_lock) { executeAdbCommandNoReturn(command); }
            if (!executionCompleteEventListeners.isEmpty())
                executionCompleteEventListeners.stream().forEach((evt) -> {
                    evt.onCommandExecutionCompleted(new CommandExecutionCompletedEvent(this, command, null, -1));
            });
            return null;
        });
    };
    //</editor-fold>
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Event Stuff">
    /**
     * Adds a new event listener to this class.
     * @param evt The {@link CommandExectionCompletedEventListener} to add to this class.
     * @return A value indicating whether the addition was successful or not.
     */
    public boolean addCommandExecutionCompleteEventListener(CommandExecutionCompletedEventListener evt) {
        return executionCompleteEventListeners.add(evt);
    }
    
    /**
     * Adds a new event listener to this class.
     * @param evt The {@link CommandOutputChangedEventListener} to add to this class.
     * @return A value indicating whether the addition was successful or not.
     */
    public boolean addCommandOutputChangedEventListener(CommandOutputChangedEventListener evt) {
        return _helloGentlemen.addCommandOutputChangedEventListener(evt);
    }
    
    /**
     * Removes an event listener from this class.
     * @param evt The {@link eu.beatsleigher.jdroidlib.events.CommandExecutionCompletedEvent} to remove from this class.
     * @return A value indicating whether removal was successful or not.
     */
    public boolean removeCommandExecutionCompleteEventListener(CommandExecutionCompletedEventListener evt) {
        return executionCompleteEventListeners.remove(evt);
    }
    
    /**
     * Removes an event listener from this class.
     * @param evt The {@link eu.beatsleigher.jdroidlib.events.CommandOuputChangedEvent} to remove from this class.
     * @return A value indicating whether the removal was successful or not.
     */
    public boolean removeCommandOutputChangedEventListener(CommandOutputChangedEventListener evt) {
        return _helloGentlemen.removeCommandOutputChangedEventListener(evt);
    }
    //</editor-fold>
    //</editor-fold>
    
}
