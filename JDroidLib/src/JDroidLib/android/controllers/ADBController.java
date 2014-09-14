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
package JDroidLib.android.controllers;

import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.util.*;

import JDroidLib.android.device.Device;
import JDroidLib.enums.RebootTo;
import JDroidLib.exceptions.*;
import JDroidLib.util.CaptainKirk;

/**
 * JDroidLib's main class.
 * Always create an instance of this class and derive other classes from here.
 * {@see CaptainKirk} is <u>not</u> meant to be used by anything other than JDroidLib - Only use in emergencies!
 * 
 * To "start" JDroidLib, use the following code:
 * <pre>
 * {@code
 * public class YourClass {
 *      private final ADBController adbController; // Make this final when possible, you're not going to need to change this. <b>ONLY CREATE ONE INSTANCE PER PROGRAM!</b>
 * 
 *      public YourClass() {
 *          try {
 *              adbController = new ADBController(); // Assign final variable adbController value of new object, type ADBController
 *          } catch (IOException | ZipException | InterruptedException ex) { // ADBController's constructor throws these exceptions
 *              System.err.println("Error starting JDroidLib (CODE: BLA)); // You don't have to do this..
 *              ex.printStackTrace(System.err);
 *          }
 *      }
 * 
 * }
 * 
 * }
 * </pre>
 * 
 * That code initializes JDroidLib and automatically tells JDroidLib to copy any and all needed files to the local hard drive. These are deleted once the JVM exits.
 * 
 * To get an instance of FastbootController, use the following code (make sure you have a running instance of ADBController):
 * <pre>
 * {@code
 * private final FastbootController fbController = adbController.getFastbootController(); // Make it final where you can, you won't need to change it.
 * }
 * </pre>
 *
 * @author Beatsleigher
 * @since beta
 *
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public final class ADBController {

    private CaptainKirk controller = null;
    private FastbootController fbController = null;
    private static ADBController instance = null;
    
    /**
     * Singleton Function
     * @return Returns an (and if necessary creates a new) instance of ADBController.
     * @throws IOException This exception is thrown if something goes wrong while JDroidLib is extracting needed files or performing other IO-ops.
     * @throws InterruptedException This exception is thrown if JDroidLib encounters an error while performing threaded operations.
     * @throws OSNotSupportedException This exception is thrown if JDroidLib detects that the host is running a non-supported operating system.
     */
    public static ADBController getInstance() throws IOException, InterruptedException, OSNotSupportedException {
        if (instance != null)
            return instance;
        else return (instance = new ADBController());
    }

    private ADBController() throws IOException, InterruptedException, OSNotSupportedException {
        
    }

}
