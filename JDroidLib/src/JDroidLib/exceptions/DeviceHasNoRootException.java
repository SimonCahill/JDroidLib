/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package JDroidLib.exceptions;

/**
 *
 * @author Simon
 */
public class DeviceHasNoRootException extends Exception {
    
    public DeviceHasNoRootException() { super(); }
    
    public DeviceHasNoRootException(String details) { super("Exception JDroidLib.exceptions.DeviceHasNoRootException " + details); }
    
}
