/*
 * Copyright (C) 2014 beatsleigher
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package JDroidLib.android.controllers;

import java.io.*;
import java.util.*;

import JDroidLib.exceptions.*;
import JDroidLib.util.CaptainKirk;
import JDroidLib.android.device.*;
import JDroidLib.util.*;

/**
 * JDroidLib's main class.
 * Always create an instance of this class and derive other classes from here.
 * {@see CaptainKirk} is <u>not</u> meant to be used by anything other than JDroidLib - Only use in emergencies!
 * 
 * To "start" JDroidLib, use the following code:
 * <pre>
 * {@code
 * public class YourClass {
 *      private final ADBController adbController; // Make this final when possible, you're not going to need to change this. <b>ONLY CREATE ONE INSTANCE PER PROGRAM!</b>
 * 
 *      public YourClass() {
 *          try {
 *              adbController = new ADBController(); // Assign final variable adbController value of new object, type ADBController
 *          } catch (IOException | ZipException | InterruptedException ex) { // ADBController's constructor throws these exceptions
 *              System.err.println("Error starting JDroidLib (CODE: BLA)); // You don't have to do this..
 *              ex.printStackTrace(System.err);
 *          }
 *      }
 * 
 * }
 * 
 * }
 * </pre>
 * 
 * That code initializes JDroidLib and automatically tells JDroidLib to copy any and all needed files to the local hard drive. These are deleted once the JVM exits.
 * 
 * To get an instance of FastbootController, use the following code (make sure you have a running instance of ADBController):
 * <pre>
 * {@code
 * private final FastbootController fbController = adbController.getFastbootController(); // Make it final where you can, you won't need to change it.
 * }
 * </pre>
 *
 * @author Beatsleigher
 * @since beta
 *
 */
@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnusedAssignment", "StringConcatenationInsideStringBufferAppend", "ConvertToTryWithResources"})
public final class ADBController {

    private final CaptainKirk controller;
    private static ADBController instance = null;
    
    /**
     * Singleton Function
     * @return Returns an (and if necessary creates a new) instance of ADBController.
     * @throws IOException This exception is thrown if something goes wrong while JDroidLib is extracting needed files or performing other IO-ops.
     * @throws InterruptedException This exception is thrown if JDroidLib encounters an error while performing threaded operations.
     * @throws OSNotSupportedException This exception is thrown if JDroidLib detects that the host is running a non-supported operating system.
     */
    public static ADBController getInstance() throws IOException, InterruptedException, OSNotSupportedException {
        if (instance != null)
            return instance;
        else return (instance = new ADBController());
    }

    private ADBController() throws IOException, InterruptedException, OSNotSupportedException {
        controller = CaptainKirk.getInstance();
    }
    
    /**
     * Parses ADB output and creates a list of device objects, which represents the devices connected to the computer.
     * @return A list of device object. Each @see Device in the list represents a physical (or virtual/emulator) device connected to the computer.
     * @throws IOException If something goes wrong during the execution of the command.
     */
    public List<Device> getConnectedDevices() throws IOException {
        List<Device> devices = new ArrayList<>();
        String rawOutput;
        String line;
        BufferedReader reader;
        
        rawOutput = controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("devices"), true, true));
        
        reader = new BufferedReader(new StringReader(rawOutput));
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("list")) continue;
            if (line.isEmpty()) continue;
            
            devices.add(Device.getDevice(line.split("\\s")[0], this));
        }
        reader.close();
        
        return devices;
    }
    
    /**
     * Executes a given command and either returns the output or an empty string.
     * @param device The device to issue the command to.
     * @param asShell If you wish to execute a <u>shell</u> command, set to true.
     * @param returnOutput Set to <i>true</i> if the the process' output should be returned.
     * @param executable The executable on the device to start. (E.G.: su)
     * @param params The executable's parameters.
     * @return If returnOutput is set to <i>true</i>, the process' output will be returned.
     * @throws IOException If something goes wrong during command/process execution.
     * @throws IllegalArgumentException If one or more arguments are illegal/faulty.
     */
    public String executeCommand(Device device, boolean asShell, boolean returnOutput, String executable, String... params) throws IOException, IllegalArgumentException {
        if (device == null)
            throw new IllegalArgumentException("device must not be null!");
        if (executable == null || executable.isEmpty())
            throw new IllegalArgumentException("executable must not be null or empty!");
        
        List<String> cmd_param = new ArrayList<>();
        if (asShell)
            cmd_param.add("shell");
        cmd_param.add(executable);
        cmd_param.addAll(Arrays.asList(params));
        
        return controller.executeCommand(Command.getCommand(Command.CommandType.ADB_COMMAND, device.getSerial(), cmd_param, returnOutput));
    }
    
    /**
     * Executes a given command and either returns the output or an empty string.
     * @param returnOutput Set to <i>true</i> if the the process' output should be returned.
     * @param executable The executable on the device to start. (E.G.: su)
     * @param params The executable's parameters.
     * @return If returnOutput is set to <i>true</i>, the process' output will be returned.
     * @throws IOException If something goes wrong during command/process execution.
     * @throws IllegalArgumentException If one or more arguments are illegal/faulty.
     */
    public String executeCommand(boolean returnOutput, String executable, String... params) throws IOException, IllegalArgumentException {
        List<String> commands = new ArrayList<>();
        commands.add(executable);
        commands.addAll(Arrays.asList(params));
        return controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, commands, returnOutput, true));
    }
    
    /**
     * Starts the ADB server.
     * @throws IOException If something goes wrong during process execution.
     */
    public void startServer() throws IOException {
        controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("start-server"), false, true));
    }
    
    /**
     * Stops a running ADB server.
     * @throws IOException If something goes wrong during process execution.
     */
    public void stopServer() throws IOException {
        controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("stop-server"), false, true));
    }
    
    /**
     * Restarts the running ADB server.
     * @throws IOException If something goes wrong during process execution.
     */
    public void restartServer() throws IOException {
        stopServer();
        startServer();
    }
    
    /**
     * Attempts to restart the running ADB server into root mode.
     * @throws IOException If something goes wrong during process execution.
     * @throws ServerAlreadyRootedException If the server is already running as root.
     */
    public void rootServer() throws IOException, ServerAlreadyRootedException {
        String rawOutput = 
                controller.executeCommand(Command.getAnonymousCommand(Command.CommandType.ANONYMOUS_COMMAND, Command.convertArrayToList("root"), true, true));
        
        if (rawOutput.toLowerCase().contains("already"))
            throw new ServerAlreadyRootedException(rawOutput);
        
    }

}
