package put.poznan.pl.drsa_interval_assignments;

import put.poznan.pl.drsa_interval_assignments.xmcda.InputsHandler;
import put.poznan.pl.drsa_interval_assignments.xmcda.OutputsHandler;

import java.util.Map;

public class Main{

    public static OutputsHandler.Output sortExample(InputsHandler.Inputs input){

        Map<String, Interval> resultAssignments;
        SortRecommendationGenerator sortGenerator = new SortRecommendationGenerator();
        resultAssignments = sortGenerator.generateRecommendation(input);

        OutputsHandler.Output result = new OutputsHandler.Output();
        result.setBestAssignments(resultAssignments);
        return result;
    }
}
