package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class MicroarrayTwoChannelDirectComparison extends Microarray {
  
    public void injectTemplates() {
        super.injectTemplates();

        setPropValue("decodeProfileSet", "");
        setPropValue("percentileProfileFilter", "%");

        injectTemplate("microarrayProfileSetParamQuery");
        injectTemplate("microarrayPctProfileSetParamQuery");

        String redPctSampleDecode = makeDecodeMappingStrings(getPropValue("redPctSampleMap"));
        String greenPctSampleDecode = makeDecodeMappingStrings(getPropValue("greenPctSampleMap"));

        setPropValue("redPctSampleDecode", redPctSampleDecode);
        setPropValue("greenPctSampleDecode", greenPctSampleDecode);

        injectTemplate("microarraySamplesParamQueryDirect");
        injectTemplate("microarrayPctSamplesParamQueryDirect");

        if(getPropValueAsBoolean("hasPageData")) {
            injectTemplate("microarrayFoldChangeWithConfidenceQuestionDirect");
            injectTemplate("microarrayFoldChangeWithConfidenceWSDirect");
        } else {
            injectTemplate("microarrayFoldChangeQuestionDirect");
            injectTemplate("microarrayFoldChangeWSDirect");
        }

        injectTemplate("microarrayPercentileWSDirect");
        injectTemplate("microarrayPercentileQuestionDirect");

        injectTemplate("microarraySimpleTwoChannelGraph");
    }


    protected void setExprPlotPartModule() {
        setPropValue("exprPlotPartModule", "LogRatio");
    }


    protected void setExprGraphVisiblePart() {
        setPropValue("exprGraphVisiblePart", "exprn_val");
    }

    protected void setGraphModule() {
        setPropValue("graphModule", "Microarray::TwoChannel");
    }

    public void addModelReferences() {

        if(getPropValueAsBoolean("hasPageData")) {
            //            addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
            //                            "GenesByMicroarray" + getDatasetName() + "Confidence");
        } else {
            //            addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
            //                            "GenesByMicroarray" + getDatasetName());
        }

            //        addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
            //                        "GenesByMicroarray" + getDatasetName() + "Percentile");
    }




    public String[][] getPropertiesDeclaration() {
        String[][] microarrayDeclaration = super.getPropertiesDeclaration();
        
        String [][] declaration = {{"redPctSampleMap", "The ProfileElementName will be Like 'A vs B' ... Need to say whether A or B maps to this channel"},
                                   {"greenPctSampleMap", "The ProfileElementName will be Like 'A vs B' ... Need to say whether A or B maps to this channel"},
        };

        return combinePropertiesDeclarations(microarrayDeclaration, declaration);
    }


}
