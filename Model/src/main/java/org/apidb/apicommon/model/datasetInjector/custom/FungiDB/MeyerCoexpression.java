package org.apidb.apicommon.model.datasetInjector.custom.FungiDB;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class MeyerCoexpression extends DatasetInjector {

  @Override
  public void injectTemplates() {
    setShortAttribution();

    String projectName = getPropValue("projectName");
    String datasetName = getDatasetName();

    injectTemplate("coexpressionCategory");
    injectTemplate("coexpressionQuestion");
    injectTemplate("coexpressionSource");
  }

  @Override
  public void addModelReferences() {
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByCoexpressionanigCBS513-88_array_microarrayCoexpressionMeyer_RSRC"); 
  }

  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }


}
