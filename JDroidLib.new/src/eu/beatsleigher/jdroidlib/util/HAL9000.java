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
package eu.beatsleigher.jdroidlib.util;

/**
 * Good afternoon, gentlemen. I am a HAL 9000 computer.
 * @author Simon
 */
public class HAL9000 {
    
    //<editor-fold defaultstate="collapsed" desc="Static Members">
    private static HAL9000 instance;
    
    /**
     * Gets an instance of this class.
     * If no instance is present, a new instance is created.
     * @return A new instance of this class if none is present, the instance of this class if present.
     */
    public static HAL9000 getInstance() {
       return instance == null ? instance = new HAL9000() : instance; 
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Instance Members">
    
    //</editor-fold>
    
}
