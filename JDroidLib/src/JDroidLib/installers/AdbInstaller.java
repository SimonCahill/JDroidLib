/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.installers;

import java.net.*;
import java.io.*;
import JDroidLib.exceptions.*;

/**
 *
 * @author Simon
 */
public class AdbInstaller {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    // Get OS name
    static String osName = System.getProperty("os.name");
    // Get user directory and add JDroidLib dir
    static String m4gkbeatzDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
    // Windows ADB filenames
    static File adbWin = new File(m4gkbeatzDir + "adb.exe");
    static File adbWinApi = new File(m4gkbeatzDir + "AdbWinApi.dll");
    static File adbWinUsbApi = new File(m4gkbeatzDir + "AdbWinUsbApi.dll");
    // Linux ADB file name
    static File adbLinux = new File(m4gkbeatzDir + "adb");
    // Mac OS ADB file name
    static File adbMac = new File(m4gkbeatzDir + "adb");
    
    // Windows ADB URLs
    static String _adbWin = "http://team-m4gkbeatz.eu/Beatsleigher/adb_win/adb.exe";
    static String _adbWinApi = "http://team-m4gkbeatz.eu/Beatsleigher/adb_win/AdbWinApi.dll";
    static String _adbWinUsbApi = "http://team-m4gkbeatz.eu/Beatsleigher/adb_win/AdbWinUsbApi.dll";
    // Linux ADB URL
    static String _adbLinux = "http://team-m4gkbeatz.eu/Beatsleigher/adb_linux/adb";
    // Mac OS ADB URL
    static String _adbMac = "http://team-m4gkbeatz.eu/Beatsleigher/adb_mac/adb";
    
    // Settings file
    
    //</editor-fold>
    
    public AdbInstaller() {  }
    
    /**
     * Installs ADB binaries to location on the hard drive for use only with JDroidLib.
     * Can be used with other programs.
     * @throws InvalidOSException
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void installAdb() throws InvalidOSException, MalformedURLException, IOException {
        if (osName.contains("Windows")) {
            // Create necessary dirs
            if (!adbWin.exists()) adbWin.getParentFile().mkdirs();
            // Download files for Windows
            // adb.exe
            int dl = download(_adbWin, adbWin.toString());
            while (dl != 1) {
                // Wait for download to complete
            }
            // AdbWinApi.dll
            dl = download(_adbWinApi, adbWinApi.toString());
            while (dl != 1) { /*Wait for download to complete*/}
            // AdbWinUsbApi.dll
            dl = download(_adbWinUsbApi, adbWinUsbApi.toString());
            while (dl != 1) { /*Wait for download to complete*/ }
            return;
        } else if (osName.contains("Mac")) {
            // Create necessary dirs
            if (!adbMac.exists()) adbMac.getParentFile().mkdirs();
            // Download files for Mac
            int dl = download(_adbMac, adbMac.toString());
            while (dl != 1) { /*Wait for download to complete*/ }
            return;
        } else if (osName.contains("Ubuntu") | osName.contains("Debian") | osName.contains("Red Hat") | osName.contains("Fedora") | osName.contains("Damn Small")) /*Feel free to add more OSs*/ {
            // Create necessary dirs
            if (!adbLinux.exists()) adbLinux.getParentFile().mkdirs();
            // Download files for Linux
            int dl = download(_adbLinux, adbLinux.toString());
            while (dl != 1) { /*Wait for download to complete*/ }
            return;
        } else throw new InvalidOSException("WARNING: OS could not be determined or is not supported for ADB installation!");
    }
    
    /**
     * Download files to specified directory.
     * @param url
     * @param filename
     * @return
     * @throws MalformedURLException
     * @throws IOException 
     */
    private static int download(String url, String filename) throws MalformedURLException, IOException{
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream());
            fout = new FileOutputStream(filename);

            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
            {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
        return 1;
    }
    
}
