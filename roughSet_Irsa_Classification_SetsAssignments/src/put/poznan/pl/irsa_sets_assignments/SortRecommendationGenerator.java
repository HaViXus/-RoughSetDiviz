package put.poznan.pl.irsa_sets_assignments;

import put.poznan.pl.irsa_sets_assignments.xmcda.InputsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortRecommendationGenerator {

    public SortRecommendationGenerator(){}

    public Map<String, List<String>> generateRecommendation (InputsHandler.Inputs inputs){
        Map<String, Map<String, Double>> classAssignments = inputs.getClassSupports();
        Map<String, List<String>> resultAssignments = new HashMap<>();

        for(Map.Entry<String, Map<String, Double>> classAssignment : classAssignments.entrySet()){
            double bestSupport = Double.MIN_VALUE;
            List<String> bestCategories = new ArrayList<>();

            for(Map.Entry<String, Double> classSupport: classAssignment.getValue().entrySet()){
                if(classSupport.getValue() > bestSupport){
                    bestSupport = classSupport.getValue();
                    bestCategories.clear();
                    bestCategories.add(classSupport.getKey());
                }
                else if(classSupport.getValue() == bestSupport){
                    bestCategories.add(classSupport.getKey());
                }
            }
            resultAssignments.put(classAssignment.getKey(), bestCategories);
        }

        return resultAssignments;
    }
}
