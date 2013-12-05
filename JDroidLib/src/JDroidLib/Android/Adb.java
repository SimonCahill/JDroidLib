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
import JDroidLib.exceptions.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;

/**
 *
 * @author Simon
 */
public final class Adb {

    Command cmd = new Command();

    /**
     * 
     * @return 
     */
    private String getAdb() {
        String osName = System.getProperty("os.name");
        String bin = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
        String _adb = "";
        if (osName.contains("Windows")) {
            _adb = bin + "adb.exe";
        }
        if (osName.equals("Linux")) {
            _adb = bin + "adb";
        }
        if (osName.contains("Mac")) {
            _adb = bin + "adb";
        }
        return _adb;
    }

    /**
     * 
     */
    public void startServer() {
        try {
            cmd.executeProcessNoReturn(getAdb(), "start-server");
        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     */
    public void killServer() {
        try {
            cmd.executeProcessNoReturn(getAdb(), "kill-server");
        } catch (IOException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     */
    public void restartServer() {
        try {
            cmd.executeProcessNoReturn(getAdb(), "kill-server");
            Thread.sleep(2000);
            cmd.executeProcessNoReturn(getAdb(), "start-server");
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Adb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param command
     * @param useShell
     * @param rootShell
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public void executeAdbCommand(String command, boolean useShell, boolean rootShell) throws InvalidCommandException, IOException {
        if (!command.equals("")) {
            String adbCmd = "";
            // Don't use shell at all
            if (!useShell) {
                adbCmd = command;
                cmd.executeProcessNoReturn(getAdb(), adbCmd);
            }
            // Use shell no root
            if (useShell && !rootShell) {
                adbCmd = "shell " + command;
                cmd.executeProcessNoReturn(getAdb(), adbCmd);
            }
            // Use shell with root
            if (useShell && rootShell) {
                adbCmd = String.format("-s {0} shell \"");
                adbCmd += "su -c \"";
                adbCmd += command;
                cmd.executeProcessNoReturn(getAdb(), adbCmd);
            }
        } else throw new InvalidCommandException("You have entered an empty command. Please type a valid command!");
    }
    
    /**
     * 
     * @param command
     * @param useShell
     * @param rootShell
     * @return
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public String executeAdbCommandReturnLastLine(String command, boolean useShell, boolean rootShell) throws InvalidCommandException, IOException {
        String adbCmd = "";
        if (!command.equals("")) {
            // Don't use shell at all
            if (!useShell) {
                adbCmd = command;
                return cmd.executeProcessReturnLastLine(getAdb(), adbCmd);
            }
            // Use shell no root
            if (useShell && !rootShell) {
                adbCmd = "shell " + command;
                return cmd.executeProcessReturnLastLine(getAdb(), adbCmd);
            }
            // Use shell with root
            if (useShell && rootShell) {
                adbCmd = String.format("-s {0} shell \"");
                adbCmd += "su -c \"";
                adbCmd += command;
                return cmd.executeProcessReturnLastLine(getAdb(), adbCmd);
            }
        } else throw new InvalidCommandException("You have entered an invalid command. Please type a valid command!"); return "";
    }
    
    /**
     * 
     * @param command
     * @param useShell
     * @param rootShell
     * @return
     * @throws InvalidCommandException
     * @throws IOException 
     */
    public StringBuilder executeAdbCommandReturnAll(String command, boolean useShell, boolean rootShell) throws InvalidCommandException, IOException {
        String adbCmd= "";
        StringBuilder out = null;
        if (!command.equals("")) {
            // Don't use shell at all
            if (!useShell) {
                adbCmd = command;
                out = cmd.executeProcessReturnAllOutput(getAdb(), adbCmd);
            }
            // Use shell no root
            if (useShell && !rootShell) {
                adbCmd = "shell " + command;
                out = cmd.executeProcessReturnAllOutput(getAdb(), adbCmd);
            }
            // Use shell with root
            if (useShell && rootShell) {
                adbCmd = String.format("-s {0} \"");
                adbCmd += "su -c\"";
                adbCmd += command;
                out = cmd.executeProcessReturnAllOutput(getAdb(), adbCmd);
            }
            return out;
        } else throw new InvalidCommandException("You have entered an empty command. Please enter a valid command!");
    }
}
