package JDroidLib.android.pm;

/**
 * Created by pedja on 26.3.14..
 */
public class Package
{
    String packageName;
    String installer;

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getInstaller()
    {
        return installer;
    }

    public void setInstaller(String installer)
    {
        this.installer = installer;
    }

    @Override
    public String toString()
    {
        return packageName + (installer != null ? " [installer: " + installer + "]" : "");
    }
}
