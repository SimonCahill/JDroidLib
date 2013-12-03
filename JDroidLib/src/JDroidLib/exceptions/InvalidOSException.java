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
public class InvalidOSException extends Exception {
    public InvalidOSException() { super(); }
    
    public InvalidOSException(String details) {
        super("JDroidLib.exceptions.InvalidOSException " + details);
    }
}
