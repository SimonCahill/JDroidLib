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
 * JDroidLib exception
 * This exception is thrown when a root function is executed and the selected device is not rooted.
 * @author Simon
 */
public class DeviceHasNoRootException extends Exception {
    
    /**
     * Default constructor.
     */
    public DeviceHasNoRootException() { super(); }
    
    /**
     * Default constructor.
     * @param msg The exception message.
     */
    public DeviceHasNoRootException(String msg) { super(msg); }
    
}
