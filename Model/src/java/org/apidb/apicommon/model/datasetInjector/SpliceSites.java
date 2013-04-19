package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class SpliceSites extends  DatasetInjector {
  
  /*
   * getPropValues() gets the property values provided by the datasetPresenter
   * xml file. they are validated against the names provided in
   * getPropertyNames(). in the code below, the whole bundle is passed to each
   * of the templates even though a given template might not need all of them.
   * this is just convenience, rather than tailoring the list per template, it
   * is safe to pass all in, because unneeded ones will be ignored.
   */

  public void injectTemplates() {
      // TODO:  graphs for the gene record page?

      String projectName = getPropValue("projectName");

      setPropValue("organismAbbrev", getOrganismAbbrevFromDatasetName());

      String exprMetric = getPropValue("exprMetric");


      if(getPropValueAsBoolean("isEuPathDBSite")) {
          setPropValue("includeProjects", projectName + ",EuPathDB");

      } else {
          setPropValue("includeProjects", projectName);
      }

      setPropValue("graphGenePageSection", "expression");

      setPropValue("graphModule", "SpliceSites");

          if(getPropValueAsBoolean("isPairedEnd")) {
              setPropValue("exprMetric", "fpkm");
              setPropValue("graphYAxisDescription", "");
          } else {
              setPropValue("exprMetric", "rpkm");
              setPropValue("graphYAxisDescription", "");
          }
      setPropValue("graphVisibleParts", exprMetric + ",percentile");

      setPropValue("exprGraphAttr", datasetName + "_expr_graph");
      setPropValue("pctGraphAttr", datasetName + "_pct_graph");

      injectTemplate("spliceSitesAttributeCategory");
      injectTemplate("spliceSitesExpressionGraphAttributes");
      injectTemplate("spliceSitesGraph");

  
      if(getPropValueAsBoolean("hasMultipleSamples")) {
	  injectTemplate("spliceSitesProfileSetParamQuery");
	  injectTemplate("spliceSitesFoldChangeQuestion");

	  injectTemplate("spliceSitesProfileSetsQuery");
	  injectTemplate("spliceSitesDifferentialQuestion");
      }

      injectTemplate("spliceSitesPctProfileSetParamQuery");
      injectTemplate("spliceSitesPercentileQuestion");
     
  }

  public void addModelReferences() {

      // TODO: Add reference for Graph
      if(getPropValueAsBoolean("hasMultipleSamples")) {
	  addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
			  "GenesBySpliceSites" + getDatasetName() );


	  addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
			  "GenesByDifferentialSpliceSites" + getDatasetName());
      }

      addWdkReference("GeneRecordClasses.GeneRecordClass", "question",
		      "GenesBySpliceSites" + getDatasetName() + "Percentile");

  }

  
  // declare properties required beyond those inherited from the datasetPresenter
  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {
                                 {"isEuPathDBSite", ""},
                                 {"hasMultipleSamples", "if experiment has just one sample, then NO fold-change or differential Q"},
                                 {"graphColor", ""},
                                 {"graphBottomMarginSize", ""},
                                 {"graphXAxisSamplesDescription", "will show up on the gene record page next to the graph"},
                                 {"graphPriorityOrderGrouping", "numeric grouping / ordering of graphs on the gene record page"},
                                 {"optionalQuestionDescription", "html text to be appended to the descriptions of all questions"},
      };

    return declaration;
  }
}
