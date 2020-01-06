package put.poznan.pl.drsa_sets_assignments;


import put.poznan.pl.drsa_sets_assignments.xmcda.InputsHandler;
import put.poznan.pl.drsa_sets_assignments.xmcda.OutputsHandler;

import java.util.List;

public class Main{

    public static OutputsHandler.Output rankExample(InputsHandler.Inputs inputs){

        List<NetFlowScoreResult> netFlowScoreResults;
        RankRecommendationGenerator recommendationGenerator = new RankRecommendationGenerator();
        netFlowScoreResults = recommendationGenerator.generate(inputs);

        OutputsHandler.Output result = new OutputsHandler.Output();
        result.setNetFlowScoreResults(netFlowScoreResults);
        return result;
    }
}
