package JDroidLib.android.pm.builder;

/**
 * Created by pedja on 26.3.14..
 */
public class PMListPackagesBuilder extends PMBuilder
{
    // filter must be set last
    // this is used to check if we already specified filter and ignore all other calls if so
    private boolean filterSet = false;

    /**
     * Default constructor<br>
     * Automatically adds "pm list packages"
     * */
    public PMListPackagesBuilder()
    {
        builder.append(" list packages");
    }

    /**
     * filter to only show disabled packages.
     * */
    public PMListPackagesBuilder disabled()
    {
        if (filterSet)return this;
        builder.append(" -d");
        return this;
    }

    /**
     * filter to only show enabled packages.
     * */
    public PMListPackagesBuilder enabled()
    {
        if (filterSet)return this;
        builder.append(" -e");
        return this;
    }

    /**
     * filter to only show system packages.
     * */
    public PMListPackagesBuilder system()
    {
        if (filterSet)return this;
        builder.append(" -s");
        return this;
    }

    /**
     * filter to only show third party packages.
     * */
    public PMListPackagesBuilder thirdParty()
    {
        if (filterSet)return this;
        builder.append(" -3");
        return this;
    }

    /**
     * see the installer for the packages.
     * */
    public PMListPackagesBuilder seeInstaller()
    {
        if (filterSet)return this;
        builder.append(" -i");
        return this;
    }

    /**
     * also include uninstalled packages.
     * */
    public PMListPackagesBuilder includeUninstalled()
    {
        if (filterSet)return this;
        builder.append(" -u");
        return this;
    }

    /**
     * Set username for which to list packages
     * */
    public PMListPackagesBuilder setUser(String userId)
    {
        if(userId == null || userId.isEmpty() || filterSet)
        {
            return this;
        }
        builder.append(" --user ").append(userId);
        return this;
    }

    /**
     * Specify filter that will list only matching packages<br>
     * You can use regular expression. eg. "com.android.*"
     * this method must be called last, all other calls in this class will be ignored after calling this method
     * */
    public PMListPackagesBuilder setFilter(String filter)
    {
        if(filter == null || filter.isEmpty() || filterSet)
        {
            return this;
        }
        builder.append(" ").append(filter);
        filterSet = true;
        return this;
    }
}
