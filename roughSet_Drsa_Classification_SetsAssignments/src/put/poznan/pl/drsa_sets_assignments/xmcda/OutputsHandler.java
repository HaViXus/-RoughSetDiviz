package put.poznan.pl.drsa_sets_assignments.xmcda;

import org.xmcda.*;
import org.xmcda.v2.AlternativeValue;
import put.poznan.pl.drsa_sets_assignments.NetFlowScoreResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputsHandler {

    private static final String SCORES = "alternativesValues";
    private static final String MESSAGES = "messages";

    private OutputsHandler() {

    }

    public static class Output{
        private List<NetFlowScoreResult> netFlowScoreResults;

        public List<NetFlowScoreResult> getNetFlowScoreResults() {
            return netFlowScoreResults;
        }
        public void setNetFlowScoreResults(List<NetFlowScoreResult> netFlowScoreResults){
            this.netFlowScoreResults = netFlowScoreResults;
        }
    }

    public static final String xmcdaV3Tag(String outputName)
    {
        switch(outputName)
        {
            case SCORES:
                return "alternativesValues";
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
            case SCORES:
                return "alternativesValues";
            case MESSAGES:
                return "methodMessages";
            default:
                throw new IllegalArgumentException(String.format("Unknown output name '%s'",outputName));
        }
    }

    public static Map<String, XMCDA> convert(List<NetFlowScoreResult> netFlowScoreResults)
    {
        final HashMap<String, XMCDA> xResults = new HashMap<>();

        XMCDA results = new XMCDA();

        AlternativesValues<Double> alternativesValues = new AlternativesValues();

        for (NetFlowScoreResult netResult : netFlowScoreResults) {
            AlternativeValue alternativeValue = new AlternativeValue();
            alternativeValue.setAlternativeID(netResult.variant);
            QualifiedValue<Double> score = new QualifiedValue<>();
            score.setValue((double) netResult.quality);
            alternativesValues.put(new Alternative(netResult.variant), score);
        }

        results.alternativesValuesList.add(alternativesValues);

        xResults.put(SCORES, results);
        return xResults;
    }
}
