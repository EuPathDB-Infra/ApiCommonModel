package org.apidb.apicommon.model.datasetInjector;

public class RNASeqMetaCycleFungiDB extends RNASeq {

  @Override
  public void addModelReferences() {

      super.addModelReferences();

      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByRNASeqMetaCyclencraOR74A_Bharath_Circadian_Time_Course_ebi_rnaSeq_RSRC"); 
  }

}
