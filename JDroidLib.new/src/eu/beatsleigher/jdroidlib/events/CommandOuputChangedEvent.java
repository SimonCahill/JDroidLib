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
 * 
 * The CommandOutputChanged event is called when an active command, called with {@link eu.beatsleigher.jdroidlib.util.HAL9000}.executeReturnOutput, outputs new information.
 * This class can be used to begin processing the information before the entire output is available.
 * 
 * The data in this class provides information, which cannot be obtained by simply executing methods, such as the current output.
 * @author Simon
 */
public final class CommandOuputChangedEvent extends EventObject {
    
    /** The command that is currently being executed by the method calling this event. */
    private final Command executingCommand;
    /** The current output of the command. */
    private final String currentOutput;
    /** The previous output of the command. */
    private final String previousOutput;

    /**
     * Default constructor.
     * @param source The calling object.
     * @param executingCommand The command that is currently being executed by the method calling this event.
     * @param currentOutput The current output of the process.
     * @param previousOutput The entire output of the command that has been piped up until this event was called. 
     */
    public CommandOuputChangedEvent(Object source, Command executingCommand, String currentOutput, String previousOutput) {
        super(source);
        this.executingCommand = executingCommand;
        this.currentOutput = currentOutput;
        this.previousOutput = previousOutput;
    }
    
    /**
     * Gets the command that is currently being executed by the method calling this event.
     * @return A {@link eu.beatsleigher.jdroidlib.android.Command} instance. (Can be either FastbootCommand or AdbCommand).
     */
    public Command getExecutingCommand() { return executingCommand; }
    
    /**
     * Gets the latest output (from the time of this event being called) from the command being executed.
     * @return A string value containing the latest output of the process.
     */
    public String getCurrentOutput() { return currentOutput; }
    
    /**
     * Gets the previous output of the command.
     * @return A string value containing all of the command's output up until the calling of this event.
     */
    public String getPreviousOutput() { return previousOutput; }
    
}
