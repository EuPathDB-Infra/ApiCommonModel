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

  @Override
  public void injectTemplates() {
      String projectName = getPropValue("projectName");
      String datasetName = getDatasetName();

      setOrganismAbbrevFromDatasetName();

      if(getPropValueAsBoolean("isEuPathDBSite")) {
          setPropValue("includeProjects", projectName + ",EuPathDB");

      } else {
          setPropValue("includeProjects", projectName);
      }

      setPropValue("graphGenePageSection", "expression");

      setGraphModule();

      setPropValue("exprMetric", "rpm");
      setPropValue("graphYAxisDescription", "count per million");

      String exprMetric = getPropValue("exprMetric");
      setPropValue("graphVisibleParts", exprMetric + ",percentile");

      setPropValue("exprGraphAttr", datasetName + "_expr_graph");
      setPropValue("pctGraphAttr", datasetName + "_pct_graph");

      injectTemplate("spliceSitesExpressionGraphAttributes");
      injectTemplate("spliceSitesGraph");

  
      if(getPropValueAsBoolean("hasMultipleSamples")) {
        injectTemplate("spliceSitesFoldChangeQuestion");
        //        injectTemplate("spliceSitesFoldChangeCategories");

        setPropValue("searchCategory", "searchCategory-fold-change");
        setPropValue("questionName", "GeneQuestions.GenesBySpliceSites" + getDatasetName());
        injectTemplate("internalGeneSearchCategory");


        injectTemplate("spliceSitesProfileSetsQuery");
        injectTemplate("spliceSitesDifferentialQuestion");
        //        injectTemplate("spliceSitesDifferentialCategories");
        setPropValue("searchCategory", "searchCategory-splice-site-loc");
        setPropValue("questionName", "GeneQuestions.GenesByDifferentialSpliceSites" + getDatasetName());
        injectTemplate("internalGeneSearchCategory");

      }
      injectTemplate("spliceSitesProfileSetParamQuery");
      injectTemplate("spliceSitesPercentileQuestion");
      //      injectTemplate("spliceSitesPercentileCategories");
      setPropValue("searchCategory", "searchCategory-percentile");
      setPropValue("questionName", "GeneQuestions.GenesBySpliceSites" + getDatasetName() + "Percentile");
      injectTemplate("internalGeneSearchCategory");


      if(getPropValue("graphPriorityOrderGrouping").equals("")) {
        setPropValue("graphPriorityOrderGrouping", "5");
      }

 // need to make sure there are no single quotes in the descrip
         String datasetDescrip = getPropValue("datasetDescrip");
         setPropValue("datasetDescrip", datasetDescrip.replace("'", ""));

      setPropValue("isGraphCustom", "false");
      injectTemplate("genePageGraphDescriptions") ;    
      injectTemplate("datasetExampleGraphDescriptions");
  }


  protected void setGraphModule() {
      setPropValue("graphModule", "SpliceSites");
  }


  @Override
  public void addModelReferences() {
      setGraphModule();
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "profile_graph", getPropValue("graphModule") + getDatasetName() ); 

      addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "SpliceSites"); 
      addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "PolyASites"); 

      if(getPropValueAsBoolean("hasMultipleSamples")) {
    addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
        "GeneQuestions.GenesBySpliceSites" + getDatasetName() );


    addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
        "GeneQuestions.GenesByDifferentialSpliceSites" + getDatasetName());
      }

      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
          "GeneQuestions.GenesBySpliceSites" + getDatasetName() + "Percentile");

  }

  
  // declare properties required beyond those inherited from the datasetPresenter
  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {
                                 {"isEuPathDBSite", ""},
                                 {"hasMultipleSamples", "if experiment has just one sample, then NO fold-change or differential Q"},
                                 {"graphColor", ""},
                                 {"graphBottomMarginSize", ""},
                                 {"graphXAxisSamplesDescription", "will show up on the gene record page next to the graph"},
                                 {"graphPriorityOrderGrouping", "numeric grouping / ordering of graphs on the gene record page"},
                                 {"graphForceXLabelsHorizontal", "should the x axis labels be always horiz"},
                                 {"optionalQuestionDescription", "html text to be appended to the descriptions of all questions"},
      };

    return declaration;
  }
}
