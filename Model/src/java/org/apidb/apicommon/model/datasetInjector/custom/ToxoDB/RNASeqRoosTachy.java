package org.apidb.apicommon.model.datasetInjector.custom.ToxoDB;

import org.apidb.apicommon.model.datasetInjector.CusomGenePageExpressionGraphsAndCoverage;

public class RNASeqRoosTachy extends CusomGenePageExpressionGraphsAndCoverage {

  public void addModelReferences() {
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.ToxoGenesByGregoryRNASeqTsFC"); 
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestios.ToxoGenesByGregoryRNASeqTsPValue"); 
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.ToxoGenesByGregoryRNASeqTsPct"); 
  }


}
