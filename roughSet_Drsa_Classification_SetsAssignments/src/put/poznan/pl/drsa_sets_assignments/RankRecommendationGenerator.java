package put.poznan.pl.drsa_sets_assignments;

import put.poznan.pl.drsa_sets_assignments.xmcda.InputsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankRecommendationGenerator {

    public List<NetFlowScoreResult> generate(InputsHandler.Inputs inputs){
        List<NetFlowScoreResult> result = new ArrayList<>();

        for(String variant : inputs.getAlternativesIds()){
            result.add(getNetFlowScoreResultForVariant(inputs.getPreferences_S(), inputs.getPreferences_Sc(), variant));
        }

        return result;
    }

    private NetFlowScoreResult getNetFlowScoreResultForVariant(Map<String, Map<String, Integer>> relationsS,
                                                               Map<String, Map<String, Integer>> relationsSc,
                                                               String variant){
        int weakness = 0;
        int strength = 0;
        for(Map.Entry<String, Map<String, Integer>> relationMap : relationsS.entrySet()){
            if(relationMap.getKey().equals(variant)){
                for(Map.Entry<String, Integer> secondVariantAndRelation : relationMap.getValue().entrySet()){
                    strength++;

                }
            }
            for(Map.Entry<String, Integer> secondVariantAndRelation : relationMap.getValue().entrySet()){
                if(secondVariantAndRelation.getKey().equals(variant)){
                    weakness++;

                }
            }
        }

        for(Map.Entry<String, Map<String, Integer>> relationMap : relationsSc.entrySet()){
            if(relationMap.getKey().equals(variant)){
                for(Map.Entry<String, Integer> secondVariantAndRelation : relationMap.getValue().entrySet()){
                    weakness++;
                }
            }
            for(Map.Entry<String, Integer> secondVariantAndRelation : relationMap.getValue().entrySet()){
                if(secondVariantAndRelation.getKey().equals(variant)){
                    strength++;
                }
            }
        }

        NetFlowScoreResult result = new NetFlowScoreResult(variant, strength, weakness);
        result.printScore();
        return result;
    }

}
