package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.model.datasetInjector.MicroarrayOneChannelRma;
public class MicroarrayOneChannelRmaMetaCycle extends MicroarrayOneChannelRma {


  @Override
  public void injectTemplates() {

      super.injectTemplates();

      setPropValue("searchCategory", "searchCategory-transcriptomics-VBmetacycle");

      injectTemplate("internalGeneSearchCategory");

  }



  @Override
  public void addModelReferences() {

      super.addModelReferences();

      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByMicroarrayMetaCycle" + getDatasetName()); 

  }



}
