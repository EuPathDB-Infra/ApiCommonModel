package org.apidb.apicommon.model.datasetInjector.custom.PlasmoDB;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class Y2HInteractions extends DatasetInjector {

  public void injectTemplates() {
  }

  public void addModelReferences() {
    addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesbyProteinProteinInteraction");
    addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "Y2hInteractions");
  }

  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }



}
