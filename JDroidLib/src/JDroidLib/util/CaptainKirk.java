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

import JDroidLib.android.controllers.ADBController;
import JDroidLib.android.device.Device;
import JDroidLib.enums.*;
import JDroidLib.exceptions.*;

import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.util.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.net.InetAddress;
import java.nio.file.CopyOption;
import java.nio.file.Files;

/**
 * This is Captain Kirk! Say hello! He will be our commander and captain,
 * throughout this journey. He will defeat those meanies and protect your device
 * from unwanted stuff. Or, he will at some point in time!
 *
 * @author beatsleigher
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend"})
public class CaptainKirk {

    ResourceManager resMan = null;
    private File adb = null;
    private File fastboot = null;
    private final File backupDir;
    private static CaptainKirk instance = null;
    
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

    

}

/*Please ignore this. This is just here, so I don't always have to open methods, but can just C&P comments I need.*/
///////////////////
// Variables /////
/////////////////
////////////////////
// Execute command/
//////////////////
