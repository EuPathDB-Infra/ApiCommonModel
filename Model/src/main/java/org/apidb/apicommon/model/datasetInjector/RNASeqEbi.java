package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;
//import org.apidb.apicommon.model.datasetInjector.RNASeq;

public class RNASeqEbi extends RNASeq {
  
  /*
   * getPropValues() gets the property values provided by the datasetPresenter
   * xml file. they are validated against the names provided in
   * getPropertyNames(). in the code below, the whole bundle is passed to each
   * of the templates even though a given template might not need all of them.
   * this is just convenience, rather than tailoring the list per template, it
   * is safe to pass all in, because unneeded ones will be ignored.
   */

    @Override
    protected void setProfileSamplesHelp() {
        String profileSamplesHelp = "Transcript abundance in Transcripts per Million (TPM)";

        setPropValue("profileSamplesHelp", profileSamplesHelp);
    }

    @Override
    protected void setExprMetric() {
        setPropValue("exprMetric", "tpm");
    }

    @Override
    protected void setGraphYAxisDescription() {
        setPropValue("graphYAxisDescription", "Transcript abundance in Transcripts per Million (TPM). The percentile graph shows the ranking of expression for this gene compared to all others in this experiment.");
    }

}
