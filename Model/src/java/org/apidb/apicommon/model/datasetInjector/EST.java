package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class EST extends DatasetInjector {

  public void injectTemplates() {
  }

  public void addModelReferences() {
    addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesByESTOverlap");
    addWdkReference("EstRecordClasses.EstRecordClass", "attribute", "overview");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.EstsBySimilarity");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.EstBySourceId");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.EstsWithGeneOverlap");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.EstsByLibrary");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.ESTsByGeneIDs");
    addWdkReference("EstRecordClasses.EstRecordClass", "question", "EstQuestions.EstsByLocation");
  }

  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }

  
}
