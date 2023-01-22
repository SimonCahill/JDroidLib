/*
 * Copyright (C) 2014 beatsleigher.
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

package JDroidLib.exceptions;

/**
 * Custom exception used in JDroidLib.
 * This exception is thrown, when a specified device could not be found by the ADB binary.
 * this is usually caused, when a faulty serial number was hard-coded, or when a device was unplugged before an operation begins.
 * @author beatsleigher
 * @since 11.03.2014
 */
public class DeviceNotFoundException extends Exception {
    
    public DeviceNotFoundException() {}
    
    public DeviceNotFoundException(String msg) { super(msg); }
    
}
