package put.poznan.pl.drsa_interval_assignments.xmcda;

import java.util.ArrayList;
import java.util.Arrays;

public class IntervalAssignmentsCLI {
    public IntervalAssignmentsCLI(){};

    public static void main(String[] args) throws Exception
    {
        final ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        if ( argsList.remove("--v2") )
        {
            IntervalAssignmentsXMCDAv2.main((String[]) argsList.toArray(new String[]{}));
        }
        else if ( argsList.remove("--v3") )
        {

            IntervalAssignmentsXMCDAv3.main((String[]) argsList.toArray(new String[]{}));
        }
        else
        {
            System.err.println("missing mandatory option --v2 or --v3");
            System.exit(-1);
        }
    }
}
