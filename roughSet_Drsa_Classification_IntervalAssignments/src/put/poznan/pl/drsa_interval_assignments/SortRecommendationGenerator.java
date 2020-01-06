package put.poznan.pl.drsa_interval_assignments;

import put.poznan.pl.drsa_interval_assignments.xmcda.InputsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortRecommendationGenerator {

    public SortRecommendationGenerator(){}

    public Map<String, Interval> generateRecommendation (InputsHandler.Inputs inputs){
        Map<String, Map<String, Integer>> classAssignments = inputs.getClassSupports();
        Map<String, Interval> resultAssignments = new HashMap<>();

        for(Map.Entry<String, Map<String, Integer>> classAssignment : classAssignments.entrySet()){
            Integer bestSupport = Integer.MIN_VALUE;

            List<String> bestCategories = new ArrayList<>();

            for(Map.Entry<String, Integer> classSupport: classAssignment.getValue().entrySet()){
                if(classSupport.getValue() > bestSupport){
                    bestSupport = classSupport.getValue();
                    bestCategories.clear();
                    bestCategories.add(classSupport.getKey());
                }
                else if(classSupport.getValue() == bestSupport){
                    bestCategories.add(classSupport.getKey());
                }
            }

            Interval interval = new Interval();
            int downwardIntervalSupportRank = Integer.MAX_VALUE;
            int upperIntervalSupportRank = Integer.MIN_VALUE;

            Map<String, Integer> categoriesValues = inputs.getCategoriesValues();

            for(String categoryID : bestCategories){
                if(categoriesValues.get(categoryID).intValue() < downwardIntervalSupportRank){
                    downwardIntervalSupportRank = categoriesValues.get(categoryID).intValue();
                    interval.downwardInterval = categoryID;
                }
                if(categoriesValues.get(categoryID).intValue() > upperIntervalSupportRank) {
                    upperIntervalSupportRank = categoriesValues.get(categoryID).intValue();
                    interval.upperInterval = categoryID;
                }
            }

            resultAssignments.put(classAssignment.getKey(), interval);
        }

        return resultAssignments;
    }
}
