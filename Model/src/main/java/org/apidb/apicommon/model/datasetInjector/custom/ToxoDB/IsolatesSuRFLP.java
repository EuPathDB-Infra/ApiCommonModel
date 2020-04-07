package org.apidb.apicommon.model.datasetInjector.custom.ToxoDB;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class IsolatesSuRFLP extends DatasetInjector {

  @Override
  public void injectTemplates() {
  }

  @Override
  public void addModelReferences() {
      addWdkReference("RflpIsolateRecordClasses.RflpIsolateRecordClass", "question", "RflpIsolateQuestions.ByRFLPGenotype"); 
      addWdkReference("RflpIsolateRecordClasses.RflpIsolateRecordClass", "question", "RflpIsolateQuestions.ByGenotypeNumber"); 
  }

  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }


}
