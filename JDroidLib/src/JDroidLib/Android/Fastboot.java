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
import java.io.*;
import JDroidLib.exceptions.*;

/**
 *
 * @author Simon
 */
public final class Fastboot {
    
    private final String mbDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
    private final String osName = System.getProperty("os.name");
    private String getFastboot() {
        String str = "";
        if (osName.contains("Windows")) {
            str = mbDir + "fastboot.exe";
        } else {
            str = mbDir + "fastboot";
        }
        return str;
    }
    private final Command cmd = new Command();
    
    public void executeFastbootCommandNoReturn(String command) throws InvalidCommandException, IOException{
        if (!command.equals("")) {
        cmd.executeProcessNoReturn(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an invalid command! Please enter a valid command.");
    }
    
    public String executeFastbootCommandReturnLastLine(String command) throws InvalidCommandException, IOException {
        if (!command.equals("")) {
            return cmd.executeProcessReturnLastLine(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an empty command! Please enter a valid command.");
    }
    
    public StringBuilder executeFastbootCommandReturnEntireOutput(String command) throws InvalidCommandException, IOException {
        if (!command.equals("")) {
            return cmd.executeProcessReturnAllOutput(getFastboot(), command);
        } else throw new InvalidCommandException("You have entered an empty command! Please enter a valid command.");
    }
}
