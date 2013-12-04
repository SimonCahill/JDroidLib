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

import JDroidLib.utils.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public final class Adb {
    
    Command cmd = new Command();
    
    private String getAdb() {
        String osName = System.getProperty("os.name");
        String bin = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
        String _adb = "";
        if (osName.contains("Windows")) _adb = bin + "adb.exe";
        if (osName.equals("Linux")) _adb = bin + "adb";
        if (osName.contains("Mac")) _adb = bin + "adb";
        return _adb;
    }
    
    public void startServer() {
        try {
            cmd.executeProcessNoReturn(getAdb(), "start-server");
        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void killServer() {
        try {
            cmd.executeProcessNoReturn(getAdb(), "kill-server");
        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
