package org.apidb.apicommon.model.datasetInjector.custom.PlasmoDB;

import org.apidb.apicommon.model.datasetInjector.RNASeq;

public class RNASeqGalinskiMmulatta extends RNASeq {

  @Override
  public void addModelReferences() {
    super.addModelReferences();
    addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
                    "GeneQuestions.GenesByRNASeq" + getDatasetName() + "SenseAntisense");
  }

  @Override
  protected void injectTemplate(String templateName) {
    if (templateName.equals("rnaSeqPercentileQuestion")) {
      //Do Nothing
    }
    else {
      super.injectTemplate(templateName);
    }
  }
}
