package com.pedja;

import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;

import JDroidLib.android.controllers.ADBController;

/**
 * Created by pedja on 26.3.14..
 */
public class test
{
    public static void main(String[] args)
    {
        try
        {
            ADBController controller = new ADBController();
            
        }
        catch (IOException | InterruptedException | ZipException e)
        {
            e.printStackTrace();
        }
    }
}
