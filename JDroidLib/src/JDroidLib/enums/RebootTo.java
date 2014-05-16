/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.enums;

/**
 * The modes that a device can be booted to.
 * @author beatsleigher
 */
public enum RebootTo {
    /**
     * Reboot the device to Android OS.
     */
    ANDROID, 
    /**
     * Reboot the device to recovery mode.
     */
    RECOVERY, 
    /**
     * Reboot the device to its respective bootloader.
     */
    BOOTLOADER
}
