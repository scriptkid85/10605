import java.util.Comparator;


public class MyentryComparator implements Comparator<Myentry>
{
    public int compare(Myentry x, Myentry y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        if (x.getTotal() < y.getTotal())
        {
            return -1;
        }
        if (x.getTotal() > y.getTotal())
        {
            return 1;
        }
        return 0;
    }
}