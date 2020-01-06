package put.poznan.pl.irsa_sets_assignments;

import put.poznan.pl.irsa_sets_assignments.xmcda.InputsHandler;
import put.poznan.pl.irsa_sets_assignments.xmcda.OutputsHandler;

import java.util.List;
import java.util.Map;

public class Main{

    public static OutputsHandler.Output sortExample(InputsHandler.Inputs input){

        Map<String, List<String>> resultAssignments;
        SortRecommendationGenerator sortGenerator = new SortRecommendationGenerator();
        resultAssignments = sortGenerator.generateRecommendation(input);

        OutputsHandler.Output result = new OutputsHandler.Output();
        result.setBestAssignments(resultAssignments);
        return result;
    }
}
