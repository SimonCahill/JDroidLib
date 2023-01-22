/*
 * Copyright (C) 2015 Simon.
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
 * License along with instance library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package eu.beatsleigher.jdroidlib.util;

import eu.beatsleigher.jdroidlib.exception.InstallationFailedException;
import java.io.*;

/**
 * ResourceManager class. This class is an internal class and is not designed
 * for use applications. ResourceManager manages the resources provided by
 * JDroidLib. As an example, it installs the ADB binaries to the local
 * filesystem and deletes the installed files as soon as the JVM exits.
 *
 * @author Simon
 */
class ResourceManager {

    static class Installer {

        private static final File installPath;
        private static final String os = System.getProperty("os.name").toLowerCase();
        private static final String userHome = System.getProperty("user.home");
        private static final Installer instance;
        private static File adbExe;
        private static File fastbootExe;
        private static File ddmsJar;

        /**
         * Default constructor.
         */
        static {
            if (os.contains("windows")) {
                installPath = new File(String.format("%s/AppData/com/beatsleigher/jdroidlib/bin/", userHome));
            } else {
                installPath = new File(String.format("%s/com/beatsleigher/jdroidlib/bin", userHome));
            }
            instance = new Installer();
        }

        /**
         * Installs the ADB and fastboot binaries to the local system.
         *
         * @return Returns a boolean value indicating whether the install was
         * successful or not.
         * @author Simon
         * @throws IOException if an error occurs during installation.
         * @throws InterruptedException if an error occurs during installation.
         * @throws InstallationFailedException if files were not installed (correctly).
         */
        static boolean installBinaries() throws IOException, InterruptedException, InstallationFailedException {
            if (os.contains("windows")) {
                return installWin();
            } else if (os.contains("linux")) {
                return installLinux();
            } else {
                return installMac();
            }
        }

        /**
         * Installs the ADB binaries for the Linux operating systems.
         *
         * @return {@code true} if install was successful, {@code false} if not.
         * @author Simon
         * @throws IOException if an error occurs during installation.
         * @throws InterruptedException if an error occurs during installation.
         * @throws InstallationFailedException if files were not installed (correctly).
         */
        private static boolean installLinux() throws IOException, InterruptedException, InstallationFailedException {
            if (!installPath.exists()) {
                installPath.mkdirs();
            }

            try {
                InputStream input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/linux/adb");
                File adb_tools = new File(installPath.getAbsolutePath() + "/adb");
                OutputStream output = new FileOutputStream(adb_tools);
                int readBytes = 0;
                byte[] buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/linux/fastboot");
                adb_tools = new File(installPath.getAbsolutePath() + "/fastboot");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/linux/ddms.jar");
                adb_tools = new File(installPath.getAbsolutePath() + "/ddms.jar");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                ProcessBuilder process = new ProcessBuilder("chmod", "a+x", installPath.getAbsolutePath() + "/adb");
                Process pr = process.start();
                Thread.sleep(200);
                pr.destroy();
                process = new ProcessBuilder("chmod", "a+x", installPath.getAbsolutePath() + "/fastboot");
                pr = process.start();
                Thread.sleep(200);
                pr.destroy();
            } finally {
                adbExe = new File(String.format("$s/adb", installPath.getAbsolutePath()));
                fastbootExe = new File(String.format("$s/fastboot", installPath.getAbsolutePath()));
                ddmsJar = new File(String.format("$s/ddms.jar", installPath.getAbsolutePath()));
                if (adbExe.exists() && fastbootExe.exists() && ddmsJar.exists()) {
                    adbExe.deleteOnExit();
                    fastbootExe.deleteOnExit();
                    ddmsJar.deleteOnExit();
                    return true;
                } else {
                    String exceptionMessage = String.format(
                              "An error has occurred during installation!\n"
                            + "Files attempted to install:\n"
                            + "adb[.exe]:\t\t%s"
                            + "fastboot[.exe]:\t\t%s"
                            + "ddms.jar:\t\t%s", adbExe.exists() ? adbExe.getAbsolutePath() : "Installation failed!",
                                                 fastbootExe.exists() ? fastbootExe.getAbsolutePath() : "Installation failed!",
                                                 ddmsJar.exists() ? ddmsJar.getAbsolutePath() : "Installation failed!");
                    throw new InstallationFailedException(exceptionMessage);
                }
            }
        }

        /**
         * Installs the ADB binaries for the Mac OS operating system.
         *
         * @return {@code true} if installation was successful, {@code false} if
         * not.
         * @throws IOException if an error occurs during installation.
         * @throws InterruptedException if an error occurs during installation.
         */
        private static boolean installMac() throws IOException, InterruptedException, InstallationFailedException {
            if (!installPath.exists()) {
                installPath.mkdirs();
            }

            try {
                InputStream input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/mac/adb");
                File adb_tools = new File(installPath.getAbsolutePath() + "/adb");
                OutputStream output = new FileOutputStream(adb_tools);
                int readBytes = 0;
                byte[] buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/mac/fastboot");
                adb_tools = new File(installPath.getAbsolutePath() + "/fastboot");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/mac/ddms.jar");
                adb_tools = new File(installPath.getAbsolutePath() + "/ddms.jar");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                ProcessBuilder process = new ProcessBuilder("chmod", "a+x", installPath.getAbsolutePath() + "/adb");
                Process pr = process.start();
                Thread.sleep(200);
                pr.destroy();
                process = new ProcessBuilder("chmod", "a+x", installPath.getAbsolutePath() + "/fastboot");
                pr = process.start();
                Thread.sleep(200);
                pr.destroy();
            } finally {
                adbExe = new File(String.format("$s/adb", installPath.getAbsolutePath()));
                fastbootExe = new File(String.format("$s/fastboot", installPath.getAbsolutePath()));
                ddmsJar = new File(String.format("$s/ddms.jar", installPath.getAbsolutePath()));
                if (adbExe.exists() && fastbootExe.exists() && ddmsJar.exists()) {
                    adbExe.deleteOnExit();
                    fastbootExe.deleteOnExit();
                    ddmsJar.deleteOnExit();
                    return true;
                } else {
                    String exceptionMessage = String.format(
                              "An error has occurred during installation!\n"
                            + "Files attempted to install:\n"
                            + "adb[.exe]:\t\t%s"
                            + "fastboot[.exe]:\t\t%s"
                            + "ddms.jar:\t\t%s", adbExe.exists() ? adbExe.getAbsolutePath() : "Installation failed!",
                                                 fastbootExe.exists() ? fastbootExe.getAbsolutePath() : "Installation failed!",
                                                 ddmsJar.exists() ? ddmsJar.getAbsolutePath() : "Installation failed!");
                    throw new InstallationFailedException(exceptionMessage);
                }
            }
        }

        /**
         * Installs the ADB binaries for the Windows operating system.
         *
         * @return {@code true} if installation was successful, {@code false} if
         * not.
         * @throws IOException if an error occurs during installation.
         * @throws InterruptedException if an error occurs during installation.
         */
        private static boolean installWin() throws IOException, InterruptedException, InstallationFailedException {
            try {
                InputStream input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/win/adb.exe");
                File adb_tools = new File(installPath.getAbsolutePath() + "/adb.exe");
                OutputStream output = new FileOutputStream(adb_tools);
                int readBytes = 0;
                byte[] buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0) {
                    output.write(buffer, 0, readBytes);
                }
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/win/AdbWinApi.dll");
                adb_tools = new File(installPath.getAbsolutePath() + "/AdbWinApi.dll");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/win/AdbWinUsbApi.dll");
                adb_tools = new File(installPath.getAbsolutePath() + "/AdbWinUsbApi.dll");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/win/fastboot.exe");
                adb_tools = new File(installPath.getAbsolutePath() + "/fastboot.exe");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
                input = instance.getClass().getResourceAsStream("/eu/beatsleigher/jdroidlib/res/adb/win/ddms.jar");
                adb_tools = new File(installPath.getAbsolutePath() + "/ddms.jar");
                output = new FileOutputStream(adb_tools);
                readBytes = 0;
                buffer = new byte[4096];
                while ((readBytes = input.read(buffer)) > 0)
                    output.write(buffer, 0, readBytes);
                input.close();
                output.close();
                /*# ========== #*/
            } finally {
                adbExe = new File(String.format("%s/adb", installPath.getAbsolutePath()));
                File adbDll = new File(String.format("%s/AdbWinApi.dll", installPath.getAbsolutePath()));
                File adbUsbDll = new File(String.format("%s/AdbWinUsbApi.dll", installPath.getAbsolutePath()));
                fastbootExe = new File(String.format("%s/fastboot", installPath.getAbsolutePath()));
                ddmsJar = new File(String.format("%s/ddms.jar", installPath.getAbsolutePath()));
                if (adbExe.exists() && fastbootExe.exists() && ddmsJar.exists()) {
                    adbExe.deleteOnExit();
                    fastbootExe.deleteOnExit();
                    ddmsJar.deleteOnExit();
                    return true;
                } else {
                    String exceptionMessage = String.format(
                              "An error has occurred during installation!\n"
                            + "Files attempted to install:\n"
                            + "adb[.exe]:\t\t%s"
                            + "AdbWinApi.dll:\t\t%s"
                            + "AdbWinUsbApi.dll.\t\t%s"
                            + "fastboot[.exe]:\t\t%s"
                            + "ddms.jar:\t\t%s", adbExe.exists() ? adbExe.getAbsolutePath() : "Installation failed!",
                                                 adbDll.exists() ? adbDll.getAbsolutePath() : "Installation failed!",
                                                 adbUsbDll.exists() ? adbUsbDll.getAbsolutePath() : "Installation failed!",
                                                 fastbootExe.exists() ? fastbootExe.getAbsolutePath() : "Installation failed!",
                                                 ddmsJar.exists() ? ddmsJar.getAbsolutePath() : "Installation failed!");
                    throw new InstallationFailedException(exceptionMessage);
                }
            }
        }
        
        /**
         * Gets the installation path for the ADB binaries.
         * @return The directory to which the ADB binaries are installed.
         */
        static File getInstallPath() { return installPath; }
        
        /**
         * Gets a {@link java.io.File} pointing to the installed ADB executable.
         * @return The ADB executable.
         */
        static File getAdbExe() { return adbExe; }
        
        /**
         * Gets a {@link java.io.File} pointing to the fastboot executable installed on the system.
         * @return The fastboot executable.
         */
        static File getFastbootExe() { return fastbootExe; }
        
        /**
         * Gets a {@link java.io.File} pointing to the installed DDMS Jarfile.
         * @return The DDMS Jarfile
         */
        static File getDdmsJar() { return ddmsJar; }

    }

}
