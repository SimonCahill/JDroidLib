/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.Android;

import JDroidLib.exceptions.InvalidOSException;
import java.io.*;
import java.util.*;
import JDroidLib.installers.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon
 */
public final class Android {
    private final String mbDir = System.getProperty("user.home") + "/.m4gkbeatz/JDroidLib/bin/";
    private final File adb = null;
    private final File fastboot = null;
    private static final AdbInstaller adbInstaller = new AdbInstaller();
    private static final FastbootInstaller fastbootInstaller = new FastbootInstaller();
    
    private static Android _instance;
    
    private List<String> conDevices;
    
    public static Android instance() {
     if (_instance == null) {
         _instance = new Android();
         _instance.installAdb();
     }   
     return _instance;
    }
    
    private static void installAdb() {
        try {
            adbInstaller.installAdb();
            fastbootInstaller.installFastboot();
        } catch (InvalidOSException ex) {
            Logger.getLogger(Android.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Android.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> connectedDevices() {
        
        return conDevices;
    }
    
    public void updateDevList() {
        String devList = "";
        
        this.conDevices.clear();
        
        //devList = 
    }
    
    
}
