package put.poznan.pl.drsa_sets_assignments.xmcda;

import java.util.ArrayList;
import java.util.Arrays;

public class SetsAssignmentsCLI {
    public SetsAssignmentsCLI(){};

    public static void main(String[] args) throws Exception
    {
        final ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));
        if ( argsList.remove("--v2") )
        {
            SetsAssignmentsXMCDAv2.main((String[]) argsList.toArray(new String[]{}));
        }
        else if ( argsList.remove("--v3") )
        {
            SetsAssignmentsXMCDAv3.main((String[]) argsList.toArray(new String[]{}));
        }
        else
        {
            System.err.println("missing mandatory option --v2 or --v3");
            System.exit(-1);
        }
    }
}
