package put.poznan.pl.drsa_interval_assignments.xmcda;

import org.xmcda.*;
import put.poznan.pl.drsa_interval_assignments.exceptions.InputDataException;

import java.util.*;

public class InputsHandler {

    private InputsHandler() {

    }

    public static class Inputs {

        private List<String> alternativesIds;
        private Map<String, Map<String, Integer>> classSupports;
        private Map<String, Integer> categoriesValues;
        private List<String> categoriesIds;

        public List<String> getAlternativesIds() {
            return alternativesIds;
        }
        public Map<String, Map<String, Integer>> getClassSupports() {
            return classSupports;
        }
        public Map<String, Integer> getCategoriesValues(){ return categoriesValues; }
        public List<String> getCategoriesIds(){ return categoriesIds; }

        public void setAlternativesIds(List<String> alternativesIds) {
            this.alternativesIds = alternativesIds;
        }
        public void setClassSupports(Map<String, Map<String, Integer>> classSupports) {
            this.classSupports = classSupports;
        }
        public void setCategoriesValues(Map<String, Integer> categoriesValues){ this.categoriesValues = categoriesValues;}
        public void setCategoriesIds(List<String> categoriesIds){ this.categoriesIds = categoriesIds; }
    }

    public static Inputs checkAndExtractInputs(XMCDA xmcda, ProgramExecutionResult xmcdaExecResults) {
        Inputs inputsDict = checkInputs(xmcda, xmcdaExecResults);
        if (xmcdaExecResults.isError())
            return null;
        return inputsDict;
    }

    protected static Inputs checkInputs(XMCDA xmcda, ProgramExecutionResult errors) {
        Inputs inputs = new Inputs();

        try {
            checkAndExtractAssignments(inputs, xmcda, errors);
            checkAndExtractCategories(inputs, xmcda, errors);
            checkCategoriesRanking(inputs, xmcda, errors);
        } catch (InputDataException exception) {
            //Just catch the exceptions and skip other functions
        }

        return inputs;
    }

    protected static void checkAndExtractAssignments(Inputs inputs, XMCDA xmcda, ProgramExecutionResult errors) throws InputDataException {

        if (xmcda.alternativesAssignmentsList.isEmpty()) {

            String errorMessage = "No alternatives assignments list has been supplied";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        if (xmcda.alternativesAssignmentsList.size() > 1) {

            String errorMessage = "More then one assignments list has been detected";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        AlternativesAssignments alternativesAssignments = xmcda.alternativesAssignmentsList.get(0);
        Map<String, Map<String, Integer>> assignmentsMap = new HashMap<>();

        for(Object assignmentObject : alternativesAssignments){
            AlternativeAssignment assignment = (AlternativeAssignment) assignmentObject;
            Map<String, Integer> assignmentsSupport;
            if(assignmentsMap.get(assignment.getAlternative().id()) == null){
                assignmentsSupport = new HashMap<>();
            }
            else
                assignmentsSupport = assignmentsMap.get(assignment.getAlternative().id());

            assignmentsSupport.put(assignment.getCategory().id(),
                    ((QualifiedValue<Integer>)assignment.getValues().get(0)).getValue());

            assignmentsMap.put(assignment.getAlternative().id(), assignmentsSupport);
        }
        inputs.setClassSupports(assignmentsMap);
    }

    protected static void checkCategoriesRanking(Inputs inputs, XMCDA xmcda, ProgramExecutionResult errors) throws InputDataException {
        if (xmcda.categoriesValuesList.isEmpty()) {
            String errorMessage = "No categories values list has been supplied";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        if (xmcda.categoriesValuesList.size() > 1) {
            String errorMessage = "More than one categories values list has been supplied";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        CategoriesValues categoriesValuesList = xmcda.categoriesValuesList.get(0);
        if (!categoriesValuesList.isNumeric()) {
            String errorMessage = "Each of the categories ranks must be integer";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        Map<String, Integer> categoriesValues = new LinkedHashMap<>();
        try {
            CategoriesValues<Integer> categoriesValuesClass = categoriesValuesList.convertTo(Integer.class);

            xmcda.categoriesValuesList.set(0, categoriesValuesClass);

            int min = Integer.MAX_VALUE;
            int max = -1;

            for (Map.Entry<Category, LabelledQValues<Integer>> a : categoriesValuesClass.entrySet()) {
                if (a.getValue().get(0).getValue() < min) {
                    min = a.getValue().get(0).getValue();
                }
                if (a.getValue().get(0).getValue() > max) {
                    max = a.getValue().get(0).getValue();
                }
                categoriesValues.put(a.getKey().id(), a.getValue().get(0).getValue());
            }

            if (min != 1) {
                String errorMessage = "Minimal rank should be equal to 1.";
                errors.addError(errorMessage);
                throw new InputDataException(errorMessage);
            }

            if (max != inputs.getCategoriesIds().size()) {
                String errorMessage = "Maximal rank should be equal to number of categories.";
                errors.addError(errorMessage);
                throw new InputDataException(errorMessage);
            }

            for (Map.Entry<String, Integer> categoryA : categoriesValues.entrySet()) {
                for (Map.Entry<String, Integer> categoryB : categoriesValues.entrySet()) {
                    if (categoryA.getValue().intValue() == categoryB.getValue() && !categoryA.getKey().equals(categoryB.getKey())) {
                        String errorMessage = "There can not be two categories with the same rank.";
                        errors.addError(errorMessage);
                        throw new InputDataException(errorMessage);
                    }
                }
            }

            inputs.setCategoriesValues(categoriesValues);

        } catch (InputDataException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "An error occurred. Remember that each rank has to be integer.";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }
    }

    protected static void checkAndExtractCategories(Inputs inputs, XMCDA xmcda, ProgramExecutionResult errors) throws InputDataException {
        if (xmcda.categories.isEmpty()) {
            String errorMessage = "No categories has been supplied.";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        if (xmcda.categories.size() == 1) {
            String errorMessage = "You should supply at least 2 categories.";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        List<String> categories = new ArrayList<>();

        for(Category category : xmcda.categories.getActiveCategories()) {
            categories.add(category.id());
        }

        inputs.setCategoriesIds(categories);
        if (categories.isEmpty()) {
            String errorMessage = "The category list can not be empty.";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }
    }

}
