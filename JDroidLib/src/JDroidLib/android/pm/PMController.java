package JDroidLib.android.pm;

/**
 * Created by pedja on 26.3.14..
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import JDroidLib.android.controllers.ADBController;
import JDroidLib.android.pm.builder.PMBuilder;
import JDroidLib.android.pm.builder.PMInstallPackageBuilder;
import JDroidLib.android.pm.builder.PMListPackagesBuilder;
import JDroidLib.util.ResourceManager;

/**
 * JDroidLib class - Contains methods for manipulating installed applications on the device using <b>pm</b> (Package Manager) binary (/system/bin/pm)
 *
 * @author pedja1
 * @since beta
 *
 */
public class PMController
{
    private ADBController controller;
    private static String help = null;

    /**
     * Default constructor
     * @param controller ADBController, cannot be null
     * */
    public PMController(ADBController controller)
    {
        this.controller = controller;
    }

    /**
     * Get help/usage for <b>pm</b> as String
     * */
    public String getHelp()
    {
        InputStream input = this.getClass().getResourceAsStream("/JDroidLib/res/help/pm_help");
        try
        {
            help = ResourceManager.readStreamToString(input);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return help;
    }

    /**
     * Print <b>pm</b> usage help to output(System.out)
     * */
    public void printHelp()
    {
        System.out.println(getHelp());
    }

    /**
     * execute pm command
     * @param builder use this to build options for package manager. This param cannot be null
     * @param serial optional param to specify serial of the device on which to execute command
     * @return List of packages
     * */
    public String executePMCommand(PMBuilder builder, String serial) throws IOException
    {
        String[] commands = builder.toString().split(" ");
        return controller.executeADBCommand(true, false, serial, commands);
    }

    /**
     * List all packages on a device
     * @param builder use this to build options for package manager. This param cannot be null
     * @param serial optional param to specify serial of the device on which to execute command
     * @return List of packages
     * */
    public List<Package> listPackages(PMListPackagesBuilder builder, String serial) throws IOException
    {
        List<Package> packages = new ArrayList<>();
        String[] cmd = builder.toString().split(" ");

        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null)
        {
            if(line.trim().isEmpty())//ignore empty lines
            {
                continue;
            }
            String[] tmp = line.split(":");
            if(tmp.length < 2)
            {
                continue;
            }
            Package mPackage = new Package();
            mPackage.setPackageName(tmp[1]);
            if(builder.toString().contains("-i"))//we requested that we also show installer
            {
                String[] tmp2 = tmp[1].split("=");
                if(tmp2.length < 2) continue;
                mPackage.setInstaller(tmp2[1]);
            }
            packages.add(mPackage);
        }
        return packages;
    }

    /**
     * Install apk to a device<br>
     * Unlike {@link JDroidLib.android.controllers.ADBController#installApplication(boolean, String, String)}
     * apk file must be located on a device to install<br>
     * @param builder use this to build options for package manager. This param cannot be null
     * @param serial optional param to specify serial of the device on which to execute command
     */
    public String installPackage(PMInstallPackageBuilder builder, String serial) throws IOException
    {
        String[] cmd = builder.toString().split(" ");

        StringBuilder sb = new StringBuilder();
        String raw = controller.executeADBCommand(true, false, serial, cmd);
        BufferedReader reader = new BufferedReader(new StringReader(raw));
        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
