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

/**
 * Interface ICommand
 * Contains methods and variables required to execute commands on a connected device.
 * This base interface is implemented by {@link eu.beatsleigher.jdroidlib.android.AdbCommand} and {@link eu.beatsleigher.jdroidlib.android.FastbootCommand}.
 * @author Simon
 */
public interface ICommand {
    /**
     * Gets the command stored in this class.
     * @return A string object containing formatted commands and parameters.
     */
    String getCommand();
    
    /**
     * Sets the timeout for this command.
     * @param timeout The time to pass until command execution is aborted.
     * @return A subclass of {@link ICommand} with the new timeout.
     */
    ICommand withTimeout(int timeout);
    
    /**
     * Gets a ready to use {java.lang.ProcessBuilder} which is used by HAL to execute commands via the ADB server.
     * @return A ProcessBuilder ready for execution
     */
    ProcessBuilder getProcess();
}
