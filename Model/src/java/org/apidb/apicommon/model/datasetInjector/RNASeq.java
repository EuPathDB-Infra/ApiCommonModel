package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class RNASeq extends  DatasetInjector {
  
  /*
   * getPropValues() gets the property values provided by the datasetPresenter
   * xml file. they are validated against the names provided in
   * getPropertyNames(). in the code below, the whole bundle is passed to each
   * of the templates even though a given template might not need all of them.
   * this is just convenience, rather than tailoring the list per template, it
   * is safe to pass all in, because unneeded ones will be ignored.
   */

  public void injectTemplates() {
      // TODO: split out param query templates (not urgent)
      // TODO: which graphs for the gene record page?

      String datasetName = getDatasetName();
      String[] datasetWords = datasetName.split("_");
      setPropValue("organismAbbrev", datasetWords[0]);

      String projectName = getPropValue("projectName");
      String isEuPathDBSite = getPropValue("isEuPathDBSite");
      if(Boolean.parseBoolean(isEuPathDBSite)) {
          setPropValue("includeProjects", projectName + ",EuPathDB");

      } else {
          setPropValue("includeProjects", projectName);
      }

      String isPairedEnd = getPropValue("isPairedEnd");
      if(Boolean.parseBoolean(isPairedEnd)) {
          setPropValue("exprMetric", "fpkm");
      } else {
          setPropValue("exprMetric", "rpkm");
      }

      injectTemplate("rnaSeqAttributeCategory");

      // Strand Specific Could be factored into subclasses
      String isStrandSpecific = getPropValue("isStrandSpecific");
      if(Boolean.parseBoolean(isStrandSpecific)) {
          setPropValue("exprGraphAttr", datasetName + 
                       "_sense_expr_graph," + datasetName + "_antisense_expr_graph");
          setPropValue("pctGraphAttr", datasetName + 
                       "_sense_pct_graph," + datasetName + "_antisense_pct_graph");

          injectTemplate("rnaSeqSsExpressionGraphAttributes");
          injectTemplate("rnaSeqSsProfileSetParamQuery");
          injectTemplate("rnaSeqSsPctProfileSetParamQuery");
          injectTemplate("rnaSeqStrandSpecificGraph");
      
      } else {
          setPropValue("exprGraphAttr", datasetName + "_expr_graph");
          setPropValue("pctGraphAttr", datasetName + "_pct_graph");

          injectTemplate("rnaSeqExpressionGraphAttributes");
          injectTemplate("rnaSeqProfileSetParamQuery");
          injectTemplate("rnaSeqPctProfileSetParamQuery");
          injectTemplate("rnaSeqStrandNonSpecificGraph");
      }

      String hasFishersExactTest = getPropValue("hasFishersExactTestData");
      if(Boolean.parseBoolean(hasFishersExactTest)) {
          injectTemplate("rnaSeqFoldChangeWithPValueQuestion");
      }

      injectTemplate("rnaSeqFoldChangeQuestion");
      injectTemplate("rnaSeqPercentileQuestion");
      injectTemplate("rnaSeqCoverageTrack");

      String hasJunctions = getPropValue("hasJunctions");
      if(Boolean.parseBoolean(hasJunctions)) {
          injectTemplate("rnaSeqJunctionsTrack");
      }

  }

  public void addModelReferences() {
    addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                    "GenesByRNASeq" + getDatasetName());
    addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                    "GenesByRNASeq" + getDatasetName() + "Percentile");

    // TODO: Add reference for Graph

    String hasFishersExactTest = getPropValue("hasFishersExactTestData");
    if(Boolean.parseBoolean(hasFishersExactTest)) {
        addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
                        "GenesByRNASeq" + getDatasetName() + "PValue");
    }

  }
  
  // declare properties required beyond those inherited from the datasetPresenter
  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {{"isTimeSeries", ""},
                                 {"hasFishersExactTestData", ""},
                                 {"isPairedEnd", ""},
                                 {"isEuPathDBSite", ""},
                                 {"graphColor", ""},
                                 {"graphBottomMarginSize", ""},
                                 {"hasJunctions", ""},
                                 {"isStrandSpecific", ""}
      };

    return declaration;
  }
}
