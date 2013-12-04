/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.installers;

import java.io.*;
import java.net.*;
import JDroidLib.exceptions.*;

/**
 *
 * @author Simon
 */
public class FastbootInstaller {
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    // Get OS name
    String osName = System.getProperty("os.name");
    // Get user directory and add JDroidLib dir
    String m4gkbeatzDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
    // Windows fastboot filename
    File fastbootWin = new File(m4gkbeatzDir + "fastboot.exe");
    // Mac OS fastboot filename
    File fastbootMac = new File(m4gkbeatzDir + "fastboot");
    // Linux fastboot filename
    File fastbootLinux = new File(m4gkbeatzDir + "fastboot");
    
    // Windows fastboot URL
    String _fastbootWin = "http://team-m4gkbeatz.eu/beatsleigher/fastboot_win/fastboot.exe";
    // Mac OS fastboot URL
    String _fastbootMac = "http://team-m4gkbeatz.eu/Beatsleigher/fastboot_mac/fastboot";
    // Linux fastboot URL
    String _fastbootLinux = "http://team-m4gkbeatz.eu/Beatsleigher/fastboot_linux/fastboot";
    //</editor-fold>
    
    public FastbootInstaller() { }
    
    public void installFastboot() throws InvalidOSException, MalformedURLException, IOException {
        if (osName.contains("Windows")) {
            // Create necessary dirs
            if (!fastbootWin.exists()) fastbootWin.getParentFile().mkdirs();
            // Download files for Windows
            // adb.exe
            int dl = download(_fastbootWin, fastbootWin.toString());
            while (dl != 1) {
                // Wait for download to complete
            }
        } else if (osName.contains("Mac")) {
            // Create necessary dirs
            if (!fastbootMac.exists()) fastbootMac.getParentFile().mkdirs();
            // Download files for Mac
            int dl = download(_fastbootMac, fastbootMac.toString());
            while (dl != 1) { /*Wait for download to complete*/ }
        } else if (osName.equals("Linux")) /*Feel free to add more OSs*/ {
            // Create necessary dirs
            if (!fastbootLinux.exists()) fastbootLinux.getParentFile().mkdirs();
            // Download files for Linux
            int dl = download(_fastbootLinux, fastbootLinux.toString());
            while (dl != 1) { /*Wait for download to complete*/ }
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
    private int download(String url, String filename) throws MalformedURLException, IOException{
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
