package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public abstract class MicroarrayOneChannelAndReferenceDesign extends Microarray {
  
    public void injectTemplates() {
        super.injectTemplates();

        String decodeProfileSet = makeDecodeMappingStrings(getPropValue("profileSetNameMap"));
        setPropValue("decodeProfileSet", decodeProfileSet);

        setPercentileProfileFilter();

        injectTemplate("microarrayProfileSetParamQuery");
        injectTemplate("microarrayPctProfileSetParamQuery");

        if(getPropValueAsBoolean("hasPageData")) {
            injectTemplate("microarrayFoldChangeWithConfidenceQuestion");
            injectTemplate("microarrayFoldChangeWithConfidenceWS");
        }
            
        injectTemplate("microarrayFoldChangeQuestion");
        injectTemplate("microarrayFoldChangeWS");

        if(getPropValueAsBoolean("hasPercentileData")) {
            injectTemplate("microarrayPercentileWS");
            injectTemplate("microarrayPercentileQuestion");
        }

        if(getPropValueAsBoolean("hasSimilarityData")) {
            // TODO:  inject ProfileSimilarity Graph
            // TODO:  inject ProfileSimilrity Question
        }

    }

    protected void setPercentileProfileFilter() {
        setPropValue("percentileProfileSetPattern", "%");
    }

    public void addModelReferences() {
	super.addModelReferences();

        if(getPropValueAsBoolean("hasPageData")) {
            addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                            "GeneQuestions.GenesByMicroarray" + getDatasetName() + "Confidence");
        }
        addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                        "GeneQuestions.GenesByMicroarray" + getDatasetName());

        if(getPropValueAsBoolean("hasPercentileData")) {
            addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                            "GeneQuestions.GenesByMicroarray" + getDatasetName() + "Percentile");
        }

        // TODO inject ProfileSimilarity Reference
    }




    public String[][] getPropertiesDeclaration() {
        String[][] microarrayDeclaration = super.getPropertiesDeclaration();
        
        String [][] declaration = {{"isTimeSeries", ""},
                                   {"profileSetNameMap", "Optionally replace profileset names"},
                                   //                                   {"hasSimilarityData", ""},
        };

        return combinePropertiesDeclarations(microarrayDeclaration, declaration);
    }







}
