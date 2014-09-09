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


import net.lingala.zip4j.core.ZipFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import JDroidLib.enums.OS;
import JDroidLib.exceptions.*;

/**
 * ResourceManager - Manages resources used in JDroidLib.
 * Used to install ADB and fastboot to current computer, to either a custom
 * location or the default location.
 * JDroidLib, when used, will always use the default locations to install and
 * use ADB and fastboot.
 * However, you can install ADB and fastboot to custom locations, to, for
 * example, allow non-Java applications to use ADB and fastboot on the desired
 * system.
 *
 * @author beatsleigher
 * @since Beta 0.0
 * @version 1.0
 */
public class ResourceManager {
    
    private File adb = null;
    private File fastboot = null;
    private File backupDir = null;

    /**
     * Installs ADB to system. Is called by ADBController or CaptainKirk by
     * default. This method/class can be used to install ADB/fastboot to a
     * custom location on the drive.
     *
     * @param os       the current platform.
     * @param location the installation dir. NEVER INPUT AN ACTUAL FILE HERE!
     *
     * @throws IOException,
     * @throws net.lingala.zip4j.exception.ZipException If an exception occurred while unzipping the archives.
     * @throws JDroidLib.exceptions.OSNotSupportedException If JDroidLib detects an unsupported OS.
     *                             and also
     * @throws InterruptedException if something goes wrong.
     */
    public void install(OS os, String location) throws IOException, InterruptedException, OSNotSupportedException {
        if (location.equals("Default"))
            if (os == OS.LINUX || os == OS.MAC_OS)
                new Installer().install(os, new File(System.getProperty("user.home") + "/.jdroidlib/bin/"));
            else if (os == OS.WINDOWS)
                new Installer().install(os, new File(System.getProperty("user.home") + "/AppData/Roaming/JDroidLib/bin/"));
            else throw new OSNotSupportedException("The current operating system is not supported by JDroidLib. OS Name: " + System.getProperty("os.name"));
        else
            new Installer().install(os, new File(location));
    }
    
    /**
     * Returns a File-object which represents the ADB-binary.
     * @return adb[.exe]
     */
    public File getADB() { return adb; }
    
    /**
     * Returns a File-object which represents the Fastboot-binary.
     * @return fastboot[.exe]
     */
    public File getFastboot() { return fastboot; }
    
    /**
     * Returns a File-object representing the backup directory.
     * @return 
     */
    public File getBackupDir() { return backupDir; }

    /**
     * You are not meant to understand this code.
     */
    private class Installer {

        public void install(OS os, File location) throws IOException, InterruptedException {
            switch (os) {
                case LINUX:
                    installOnLinux(location);
                    adb = new File(location + "/adb");
                    fastboot = new File(location + "/fastboot");
                    backupDir = new File(location + "/../binBackup");
                    break;
                case MAC_OS:
                    installOnMac(location);
                    adb = new File(location + "/adb");
                    fastboot = new File(location + "/fastboot");
                    backupDir = new File(location + "/../binBackup");
                    break;
                case WINDOWS:
                    installOnWin(location);
                    adb = new File(location + "/adb.exe");
                    fastboot = new File(location + "/fastboot.exe");
                    backupDir = new File(location + "/../binBackup");
                    break;
            }
        }

        private void installOnLinux(File location) throws IOException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists())
                try {
                    installDir.getParentFile().mkdirs();
                    InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb/adb");
                    File adb_tools = new File(location + "/adb");
                    OutputStream output = new FileOutputStream(adb_tools);
                    int readBytes = 0;
                    byte[] buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0) {
                        output.write(buffer, 0, readBytes);
                    }
                    input.close();
                    output.close();
                    /*# ========== #*/
                    input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb/fastboot");
                    adb_tools = new File(location + "/fastboot");
                    output = new FileOutputStream(adb_tools);
                    readBytes = 0;
                    buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0)
                        output.write(buffer, 0, readBytes);
                    input.close();
                    output.close();
                    /*# ========== #*/
                    input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb/ddms.jar");
                    adb_tools = new File(location + "/ddms.jar");
                    output = new FileOutputStream(adb_tools);
                    readBytes = 0;
                    buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0)
                        output.write(buffer, 0, readBytes);
                    input.close();
                    output.close();
                    /*# ========== #*/
                    ProcessBuilder process = new ProcessBuilder("chmod", "a+x", location + "/adb");
                    Process pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                    process = new ProcessBuilder("chmod", "a+x", location + "/fastboot");
                    pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                } catch (IOException | InterruptedException ex) {
                    System.err.println("ERROR: Error while extracting adb_tools in " + location + " on system: Linux\n" + ex.toString());
                    throw ex;
                }
        }

        private void installOnMac(File location) throws IOException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists()) {
                installDir.getParentFile().mkdirs();
                InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-mac/adb");
                File adb_tools = new File(location + "/adb");
                OutputStream output = new FileOutputStream(adb_tools);
                int readBytes = 0;
                byte[] buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-mac/fastboot");
                adb_tools = new File(location + "/fastboot");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-mac/ddms.jar");
                adb_tools = new File(location + "/ddms.jar");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                ProcessBuilder process = new ProcessBuilder("chmod", "a+x", location + "/adb");
                Process pr = process.start();
                Thread.sleep(200);
                pr.destroy();
                process = new ProcessBuilder("chmod", "a+x", location + "/fastboot");
                pr = process.start();
                Thread.sleep(200);
                pr.destroy();
            }
        }

        private void installOnWin(File location) throws IOException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists()) {
                installDir.getParentFile().mkdirs();
                InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win/adb.exe");
                File adb_tools = new File(location + "/adb.exe");
                OutputStream output = new FileOutputStream(adb_tools);
                int readBytes = 0;
                byte[] buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win/AdbWinApi.dll");
                adb_tools = new File(location + "/AdbWinApi.dll");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win/AdbWinUsbApi.dll");
                adb_tools = new File(location + "/AdbWinUsbApi.dll");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win/fastboot.exe");
                adb_tools = new File(location + "/fastboot.exe");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win/ddms.jar");
                adb_tools = new File(location + "/ddms.jar");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                adb_tools.delete();
            }
        }

    }

    /**
     * Read InputStream to String
     *
     * @param inputStream
     *
     * @return
     *
     * @throws java.io.IOException If an I/O error occurs
     *
     */
    public static String readStreamToString(InputStream inputStream) throws IOException { // I have no idea who put this here, or what it's purpose is (I understand the code, before you ask...)
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        StringBuilder sb = new StringBuilder();
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        return sb.toString();
    }

}
