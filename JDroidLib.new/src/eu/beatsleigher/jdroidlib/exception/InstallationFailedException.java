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
package eu.beatsleigher.jdroidlib.exception;

/**
 * InstallationFailedException
 * This exception is thrown when the installation of files within this library has failed.
 * @author Simon
 */
public class InstallationFailedException extends Exception {
    
    /**
     * Default constructor.
     */
    public InstallationFailedException() { super(); }
    
    /**
     * Constructor with message parameter.
     * @param message The error message belonging to the exception.
     */
    public InstallationFailedException(String message) { super(message); }
    
}
