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

package JDroidLib.util;

import JDroidLib.enums.*;
import JDroidLib.exceptions.*;
import JDroidLib.interfaces.*;

import java.io.*;

/**
 * This is Captain Kirk! Say hello! He will be our commander and captain,
 * throughout this journey. He will defeat those meanies and protect your device
 * from unwanted stuff. Or, he will at some point in time!
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "FieldMayBeFinal"})
public class CaptainKirk implements Disposeable {

    // These values can't be final, because they'll be set to >>null<< by the dispose() method.
    private ResourceManager resMan;
    private File adb;
    private File fastboot;
    private File backupDir;
    private static CaptainKirk instance = null;
    private File workingDir = new File(String.format("{0}{1}{2}", System.getProperty("user.home"), System.getProperty("path.seperator"), "ADB-Fastboot-Work"));
    
    /**
     * Singleton statement.
     * Returns a CaptainKirk object. If no previous instance of this class was found, it will return a new instance
     * and will perform all the startup operations.
     * @return Returns an instance of CaptainKirk
     * @throws IOException This exception is thrown if something goes wrong while JDroidLib is extracting needed files or performing other IO-ops.
     * @throws InterruptedException This exception is thrown if JDroidLib encounters an error while performing threaded operations.
     * @throws OSNotSupportedException This exception is thrown if JDroidLib detects that the host is running a non-supported operating system.
     */
    public static CaptainKirk getInstance() throws IOException, InterruptedException, OSNotSupportedException {
        if (instance != null)
            return instance;
        else 
            return (instance = new CaptainKirk());
    }
    public static File getADB() { return instance.adb; }
    public static File getFastboot() { return instance.fastboot; }

    private CaptainKirk() throws IOException, InterruptedException, OSNotSupportedException {
        resMan = new ResourceManager();
        String osName = System.getProperty("os.name").split("[\\s]")[0];
        resMan.install(OS.getOS(osName), "default");
        adb = resMan.getADB();
        fastboot = resMan.getFastboot();
        backupDir = resMan.getBackupDir();
    }
    
    //# ========== Processing Methods Start ========== #\\
    /**
     * Executes ADB, fastboot and anonymous commands.
     * This method is used to execute all commands via the ADB server or fastboot server.
     * @param command The @see Command object containing all the information needed to execute commands.
     * @return If the property "returnsOutput" in the command object is set to <u>true</u>, 
     * this function will return the output provided by the command.
     * @throws IOException If an error occurs while executing the process.
     */
    public String executeCommand(Command command) throws IOException {
        if (command == null) throw new IllegalArgumentException("Command cannot be null!");
        StringBuilder sBuilder = new StringBuilder();
        
        Process pr;
        ProcessBuilder process = new ProcessBuilder();
        BufferedReader iStreamReader;
        String line = null;
        
        process.command(command.getParameters());
        process.directory(workingDir);
        process.redirectErrorStream(true);
        pr = process.start();
        
        // Process output
        iStreamReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        while ((line = iStreamReader.readLine()) != null)
            if (command.returnsOutput()) {
                sBuilder.append(line);
                sBuilder.append("\n");
            }
        
        executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("kill-server"), false, true));
        
        return sBuilder.toString();
    }
    //# ========== Processing Methods  End  ========== #\\

    /**
     * Prepares this object for disposal.
     */
    @Override
    public void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

/*Please ignore this. This is just here, so I don't always have to open methods, but can just C&P comments I need.*/
///////////////////
// Variables /////
/////////////////
////////////////////
// Execute command/
//////////////////
