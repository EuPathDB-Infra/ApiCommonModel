package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class AnnotatedGenomeCentromereTelomere extends AnnotatedGenome {

  public void addModelReferences() {
      // add all references from AnnotatedGenome first
      super.addModelReferences();
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesByCentromereProximity");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesByTelomereProximity");


  }

}
