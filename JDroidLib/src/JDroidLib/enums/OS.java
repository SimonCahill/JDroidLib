/*
 * Copyright (C) 2014 beatsleigher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package JDroidLib.enums;

import JDroidLib.exceptions.OSNotSupportedException;

/**
 * The operating systems supported by JDroidLib.
 * @author beatsleigher
 */
public enum OS {
    WINDOWS, LINUX, MAC_OS;
    
    /**
     * Attempts to return a type of @see OS via a value.
     * @param val The OS to return
     * @return A type of @see OS
     * @throws OSNotSupportedException If no type of @see OS could be determined. 
     */
    public static OS getOS(String val) throws OSNotSupportedException {
        for (OS os : OS.values())
            if (val.contains(os.toString()))
                return os;
        throw new OSNotSupportedException("OS \"" + val + "\" is not supported by JDroidLib!");
    }
    
}
