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
import eu.beatsleigher.jdroidlib.exception.DeviceHasNoRootException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * AdbCommand Represents a command sent to the ADB executable. This command
 * cannot be instantiated by means of a constructor and must be gotten by the
 * {@link eu.beatsleigher.jdroidlib.android.AndroidController}.getAdbCommand or
 * the
 * {@link eu.beatsleigher.jdroidlib.android.AndroidController}.getAdbShellCommand
 * methods.
 *
 * @author Simon
 */
public class AdbCommand extends Command implements ICommand {

    /**
     * Constructor Constructs an instance of this class, without a
     * {@link eu.beatsleigher.jdroidlib.android.device.Device} object.
     *
     * @param command The command to pass to the ADB executable.
     * @param params The command parameters to pass to the ADB executable.
     */
    AdbCommand(String command, String... params) {
        super(command, params);
    }

    /**
     * Default (Recommended) Constructor Constructs an instance of this class,
     * with a {@link eu.beatsleigher.jdroidlib.android.device.Device} attribute.
     * This constructor constructs an instance of this class which can be used
     * to specifically target a connected device. Furthermore, this constructor
     * allows for ADB shell commands to be passed to and executed on the
     * selected device.
     *
     * @param device The device to pass the command to.
     * @param isShellCommand set to {@code true} if you want this command to be
     * executed as a shell command.
     * @param rootShell set to {@code true} if the shell command should be
     * executed as super user (will throw an exception if device has no root).
     * @param command The command to execute (cannot be "adb" or "-s"!)
     * @param params The parameters and/or flags to go with the command to be
     * executed.
     * @throws IllegalArgumentException
     * This constructor will throw an {@link java.lang.IllegalArgumentException} if:
     * <ul>
     * <li>
     * The passed {@link eu.beatsleigher.jdroidlib.android.device.Device} object is null
     * </li>
     * <li>
     * The passes command is null or empty
     * </li>
     * </ul>
     * @throws DeviceHasNoRootException
     * This constructor will throw a {@link eu.beatsleigher.jdroidlib.exception.DeviceHasNoRootException} if:
     * <ul>
     * <li>
     * The command is designated a rooted shell command and the device has no root access.
     * </li>
     * </ul>
     */
    AdbCommand(Device device, boolean isShellCommand, boolean rootShell, String command, String... params) throws IllegalArgumentException, DeviceHasNoRootException {
        super(device, isShellCommand, rootShell, command, params);
    }
    
    /**
     * Default (Recommended) Constructor Constructs an instance of this class,
     * with a {@link eu.beatsleigher.jdroidlib.android.device.Device} attribute.
     * This constructor constructs an instance of this class which can be used
     * to specifically target a connected device. Furthermore, this constructor
     * allows for ADB shell commands to be passed to and executed on the
     * selected device.
     *
     * @param device The device to pass the command to.
     * @param command The command to execute (cannot be "adb" or "-s"!)
     * @param params The parameters and/or flags to go with the command to be
     * executed.
     * @throws IllegalArgumentException
     * This constructor will throw an {@link java.lang.IllegalArgumentException} if:
     * <ul>
     * <li>
     * The passed {@link eu.beatsleigher.jdroidlib.android.device.Device} object is null
     * </li>
     * <li>
     * The passes command is null or empty
     * </li>
     * </ul>
     * @throws DeviceHasNoRootException
     * This constructor will throw a {@link eu.beatsleigher.jdroidlib.exception.DeviceHasNoRootException} if:
     * <ul>
     * <li>
     * The command is designated a rooted shell command and the device has no root access.
     * </li>
     * </ul>
     */
    AdbCommand(Device device, String command, String... params) throws IllegalArgumentException {
        super(device, command, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICommand withTimeout(int timeout, TimeUnit timeUnit) {
        this.timeout = timeout; this.timeoutTimeUnit = timeUnit; return this;
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public List<String> getCommands() {
        List<String> listOfCommands = new ArrayList<>();
        
        listOfCommands.add("adb");
        if (device != null) {
            listOfCommands.add("-s");
            listOfCommands.add(device.getSerial());
        }
        if (isShellCommand) {
            listOfCommands.add("shell");
            if (isShellRooted)
               listOfCommands.add("su");
        }
        listOfCommands.add(command);
        listOfCommands.addAll(params);
        
        return listOfCommands;
    }

    /**
     * {inheritDoc}
     * @return 
     */
    @Override
    public String getCommand() {
        StringBuilder sBuilder = new StringBuilder();
        
        sBuilder.append("adb").append(device == null ? "" : String.format(" -s %s", device.getSerial()));
        if (isShellCommand) {
            sBuilder.append(" shell");
            if (isShellRooted)
                sBuilder.append(" su");
        }
        sBuilder.append(command);
        params.stream().forEach((String _item) -> {
            sBuilder.append(String.format(" %s", _item));
        });
        
        return sBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessBuilder getProcess() {
        ProcessBuilder process = new ProcessBuilder();
        
        process.command(getCommands());
        process.directory(workingDir);
        process.redirectErrorStream(true);
        
        return process;
    }

}
