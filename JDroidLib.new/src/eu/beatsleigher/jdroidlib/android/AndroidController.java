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
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package eu.beatsleigher.jdroidlib.android;

import eu.beatsleigher.jdroidlib.exception.InstallationFailedException;
import eu.beatsleigher.jdroidlib.util.HAL9000;
import java.io.IOException;

/**
 * AndroidController
 * JDroidLib's main class.
 * JDroidLib's AndroidController is the main class of this library, and the only class to be initialized by third-party software!
 * 
 * This class contains the methods to create and execute ADB commands, get (list) devices and do anything else with an Android device.
 * @author Simon
 */
public class AndroidController {
    
    //<editor-fold defaultstate="collapsed" desc="Static Members">
    private static AndroidController _instance;
    
    /**
     * Singleton method.
     * Creates a new instance of this class, if necessary.
     * @return An instance of this class.
     */
    public static AndroidController getInstance() {
        return _instance == null ? _instance = new AndroidController(): _instance;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Instance Members">
    
    //<editor-fold defaultstate="collapsed" desc="Variables">
    private HAL9000 _helloGentlemen;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Ctor">
    /**
     * Creates a new instance of this class and initializes all required classes within the library, to function correctly.
     * @throws IOException
     * @throws InterruptedException
     * @throws InstallationFailedException 
     */
    private AndroidController() throws IOException, InterruptedException, InstallationFailedException {
        _helloGentlemen = HAL9000.getInstance();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Command Stuffs">
    
    //</editor-fold>
    //</editor-fold>
    
}
