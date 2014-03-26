/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDroidLib.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import JDroidLib.enums.OS;

/**
 * ResourceManager - Manages resources used in JDroidLib.
 * Used to install ADB and fastboot to current computer, to either a custom location or the default location.
 * JDroidLib, when used, will always use the default locations to install and use ADB and fastboot.
 * However, you can install ADB and fastboot to custom locations, to, for example, allow non-Java applications to use ADB and fastboot on the desired system.
 * @author beatsleigher
 * @since Beta 0.0
 * @version 1.0
 */
public class ResourceManager {

    /**
     * Installs ADB to system. Is called by ADBController or CaptainKirk by
     * default. This method/class can be used to install ADB/fastboot to a
     * custom location on the drive.
     *
     * @param os the current platform.
     * @param location the installation dir. NEVER INPUT AN ACTUAL FILE HERE!
     * @throws IOException,
     * @throws ZipException
     * and also
     * @throws InterruptedException if something goes wrong.
     */
    public void install(OS os, String location) throws IOException, ZipException, InterruptedException {
        if (location.equals("Default")) {
            new Installer().install(os, new File(System.getProperty("user.home") + "/.jdroidlib/bin/"));
        } else {
            new Installer().install(os, new File(location));
        }
    }

    /**
     * You are not meant to understand this code.
     */
    private class Installer {

        public void install(OS os, File location) throws IOException, ZipException, InterruptedException {
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

        private void installOnLinux(File location) throws IOException, ZipException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists()) {
                try {
                    installDir.getParentFile().mkdirs();
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
                    ProcessBuilder process = new ProcessBuilder("chmod", "a+x", location + "/adb");
                    Process pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                    process = new ProcessBuilder("chmod", "a+x", location + "/fastboot");
                    pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                    adb_tools.delete();
                } catch (IOException | ZipException | InterruptedException ex) {
                    System.err.println("ERROR: Error while extracting adb_tools in " + location + " on system: Linux\n" + ex.toString());
                }
            }
        }

        private void installOnMac(File location) throws IOException, ZipException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists()) {
                    installDir.getParentFile().mkdirs();
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
                    ProcessBuilder process = new ProcessBuilder("chmod", "a+x", location + "/adb");
                    Process pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                    process = new ProcessBuilder("chmod", "a+x", location + "/fastboot");
                    pr = process.start();
                    Thread.sleep(200);
                    pr.destroy();
                    adb_tools.delete();
            }
        }

        private void installOnWin(File location) throws IOException, ZipException, InterruptedException {
            File installDir = new File(location + "/adb_tools.zip");
            if (!installDir.exists()) {
                    installDir.getParentFile().mkdirs();
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
                    adb_tools.delete();
            }
        }
    }

    /**
     * Read InputStream to String
     * @throws java.io.IOException If an I/O error occurs
     * */
    public static String readStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String read;
        StringBuilder sb = new StringBuilder();
        while((read = br.readLine()) != null)
        {
            sb.append(read);
        }
        return sb.toString();
    }

}
