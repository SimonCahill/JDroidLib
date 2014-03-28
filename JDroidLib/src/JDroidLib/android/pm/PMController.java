package JDroidLib.android.pm;

/**
 * Created by pedja on 26.3.14..
 */
import java.io.*;
import java.util.*;

import JDroidLib.android.controllers.ADBController;
import JDroidLib.android.pm.builder.*;
import JDroidLib.util.ResourceManager;

/**
 * JDroidLib class - Contains methods for manipulating installed applications on
 * the device using <b>pm</b> (Package Manager) binary (/system/bin/pm)
 *
 * @author pedja1
 * @since beta
 *
 */
public class PMController {

    private final ADBController controller;
    private static String help = null;

    /**
     * Default constructor
     *
     * @param controller ADBController, cannot be null
     *
     */
    public PMController(ADBController controller) {
        this.controller = controller;
    }

    /**
     * Get help/usage for <b>pm</b> as String
     *
     * @return
     */
    public String getUsage() {
        InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/help/pm_help");
        if (help == null) {
            try {
                help = ResourceManager.readStreamToString(input);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return help;
    }

    /**
     * Print <b>pm</b> usage help to output(System.out)
     *
     */
    public void printHelp() {
        System.out.println(getUsage());
    }

    /**
     * execute pm command
     *
     * @param builder use this to build options for package manager. This param
     * cannot be null
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @return List
     * @throws java.io.IOException of packages
     *
     */
    public String executePMCommand(PMBuilder builder, String serial) throws IOException {
        String[] commands = builder.toString().split(" ");
        return controller.executeADBCommand(true, false, serial, commands);
    }

    /**
     * List all packages on a device
     *
     * @return List of packages
     * @throws java.io.IOException
     *
     */
    public List<Package> listPackages() throws IOException {
        return listPackages(new PMListPackagesBuilder(), null);
    }

    /**
     * List all packages on a device
     *
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @return List of packages
     * @throws java.io.IOException
     *
     */
    public List<Package> listPackages(String serial) throws IOException {
        return listPackages(new PMListPackagesBuilder(), serial);
    }

    /**
     * List all packages on a device
     *
     * @param builder use this to build options for package manager. This param
     * cannot be null
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @return List of packages
     *
     */
    public List<Package> listPackages(PMListPackagesBuilder builder, String serial) throws IOException {
        List<Package> packages = new ArrayList<>();
        String[] cmd = builder.toString().split(" ");

        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) { //ignore empty lines 
                continue;
            }
            String[] tmp = line.split(":");
            if (tmp.length < 2) {
                continue;
            }
            Package mPackage = new Package();
            mPackage.setPackageName(tmp[1]);
            if (builder.toString().contains("-i")) { //we requested that we also show installer 
                String[] tmp2 = tmp[1].split("=");
                if (tmp2.length < 2) {
                    continue;
                }
                mPackage.setInstaller(tmp2[1]);
            }
            packages.add(mPackage);
        }
        return packages;
    }

    /**
     * Install apk to a device<br>
     * Unlike
     * {@link JDroidLib.android.controllers.ADBController#installApplication(boolean, String, String)}
     * apk file must be located on a device to install<br>
     *
     * @param builder use this to build options for package manager. This param
     * cannot be null
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @return
     * @throws java.io.IOException
     */
    public String installPackage(PMInstallPackageBuilder builder, String serial) throws IOException {
        String[] cmd = builder.toString().split(" ");

        StringBuilder sb = new StringBuilder();
        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Install apk to a device<br>
     * Unlike
     * {@link JDroidLib.android.controllers.ADBController#installApplication(boolean, String, String)}
     * apk file must be located on a device to install<br>
     *
     * @param builder use this to build options for package manager. This param
     * cannot be null
     * @return
     * @throws java.io.IOException
     */
    public String installPackage(PMInstallPackageBuilder builder) throws IOException {
        return installPackage(builder, null);
    }

    /**
     * Uninstall application<br>
     *
     * @param runAsRoot
     * @param keepData keep the data and cache directories around after package
     * removal.
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String uninstallPackage(boolean runAsRoot, boolean keepData, String serial, String packageName) throws IOException {
        String[] cmd = {runAsRoot ? "su -c " : "", "pm", "uninstall", keepData ? "-k" : "", packageName};

        StringBuilder sb = new StringBuilder();
        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Uninstall application<br>
     *
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String uninstallPackage(String serial, String packageName) throws IOException {
        return uninstallPackage(false, false, serial, packageName);
    }

    /**
     * Uninstall application<br>
     *
     * @param runAsRoot
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String uninstallPackage(boolean runAsRoot, String serial, String packageName) throws IOException {
        return uninstallPackage(runAsRoot, false, serial, packageName);
    }

    /**
     * Uninstall application<br>
     *
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String uninstallPackage(String packageName) throws IOException {
        return uninstallPackage(false, null, packageName);
    }

    /**
     * deletes all data associated with a package.<br>
     *
     * @param userId optional. Id of the user for which to clear data
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @param runAsRoot
     * @return
     * @throws java.io.IOException
     */
    public String clearData(String packageName, String serial, String userId, boolean runAsRoot) throws IOException {
        boolean userIdValid = userId != null && !userId.isEmpty();
        String[] cmd = {runAsRoot ? "su -c " : "", "pm", "clear", userIdValid ? "--user" : "", userIdValid ? userId : "", packageName};

        StringBuilder sb = new StringBuilder();
        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * deletes all data associated with a package.<br>
     *
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String clearData(String packageName, String serial) throws IOException {
        return clearData(packageName, serial, null, false);
    }

    /**
     * deletes all data associated with a package.<br>
     *
     * @param serial optional param to specify serial of the device on which to
     * execute command
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @param runAsRoot
     * @return
     * @throws java.io.IOException
     */
    public String clearData(String packageName, String serial, boolean runAsRoot) throws IOException {
        return clearData(packageName, serial, null, runAsRoot);
    }

    /**
     * deletes all data associated with a package.<br>
     *
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @return
     * @throws java.io.IOException
     */
    public String clearData(String packageName) throws IOException {
        return clearData(packageName, null, null, false);
    }

    /**
     * deletes all data associated with a package.<br>
     *
     * @param packageName package name of the application to uninstall (eg.
     * com.example.app)
     * @param runAsRoot
     * @return
     * @throws java.io.IOException
     */
    public String clearData(String packageName, boolean runAsRoot) throws IOException {
        return clearData(packageName, null, null, runAsRoot);
    }
}
