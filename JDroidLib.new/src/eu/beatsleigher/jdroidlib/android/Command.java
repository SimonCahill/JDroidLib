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
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class command.
 * Base class containing abstract information.
 * @author Simon
 */
public abstract class Command implements ICommand {

    protected final String command;
    protected final List<String> params;
    protected final Device device;
    protected final boolean isShellCommand;
    protected final boolean isShellRooted;
    protected int timeout;
    protected File workingDir;
    
    /**
     * Constructor Constructs an instance of this class, without a
     * {@link eu.beatsleigher.jdroidlib.android.device.Device} object.
     *
     * @param command The command to pass to the ADB executable.
     * @param params The command parameters to pass to the ADB executable.
     */
    Command(String command, String... params) {
        this.command = command;
        this.params = Arrays.asList(params);
        this.device = null;
        this.isShellCommand = false;
        this.isShellRooted = false;
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
    Command(Device device, boolean isShellCommand, boolean rootShell, String command, String... params) throws IllegalArgumentException, DeviceHasNoRootException {
        if (device == null)
            throw new IllegalArgumentException("Device must not be null! To specify a non device-specific command, please use the NoDevice-method!");
        else this.device = device;
        this.isShellCommand = isShellCommand;
        if (!device.hasRoot() && rootShell) throw new DeviceHasNoRootException("Cannot execute root shell command on non-rooted device!"); else this.isShellRooted = rootShell;
        if (command == null || command.isEmpty()) throw new IllegalArgumentException("Command must not be null or empty!"); else this.command = command;
        this.params = Arrays.asList(params);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public abstract String getCommand();
    
    /**
     * Gets a {@link java.util.List} of String containing all the commands necessary to execute the command on the ADB server.
     * @return A list of String containing all necessary information to execute the command.
     */
    public abstract List<String> getCommands();

    /**
     * {@inheritDoc }
     */
    @Override
    public abstract ICommand withTimeout(int timeout);

    /**
     * {@inheritDoc }
     */
    @Override
    public abstract ProcessBuilder getProcess();
    
    /**
     * Sets the working directory for this command.
     * The working directory is the directory in which the executable will automatically output items, such as files or directories, if no specific location is passed.
     * The default working directory is:
     * Windows: %AppData%\com\beatsleigher\jdroidlib
     * Other systems: /home/user/jdroidlib
     * @param workPath The new working directory for the command.
     * @return Returns a instance of this class with updated information.
     */
    public ICommand withWorkingDir(File workPath) {
        this.workingDir = workPath; return this;
    }
    
}
