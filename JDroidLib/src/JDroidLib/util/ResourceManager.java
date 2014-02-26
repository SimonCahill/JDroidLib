/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDroidLib.util;

import JDroidLib.enums.OS;

import java.io.*;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 *
 * @author beatsleigher
 */
public class ResourceManager {

    /**
     * Installs ADB to system. Is called by AndroidController or Commander by
     * default. This method/class can be used to install ADB/fastboot to a
     * custom location on the drive.
     *
     * @param os the current platform.
     * @param location the installation dir. NEVER INPUT AN ACTUAL FILE HERE!
     */
    public void install(OS os, String location) {
        if (location.equals("default")) {
            new Installer().install(os, new File(System.getProperty("user.home") + "/.jdroidlib/bin/"));
        } else {
            new Installer().install(os, new File(location));
        }
    }

    private class Installer {

        public void install(OS os, File location) {
            switch (os) {
                case LINUX:
                    installOnLinux(location);
                    break;
                case MAC_OS:
                    installOnMac(location);
                    break;
                case WINDOWS:
                    installOnWin(location);
                    break;
            }
        }

        private void installOnLinux(File location) {
            if (!new File(location + "/adb_tools.zip").exists()) {
                try {
                    InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb.zip");
                    File adb_tools = new File(location + "/adb_tools.zip");
                    OutputStream output = new FileOutputStream(adb_tools);
                    int readBytes = 0;
                    byte[] buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0) {
                        output.write(buffer, 0, readBytes);
                    }
                    input.close();
                    output.close();
                    ZipFile zip = new ZipFile(adb_tools);
                    zip.extractAll(location.toString());
                } catch (IOException | ZipException ex) {
                    System.err.println("ERROR: Error while extracting adb_tools in " + location + " on system: Linux\n" + ex.toString());
                }
            }
        }

        private void installOnMac(File location) {
            if (!new File(location + "/adb_tools.zip").exists()) {
                try {
                    InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-mac.zip");
                    File adb_tools = new File(location + "/adb_tools.zip");
                    OutputStream output = new FileOutputStream(adb_tools);
                    int readBytes = 0;
                    byte[] buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0) {
                        output.write(buffer, 0, readBytes);
                    }
                    input.close();
                    output.close();
                    ZipFile zip = new ZipFile(adb_tools);
                    zip.extractAll(location.toString());
                } catch (IOException | ZipException ex) {
                    System.err.println("ERROR: Error while extracting adb_tools in " + location + " on system: Mac OS X\n" + ex.toString());
                }
            }
        }

        private void installOnWin(File location) {
            if (!new File(location + "/adb_tools.zip").exists()) {
                try {
                    InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/adb_tools/adb-win.zip");
                    File adb_tools = new File(location + "/adb_tools.zip");
                    OutputStream output = new FileOutputStream(adb_tools);
                    int readBytes = 0;
                    byte[] buffer = new byte[4096];
                    while ((readBytes = input.read(buffer)) > 0) {
                        output.write(buffer, 0, readBytes);
                    }
                    input.close();
                    output.close();
                    ZipFile zip = new ZipFile(adb_tools);
                    zip.extractAll(location.toString());
                } catch (IOException | ZipException ex) {
                    System.err.println("ERROR: Error while extracting adb_tools in " + location + " on system: Windoze\n" + ex.toString());
                }
            }
        }
    }

}
