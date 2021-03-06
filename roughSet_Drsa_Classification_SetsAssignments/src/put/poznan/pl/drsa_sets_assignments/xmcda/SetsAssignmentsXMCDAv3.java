package put.poznan.pl.drsa_sets_assignments.xmcda;

import org.xmcda.ProgramExecutionResult;
import org.xmcda.Referenceable;
import org.xmcda.XMCDA;
import put.poznan.pl.drsa_sets_assignments.Main;

import java.io.File;
import java.util.Map;

/**
 * Created by Maciej Uniejewski on 2016-11-01.
 */
public class SetsAssignmentsXMCDAv3 {

    private SetsAssignmentsXMCDAv3() {

    }

    private static void readFiles(XMCDA xmcda, XMCDA xmcda2, String indir, ProgramExecutionResult executionResult) {
        Referenceable.DefaultCreationObserver.currentMarker="alternatives";
        Utils.loadXMCDAv3(xmcda, new File(indir, "alternatives.xml"), true, executionResult, "alternatives");
        Referenceable.DefaultCreationObserver.currentMarker = "alternativesMatrix";
        Utils.loadXMCDAv3(xmcda, new File(indir, "preferences_S.xml"), true, executionResult, "alternativesMatrix");
        Referenceable.DefaultCreationObserver.currentMarker = "alternativesMatrix";
        Utils.loadXMCDAv3(xmcda2, new File(indir, "preferences_Sc.xml"), true, executionResult, "alternativesMatrix");
    }

    public static void main(String[] args) throws Utils.InvalidCommandLineException {
        final Utils.Arguments params = Utils.parseCmdLineArguments(args);

        final String indir = params.inputDirectory;
        final String outdir = params.outputDirectory;

        final File prgExecResults = new File(outdir, "messages.xml");

        final ProgramExecutionResult executionResult = new ProgramExecutionResult();

        final XMCDA xmcda = new XMCDA();
        final XMCDA xmcda2 = new XMCDA();

        readFiles(xmcda, xmcda2, indir, executionResult);


        if ( ! (executionResult.isOk() || executionResult.isWarning() ) ) {
            Utils.writeProgramExecutionResultsAndExit(prgExecResults, executionResult, Utils.XMCDA_VERSION.v3);
        }

        final InputsHandler.Inputs inputs = InputsHandler.checkAndExtractInputs(xmcda, xmcda2, executionResult);

        if ( ! ( executionResult.isOk() || executionResult.isWarning() ) || inputs == null ) {
            Utils.writeProgramExecutionResultsAndExit(prgExecResults, executionResult, Utils.XMCDA_VERSION.v3);
        }

        final OutputsHandler.Output results;
        try
        {
            results = Main.rankExample(inputs);
        }
        catch (Exception e) {
            executionResult.addError(Utils.getMessage("The calculation could not be performed, reason: ", e));
            Utils.writeProgramExecutionResultsAndExit(prgExecResults, executionResult, Utils.XMCDA_VERSION.v3);
            return;
        }

        Map<String, XMCDA> xResults = OutputsHandler.convert(results.getNetFlowScoreResults());

        final org.xmcda.parsers.xml.xmcda_v3.XMCDAParser parser = new org.xmcda.parsers.xml.xmcda_v3.XMCDAParser();

        for ( Map.Entry<String, XMCDA> entry : xResults.entrySet() )
        {
            File outputFile = new File(outdir, String.format("%s.xml", entry.getKey()));
            try
            {
                parser.writeXMCDA(entry.getValue(), outputFile, OutputsHandler.xmcdaV3Tag(entry.getKey()));
            }
            catch (Exception e)
            {
                final String err = String.format("Error while writing %s.xml, reason: ", entry.getKey());
                executionResult.addError(Utils.getMessage(err, e));
                outputFile.delete();
            }
        }

        Utils.writeProgramExecutionResultsAndExit(prgExecResults, executionResult, Utils.XMCDA_VERSION.v3);
    }
}
