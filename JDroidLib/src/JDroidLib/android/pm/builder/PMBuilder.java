package JDroidLib.android.pm.builder;

/**
 * Created by pedja on 26.3.14..
 */
public class PMBuilder
{
    protected StringBuilder builder = new StringBuilder("pm");

    public void runAsRoot(boolean runAsRoot)
    {
        builder.insert(0, "su -c ");
    }

    @Override
    public String toString()
    {
        return builder.toString();
    }
}
