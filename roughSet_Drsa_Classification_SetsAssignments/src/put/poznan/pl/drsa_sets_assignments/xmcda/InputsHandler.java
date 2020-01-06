package put.poznan.pl.drsa_sets_assignments.xmcda;

import org.xmcda.*;
import org.xmcda.utils.Coord;
import put.poznan.pl.drsa_sets_assignments.exceptions.InputDataException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputsHandler {

    private InputsHandler() {

    }

    public static class Inputs {
        private List<String> alternativesIds;
        private Map<String, Map<String, Integer>> preferences_S;
        private Map<String, Map<String, Integer>> preferences_Sc;

        public List<String> getAlternativesIds() {
            return alternativesIds;
        }
        public Map<String, Map<String, Integer>> getPreferences_S() {
            return preferences_S;
        }
        public Map<String, Map<String, Integer>> getPreferences_Sc() {
            return preferences_Sc;
        }

        public void setAlternativesIds(List<String> alternativesIds) {
            this.alternativesIds = alternativesIds;
        }
        public void setPreferences_S(Map<String, Map<String, Integer>> preferences_S) {
            this.preferences_S = preferences_S;
        }
        public void setPreferences_Sc(Map<String, Map<String, Integer>> preferences_Sc) {
            this.preferences_Sc = preferences_Sc;
        }
    }

    public static Inputs checkAndExtractInputs(XMCDA xmcda, XMCDA xmcda2, ProgramExecutionResult xmcdaExecResults) {
        Inputs inputsDict = checkInputs(xmcda, xmcda2, xmcdaExecResults);
        if (xmcdaExecResults.isError())
            return null;
        return inputsDict;
    }

    protected static Inputs checkInputs(XMCDA xmcda, XMCDA xmcda2, ProgramExecutionResult errors) {
        Inputs inputs = new Inputs();

        try {
            checkAndExtractAlternatives(inputs, xmcda, errors);
            checkAndExtractPreferences(inputs, xmcda, xmcda2, errors);
        } catch (InputDataException exception) {
            //Just catch the exceptions and skip other functions
        }

        return inputs;
    }

    protected static void checkAndExtractAlternatives(Inputs inputs, XMCDA xmcda, ProgramExecutionResult errors) throws InputDataException {
        if (xmcda.alternatives.isEmpty()) {
            String errorMessage = "No alternatives list has been supplied.";
            errors.addError(errorMessage);
            throw new InputDataException(errorMessage);
        }

        List<String> alternativesIds = xmcda.alternatives.getActiveAlternatives().stream()
                .filter(a -> "alternatives".equals(a.getMarker())).map(Alternative::id)
                .collect(Collectors.toList());
        if (alternativesIds.isEmpty())
            errors.addError("The alternatives list can not be empty.");
        inputs.setAlternativesIds(alternativesIds);
    }

    private static void extractPreferences(AlternativesMatrix<Integer> matrix,
                                           Inputs inputs, Map<String, Map<String, Integer>> result){
        System.out.println("DDD");
        for (String a : inputs.getAlternativesIds()) {
            Map<String, Integer> mapSecondAlternativeResultS = new HashMap<>();
            for (String b : inputs.getAlternativesIds()) {
                Alternative altA = new Alternative(a);
                Alternative altB = new Alternative(b);
                Coord<Alternative, Alternative> coord = new Coord<>(altA, altB);
                if (matrix.containsKey(coord)) {
                    Integer value = matrix.get(coord).get(0).getValue();
                    mapSecondAlternativeResultS.put(b, value);
                    System.out.println(altA.id() + " " + altB.id());
                }
            }
            if(!mapSecondAlternativeResultS.isEmpty())
                result.put(a, mapSecondAlternativeResultS);
        }
    }

    protected static void checkAndExtractPreferences(Inputs inputs, XMCDA xmcda, XMCDA xmcda2, ProgramExecutionResult errors) throws InputDataException {

        AlternativesMatrix<Integer> resultS;
        AlternativesMatrix<Integer> resultSc;

        if(xmcda.alternativesMatricesList != null){

            if(xmcda.alternativesMatricesList.size() == 1)
                resultS = (AlternativesMatrix<Integer>) xmcda.alternativesMatricesList.get(0);
            else if(xmcda.alternativesMatricesList.size() > 2) {
                String errorMessage = "Too much preference lists (S) has been supplied";
                errors.addError(errorMessage);
                throw new InputDataException(errorMessage);
            }
            else
                resultS = new AlternativesMatrix<>();
        }
        else
            resultS = new AlternativesMatrix<>();

        if(xmcda2.alternativesMatricesList != null){
            if(xmcda2.alternativesMatricesList.size() == 1)
                resultSc =  (AlternativesMatrix<Integer>) xmcda2.alternativesMatricesList.get(0);
            else if(xmcda.alternativesMatricesList.size() > 2) {
                String errorMessage = "Too much preference lists (Sc) has been supplied";
                errors.addError(errorMessage);
                throw new InputDataException(errorMessage);
            }
            else
                resultSc = new AlternativesMatrix<>();
        }
        else
            resultSc = new AlternativesMatrix<>();

        Map<String, Map<String, Integer>> mapResultS = new HashMap<>();
        Map<String, Map<String, Integer>> mapResultSc = new HashMap<>();
        extractPreferences(resultS, inputs, mapResultS);
        extractPreferences(resultSc, inputs, mapResultSc);

        inputs.setPreferences_S(mapResultS);
        inputs.setPreferences_Sc(mapResultSc);

        System.out.println("=================S");

        for(Map.Entry<String, Map<String, Integer>> tmpResultS: mapResultS.entrySet()){
            for(Map.Entry<String, Integer> tmpSecondResultS : tmpResultS.getValue().entrySet()){
                System.out.println(tmpResultS.getKey() + " " + tmpSecondResultS.getKey() + " " + tmpSecondResultS.getValue());
            }
        }

        System.out.println("=================Sc");

        for(Map.Entry<String, Map<String, Integer>> tmpResultSc: mapResultSc.entrySet()){
            for(Map.Entry<String, Integer> tmpSecondResultSc : tmpResultSc.getValue().entrySet()){
                System.out.println(tmpResultSc.getKey() + " " + tmpSecondResultSc.getKey() + " " + tmpSecondResultSc.getValue());
            }
        }

    }

}
