/*
 * Copyright (C) 2014 Beatsleigher.
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

package JDroidLib.util;

import JDroidLib.android.controllers.*;
import JDroidLib.android.device.*;
import java.util.*;

/**
 * Command class.
 * Represents an ADB, a fastboot or an anonymous command.
 * 
 * This class is used by the execution methods in @see CaptainKirk and contains all the information needed to execute a process.
 * 
 * This class is inheritable.
 * @author Beatsleigher
 */
@SuppressWarnings({"LocalVariableHidesMemberVariable", "TooBroadCatch", "BroadCatchBlock"})
public class Command {
    
    /**
     * Contains the different types of available parameters.
     * They all speak for themselves.
     */
    public enum CommandType { ADB_COMMAND, FASTBOOT_COMMAND, ANONYMOUS_COMMAND; }
    
    // Yes, on first glance they should be final. Trust me. I know what I'm doing.
    private CommandType commandType = null;
    private Device device = null;
    private String serial = null; // Only used for fastboot parameters.
    private List<String> parameters = null;
    private boolean returnOutput = false;
    private final boolean isAnonymous;
    private static ADBController adbController = null;
    private final boolean isADBCommand;
    
    /**
     * Creates a new instance of @see Command with the following parameters to be used in ADBController and CaptainKirk.
     * @param cmdType The type of command to be issued (ADB_COMMAND or FASTBOOT_COMMAND).
     * @param serial The serial number of the device to which the command is issued. 
     * If you're planning on issuing an ADB command, please create a device instance and pass the serial number.
     * Example: Device.getDevice("aSerialNumber", ADBController.getInstance()).getSerial();
     * @param params The command <b><u>and</u></b> the command's arguments.
     * @param returnOutput Set to true, if you want to process the process'  output automatically.
     * @return A new instance of @see Command with all the necessary parameters to execute an ADB command.
     */
    public static Command getCommand(CommandType cmdType, String serial, List<String> params, boolean returnOutput) {
        // Check ADBController
        if (adbController == null) try { adbController = ADBController.getInstance(); } catch (Exception ex) {} // No need for explicit handling here.
        
        switch (cmdType) {
            case ADB_COMMAND:
                return new Command(cmdType, Device.getDevice(serial, adbController), params, returnOutput);
            case FASTBOOT_COMMAND:
                return new Command(cmdType, serial, params, returnOutput);
            case ANONYMOUS_COMMAND:
                throw new IllegalArgumentException("Anonymous commands can only be created by Command.getAnonymousCommand()!");                
        }
        return null;
    }
    
    /**
     * Creates a new instance of @see Command with the following parameters to be used in ADBController and CaptainKirk.
     * @param cmdType The type of command to be issued (ANONYMOUS_COMMAND only!).
     * @param params The command <b><u>and</u></b> the command's arguments.
     * @param returnOutput Set to true, if you want to process the process'  output automatically.
     * @param isADBCommand Set to true, if this command is to be issued as an ADB command.
     * @return A new instance of @see Command with all the necessary parameters to execute an ADB command.
     */
    public static Command getAnonymousCommand(CommandType cmdType, List<String> params, boolean returnOutput, boolean isADBCommand) {
        return new Command(cmdType, params, returnOutput, isADBCommand);
    }
    
    /**
     * Converts an array or a set of @see String objects to a List(of String).
     * @param parameters An array or a set of @see String objects to convert.
     * @return A list (of String) to be used within this class.
     */
    public static List<String> convertArrayToList(String... parameters) {
        List<String> params = new ArrayList<>();
        
        for (String param : parameters)
            params.add(param);
        
        return params;
    }
    
    private Command(CommandType type, Device device, List<String> parameters, boolean returnOutput) {
        commandType = type;
        this.device = device;
        this.parameters = parameters;
        this.returnOutput = returnOutput;
        isAnonymous = false;
        isADBCommand = true;
    }
    
    private Command(CommandType type, String serial, List<String> parameters, boolean returnOutput) {
        this.commandType = type;
        this.serial = serial;
        this.parameters = parameters;
        this.returnOutput = returnOutput;
        isAnonymous = false;
        isADBCommand = false;
    }
    
    private Command(CommandType type, List<String> parameters, boolean returnOutout, boolean isADBCommand) {
        this.commandType = type;
        this.parameters = parameters;
        this.returnOutput = returnOutout;
        this.isAnonymous = true;
        this.isADBCommand = isADBCommand;
    }
    
    /**
     * The type of command to be executed.
     * @return Returns the command type.
     */
    public CommandType getCommandType() { return commandType; }
    
    /**
     * The device specific device to issue the commmand to.
     * @return Returns a @see Device object.
     */
    public Device getDevice() { return device; }
    
    /**
     * The serial of the device to issue the command to (fastboot parameters only!)
     * @return Returns a String object containing the serial number of the device to which to issue the command.
     */
    public String getSerial() { return serial; }
    
    /**
     * The parameters (including the first command/executable) to execute.
     * @return Returns a list of all the supplied parameters.
     */
    public List<String> getParameters() {
        List<String> parameters = new ArrayList<>();
        
        switch (commandType) {
            case ADB_COMMAND:
                parameters.add(CaptainKirk.getADB().getAbsolutePath());
                parameters.add("-s");
                parameters.add(device.getSerial());
            case FASTBOOT_COMMAND:
                parameters.add(CaptainKirk.getFastboot().getAbsolutePath());
                parameters.add("-s");
                parameters.add(serial);
            case ANONYMOUS_COMMAND:
                if (isADBCommand)
                    parameters.add(CaptainKirk.getADB().getAbsolutePath());
                else
                    parameters.add(CaptainKirk.getFastboot().getAbsolutePath());
        }
        
        for (String param : this.parameters)
            parameters.add(param);
        
        return parameters; 
    }
    
    /**
     * Determines whether the process' output will be returned or not.
     * @return Returns true if the process' output will be returned as a @see String object
     */
    public boolean returnsOutput() { return returnOutput; }
    
    /**
     * Determines whether the current command is anonymous.
     * An anonymous command is a command that is <u>not</u> issued to a specific device.
     * These commands come in handy when you need to issue server-wide commands, like starting or stopping the server.
     * @return 
     */
    public boolean isAnonymous() { return isAnonymous; }
    
    @Override
    public String toString() {
        if (commandType == CommandType.ADB_COMMAND) 
            return String.format(
                      "Command object. Command type: {0}\n"
                    + "Device info: {1}\n"
                    + "Command args: {2}\n"
                    + "Will printOutput: {3}", commandType.toString(), device.toString(), parameters.toArray().toString(), String.valueOf(returnOutput));
        else if (commandType == CommandType.FASTBOOT_COMMAND) 
            return String.format(
                      "Command object. Command type: {0}\n"
                    + "Device info: {1}\n"
                    + "Command args: {2}\n"
                    + "Will printOutput: {3}", commandType.toString(), serial, parameters.toArray().toString(), String.valueOf(returnOutput));
        else 
            return String.format(
                      "Command object. Command type: {0}\n"
                    + "Device info: {1}\n"
                    + "Command args: {2}\n"
                    + "Will printOutput: {3}", commandType.toString(), "n/a", parameters.toArray().toString(), String.valueOf(returnOutput));
    }
    
}
