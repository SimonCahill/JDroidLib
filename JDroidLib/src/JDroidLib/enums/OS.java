/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.enums;

import JDroidLib.exceptions.OSNotSupportedException;

/**
 * The operating systems supported by JDroidLib.
 * @author beatsleigher
 */
public enum OS {
    WINDOWS, LINUX, MAC_OS;
    
    /**
     * Attempts to return a type of @see OS via a value.
     * @param val The OS to return
     * @return A type of @see OS
     * @throws OSNotSupportedException If no type of @see OS could be determined. 
     */
    public static OS getOS(String val) throws OSNotSupportedException {
        for (OS os : OS.values())
            if (val.contains(os.toString()))
                return os;
        throw new OSNotSupportedException("OS \"" + val + "\" is not supported by JDroidLib!");
    }
    
}
