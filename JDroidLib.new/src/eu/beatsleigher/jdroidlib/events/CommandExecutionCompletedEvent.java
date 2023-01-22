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
package eu.beatsleigher.jdroidlib.events;

import eu.beatsleigher.jdroidlib.android.Command;
import java.util.EventObject;

/**
 * Event class
 * Contains data concerning the command associated with this event instance.
 * @author Simon
 */
public class CommandExecutionCompletedEvent extends EventObject {
    
    /** The command associated with this event */
    private final Command command;
    /** The output of the executed command. */
    private final String commandOutput;
    /** The command's exit value */
    private final int commandExitValue;
    
    /**
     * Default constructor.
     * @param source The source of the event (the sender)
     * @param command The command associated with this event.
     * @param cmdOutput The complete output of the command.
     * @param cmdExitVal The exit value of the command.
     */
    public CommandExecutionCompletedEvent(Object source, Command command, String cmdOutput, int cmdExitVal) {
        super(source);
        this.command = command;
        this.commandOutput = cmdOutput;
        this.commandExitValue = cmdExitVal;
    }
    
    /**
     * Gets the command associated with this event.
     * @return {@link eu.beatsleigher.jdroidlib.android.Command}
     */
    public Command getCommand() { return command; }
    
    /**
     * Gets the output of the command that was executed.
     * @return The command's output.
     */
    public String getCommandOutput() { return commandOutput; }
    
    /**
     * Gets the exit value of the command.
     * @return {@code int}
     */
    public int getCommandExitValue() { return commandExitValue; }
    
}
