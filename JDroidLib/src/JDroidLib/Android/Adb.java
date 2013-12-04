/*
 * Copyright (C) 2013 Simon.
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

package JDroidLib.Android;

import JDroidLib.Android.*;

/**
 *
 * @author Simon
 */
public final class Adb {
    private static Object lock = "Locked";
    private final String adb = "adb";
    private final String adb_exe() {
        String osName = System.getProperty("os.name");
        String mbDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
        if (osName.contains("Windows")) {
            return mbDir + "adb.exe";
        } else {
            return mbDir + "adb";
        }
    }
    
    public static AdbCmd formAdbCmd(String cmd, Object[] args) {
        String adbCommand = (args.length > 0) ? cmd + " " : cmd;
        
        for (int i = 0; i < args.length; i++) {
            adbCommand += args[i] + " ";
        }
        
        return new AdbCmd(adbCommand);
    }
}
