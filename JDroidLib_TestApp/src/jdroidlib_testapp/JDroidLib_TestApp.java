/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jdroidlib_testapp;

import JDroidLib.utils.ui.*;

/**
 *
 * @author Simon
 */
public class JDroidLib_TestApp {
    
    static TestUI ui = new TestUI();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ui.setVisible(true);
        while (ui.isVisible()) {
           System.out.println("JDroidLib Test UI is visible");
        }
        System.out.println("JDroidLib test UI is no longer visible. Exiting...");
        System.exit(0);
    }
    
}
