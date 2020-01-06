package put.poznan.pl.irsa_sets_assignments.xmcda;

import org.xmcda.*;
import java.util.*;

public class OutputsHandler {

    private static final String ASSIGNMENTS = "assignments";
    private static final String CATEGORIES_SETS = "categoriesSets";
    private static final String MESSAGES = "messages";

    private OutputsHandler() { }

    public static class Output{
        private  Map<String, List<String>> bestAssignments;

        public Map<String, List<String>> getBestAssignments() {
            return bestAssignments;
        }
        public void setBestAssignments(Map<String, List<String>> bestAssignments){
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

    public static Map<String, XMCDA> convert(Map<String, List<String>> bestClasses)
    {
        final HashMap<String, XMCDA> xResults = new HashMap<>();

        XMCDA results = new XMCDA();

        AlternativesAssignments alternativesAssignments = new AlternativesAssignments();
        CategoriesSets<Double>  categoriesSets = new CategoriesSets<>();
        for(Map.Entry<String, List<String>> mapEntry: bestClasses.entrySet()){
            AlternativeAssignment tmpAssignment = new AlternativeAssignment();
            tmpAssignment.setAlternative(new Alternative(mapEntry.getKey()));

            CategoriesSet<Double> categoriesSet = new CategoriesSet<>();
            categoriesSet.setId("CS_"+mapEntry.getKey());

            for(String className: mapEntry.getValue()){
                Category category = new Category(className);
                QualifiedValues<Double> valueSet = new QualifiedValues<>();
                QualifiedValue<Double> value = new QualifiedValue<>();
                value.setValue(1.0);
                valueSet.add(value);

                categoriesSet.put(category, valueSet);
                categoriesSets.add(categoriesSet);
                results.categories.add(new Category(className));
            }

            tmpAssignment.setCategoriesSet(categoriesSet);
            alternativesAssignments.add(tmpAssignment);

        }
        results.categoriesSets = categoriesSets;
        results.alternativesAssignmentsList.add(alternativesAssignments);
        xResults.put(ASSIGNMENTS, results);
        xResults.put(CATEGORIES_SETS, results);
        return xResults;
    }
}
