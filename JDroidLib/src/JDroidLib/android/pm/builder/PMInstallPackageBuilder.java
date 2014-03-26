package JDroidLib.android.pm.builder;

/**
 * Created by pedja on 26.3.14..
 */
public class PMInstallPackageBuilder extends PMBuilder
{
    private boolean packageSet = false;

    public PMInstallPackageBuilder()
    {
        builder = new StringBuilder("pm install");
    }

    /**
     * install the package with FORWARD_LOCK.
     * */
    public PMInstallPackageBuilder forwardLock()
    {
        if (packageSet)return this;
        builder.append(" -l");
        return this;
    }

    /**
     * reinstall an existing app, keeping its data.
     * */
    public PMInstallPackageBuilder reinstall()
    {
        if (packageSet)return this;
        builder.append(" -r");
        return this;
    }

    /**
     * allow test .apks to be installed.
     * */
    public PMInstallPackageBuilder allowTest()
    {
        if (packageSet)return this;
        builder.append(" -t");
        return this;
    }

    /**
     * specify the installer package name.
     * */
    public PMInstallPackageBuilder setInstaller(String installerPackageName)
    {
        if(packageSet || installerPackageName == null || installerPackageName.isEmpty())
        {
            return this;
        }
        builder.append(" -i ").append(installerPackageName);
        return this;
    }

    /**
     * install package on sdcard.
     * */
    public PMInstallPackageBuilder installOnSdCard()
    {
        if (packageSet)return this;
        builder.append(" -s");
        return this;
    }

    /**
     * install package on internal flash.
     * */
    public PMInstallPackageBuilder installInternal()
    {
        if (packageSet)return this;
        builder.append(" -f");
        return this;
    }

    /**
     * allow version code downgrade.
     * */
    public PMInstallPackageBuilder allowDowngrade()
    {
        if (packageSet)return this;
        builder.append(" -d");
        return this;
    }

    /**
     * path to a file on a device to be installed
     * */
    public PMInstallPackageBuilder setFile(String fileName)
    {
        if(fileName == null || fileName.isEmpty() || packageSet)
        {
            return this;
        }
        builder.append(fileName);
        return this;
    }
}
