package put.poznan.pl.irsa_sets_assignments.xmcda;

import org.xmcda.*;
import put.poznan.pl.irsa_sets_assignments.exceptions.InputDataException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputsHandler {

    private InputsHandler() {

    }

    public static class Inputs {

        private List<String> alternativesIds;
        private Map<String, Map<String, Double>> classSupports;

        public List<String> getAlternativesIds() {
            return alternativesIds;
        }
        public Map<String, Map<String, Double>> getClassSupports() {
            return classSupports;
        }

        public void setAlternativesIds(List<String> alternativesIds) {
            this.alternativesIds = alternativesIds;
        }
        public void setClassSupports(Map<String, Map<String, Double>> classSupports) {
            this.classSupports = classSupports;
        }
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
        Map<String, Map<String, Double>> assignmentsMap = new HashMap<>();

        for(Object assignmentObject : alternativesAssignments){
            AlternativeAssignment assignment = (AlternativeAssignment) assignmentObject;
            Map<String, Double> assignmentsSupport;
            if(assignmentsMap.get(assignment.getAlternative().id()) == null){
                assignmentsSupport = new HashMap<>();
            }
            else
                assignmentsSupport = assignmentsMap.get(assignment.getAlternative().id());

            assignmentsSupport.put(assignment.getCategory().id(),
                    ((QualifiedValue<Double>)assignment.getValues().get(0)).getValue());

            assignmentsMap.put(assignment.getAlternative().id(), assignmentsSupport);
        }
        inputs.setClassSupports(assignmentsMap);
    }
}
