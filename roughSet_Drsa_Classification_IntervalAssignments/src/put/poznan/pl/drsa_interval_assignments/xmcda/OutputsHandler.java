package put.poznan.pl.drsa_interval_assignments.xmcda;

import org.xmcda.*;
import put.poznan.pl.drsa_interval_assignments.Interval;

import java.util.*;

public class OutputsHandler {

    private static final String ASSIGNMENTS = "assignments";
    private static final String CATEGORIES_SETS = "categoriesSets";
    private static final String MESSAGES = "messages";

    private OutputsHandler() { }

    public static class Output{
        private  Map<String, Interval> bestAssignments;

        public Map<String, Interval> getBestAssignments() {
            return bestAssignments;
        }
        public void setBestAssignments(Map<String, Interval> bestAssignments){
            this.bestAssignments = bestAssignments;
        }
    }

    public static final String xmcdaV3Tag(String outputName)
    {
        switch(outputName)
        {
            case ASSIGNMENTS:
                return "alternativesAssignments";
            case CATEGORIES_SETS:
                return "categoriesSets";
            case MESSAGES:
                return "programExecutionResult";
            default:
                throw new IllegalArgumentException(String.format("Unknown output name '%s'",outputName));
        }
    }

    public static final String xmcdaV2Tag(String outputName)
    {
        switch(outputName)
        {
            case ASSIGNMENTS:
                return "alternativesAffectations";
            case CATEGORIES_SETS:
                return "categoriesSets";
            case MESSAGES:
                return "methodMessages";
            default:
                throw new IllegalArgumentException(String.format("Unknown output name '%s'",outputName));
        }
    }

    public static Map<String, XMCDA> convert(Map<String, Interval> bestClasses)
    {
        final HashMap<String, XMCDA> xResults = new HashMap<>();

        XMCDA results = new XMCDA();

        AlternativesAssignments alternativesAssignments = new AlternativesAssignments();
        CategoriesSets<Double>  categoriesSets = new CategoriesSets<>();
        for(Map.Entry<String, Interval> mapEntry: bestClasses.entrySet()){
            AlternativeAssignment tmpAssignment = new AlternativeAssignment();
            tmpAssignment.setAlternative(new Alternative(mapEntry.getKey()));

            CategoriesInterval categoriesInterval = new CategoriesInterval();
            categoriesInterval.setLowerBound(new Category(bestClasses.get(mapEntry.getKey()).downwardInterval));
            categoriesInterval.setUpperBound(new Category(bestClasses.get(mapEntry.getKey()).upperInterval));

            tmpAssignment.setCategoryInterval(categoriesInterval);
            alternativesAssignments.add(tmpAssignment);

        }
        results.alternativesAssignmentsList.add(alternativesAssignments);
        xResults.put(ASSIGNMENTS, results);

        return xResults;
    }
}
