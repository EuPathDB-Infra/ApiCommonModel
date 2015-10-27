package org.apidb.apicommon.model.datasetInjector.custom.PlasmoDB;

import org.apidb.apicommon.model.datasetInjector.CusomGenePageExpressionGraphs;

public class MicroarrayFerdigEQtl extends CusomGenePageExpressionGraphs {

  @Override
  public void addModelReferences() {
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "profile_graph", "Ferdig::Dd2Hb3Similarity"); 
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "profile_graph", "Ferdig::DD2_X_HB3"); 
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByEqtlProfileSimilarity"); 
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByEQTL_HaploGrpSimilarity"); 
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByEQTL_Segments"); 
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "table", "Plasmo_eQTL_Table"); 
      addWdkReference("SpanRecordClasses.SpanRecordClass", "question", "SpanQuestions.DynSpansByEQTLtoGenes"); 
  }


}

