package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class SAGETags extends DatasetInjector {

  public void injectTemplates() {
  }

  public void addModelReferences() {
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesBySageTag");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesBySageTagRStat");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "SageTags");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagByRStat");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagByLocation");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagBySequence");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagByGeneSourceId");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagByExpressionLevel");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "question", "SageTagQuestions.SageTagByRadSourceId");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "attribute", "overview");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "table", "AllCounts");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "table", "Genes");
      addWdkReference("SageTagRecordClasses.SageTagRecordClass", "table", "Locations");
  }

  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }


}
