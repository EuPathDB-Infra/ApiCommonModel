package org.apidb.apicommon.model.datasetInjector;

import java.util.List;

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

  @Override
  public void injectTemplates() {
      setShortAttribution();



      String projectName = getPropValue("projectName");
      //String presenterId = getPropValue("presenterId");
      String datasetName = getDatasetName();
      
      Boolean switchStrandsGBrowse = getPropValueAsBoolean("switchStrandsGBrowse");
      Boolean switchStrandsProfiles = getPropValueAsBoolean("switchStrandsProfiles");

      setPropValue("metadataFileSuffix","");
      setOrganismAbbrevFromDatasetName();

      if(getPropValueAsBoolean("isEuPathDBSite")) {
          setPropValue("includeProjects", projectName + ",EuPathDB");

      } else {
          setPropValue("includeProjects", projectName);
      }

      setPropValue("graphModule", "RNASeq");
      setPropValue("stranded", "");

      String datasetShortDisplayName = getPropValue("datasetShortDisplayName");
      if (datasetShortDisplayName == null || datasetShortDisplayName.equals("")) {
        throw new RuntimeException(datasetName + " datasetShortDisplayName cannot be null");
      }

      injectTemplate("datasetCategory");

      if(getPropValueAsBoolean("isDESeq")) {
	    injectTemplate("DESeqProfileSetParamQuery");
      }
      
      if(getPropValueAsBoolean("isDEGseq")) {
	  injectTemplate("DEGseqProfileSetParamQuery");
      }
      if(getPropValueAsBoolean("isAlignedToAnnotatedGenome")) {

	  // plas-rbld has NO '%rpkm%' name entries in the study.study table. Are all fpkm then?
          //if(getPropValueAsBoolean("hasPairedEnds")) {
              setPropValue("exprMetric", "fpkm");
              setPropValue("graphYAxisDescription", "Transcript levels of fragments per kilobase of exon model per million mapped reads (FPKM).  Stacked bars indicate unique and non-uniquely mapped sequences.  Non-Unique sequences are plotted to indicate the maximum expression potential of this gene.  The percentile graph shows the ranking of expression for this gene compared to all others in this experiment.");
	      //} else {
              //setPropValue("exprMetric", "rpkm");
              //setPropValue("graphYAxisDescription", "Transcript levels of reads per kilobase of exon model per million mapped reads (RPKM).  Stacked bars indicate unique and non-uniquely mapped sequences.  Non-Unique sequences are plotted to indicate the maximum expression potential of this gene.  The percentile graph shows the ranking of expression for this gene compared to all others in this experiment.");
	      //}

          String exprMetric = getPropValue("exprMetric");


          if(getPropValueAsBoolean("isStrandSpecific")) {
	      setPropValue("stranded", "Strand Specific ");
	      if (switchStrandsGBrowse) {
		  setPropValue("metadataFileSuffix","_alt");
	      } 

	      if (switchStrandsProfiles) {
		  setPropValue("antisense","firststrand") ;
		  setPropValue("sense","secondstrand") ;
	      } 
              else {
		  setPropValue("sense","firststrand") ;
		  setPropValue("antisense","secondstrand") ;
	      }

              setPropValue("graphVisibleParts", exprMetric + "_sense");
              injectTemplate("pathwayGraphs");


              String senseExprGraphAttr = datasetName + "_sense_expr_graph";
              String antisenseExprGraphAttr = datasetName + "_antisense_expr_graph";

              String sensePctGraphAttr = datasetName + "_sense_pct_graph";
              String antisensePctGraphAttr = datasetName + "_antisense_pct_graph";

              String pathwayGraphAttr = datasetName + "_ss_expr_graph_pr";

              // These are used for the question
              setPropValue("exprGraphAttr", senseExprGraphAttr + "," + antisenseExprGraphAttr);
              setPropValue("pctGraphAttr", sensePctGraphAttr + "," + antisensePctGraphAttr);

              injectTemplate("rnaSeqSsExpressionGraphAttributes");
              injectTemplate("rnaSeqSsExpressionGraphAttributesPathwayRecord");

              // Inject the text attributes into the ontology for the results summary page
              setPropValue("graphTextAttrName", senseExprGraphAttr);
              injectTemplate("graphTextAttributeCategory");

              setPropValue("graphTextAttrName", antisenseExprGraphAttr);
              injectTemplate("graphTextAttributeCategory");


              setPropValue("graphTextAttrName", sensePctGraphAttr);
              injectTemplate("graphTextAttributeCategory");


              setPropValue("graphTextAttrName", antisensePctGraphAttr);
              injectTemplate("graphTextAttributeCategory");

              setPropValue("graphTextAttrName", pathwayGraphAttr);
              injectTemplate("graphTextAttributeCategoryPathwayRecord");


              injectTemplate("rnaSeqProfileSetParamQuery");
	      injectTemplate("rnaSeqPctProfileSetParamQuery");

	      injectTemplate("rnaSeqGraph");
          } else {
	      //	      setPropValue("sense","unstranded") ;

              setPropValue("graphVisibleParts", exprMetric);
              injectTemplate("pathwayGraphs");

              setPropValue("graphVisibleParts", exprMetric + ",percentile");


              String exprGraphAttr = datasetName + "_expr_graph";
              String pctGraphAttr = datasetName + "_pct_graph";

              String pathwayGraphAttr = datasetName + "_expr_graph_pr";

              setPropValue("exprGraphAttr", exprGraphAttr);
              setPropValue("pctGraphAttr", pctGraphAttr);

              injectTemplate("rnaSeqExpressionGraphAttributes");
              injectTemplate("rnaSeqExpressionGraphAttributesPathwayRecord");
              injectTemplate("rnaSeqProfileSetParamQuery");
	      injectTemplate("rnaSeqPctProfileSetParamQuery");


              // Add text attribute to the categories ontology
              setPropValue("graphTextAttrName", exprGraphAttr);
              injectTemplate("graphTextAttributeCategory");

              setPropValue("graphTextAttrName", pctGraphAttr);
              injectTemplate("graphTextAttributeCategory");

              setPropValue("graphTextAttrName", pathwayGraphAttr);
              injectTemplate("graphTextAttributeCategoryPathwayRecord");

              injectTemplate("rnaSeqGraph");
          }


          if(getPropValueAsBoolean("hasMultipleSamples")) {
              injectTemplate("rnaSeqFoldChangeQuestion");
              //              injectTemplate("rnaSeqFoldChangeCategories");
              setPropValue("searchCategory", "searchCategory-transcriptomics-fold-change");
              setPropValue("questionName", "GeneQuestions.GenesByRNASeq" + getDatasetName());
              injectTemplate("internalGeneSearchCategory");


          }

          injectTemplate("rnaSeqPercentileQuestion");
          //          injectTemplate("rnaSeqPercentileCategories");
          setPropValue("searchCategory", "searchCategory-transcriptomics-percentile");
          setPropValue("questionName", "GeneQuestions.GenesByRNASeq" + getDatasetName() + "Percentile");
          injectTemplate("internalGeneSearchCategory");

	  if(getPropValueAsBoolean("isDESeq")) {
	      injectTemplate("rnaSeqDESeqQuestion");
	      setPropValue("searchCategory", "searchCategory-transcriptomics-differential-expression");
	      setPropValue("questionName", "GeneQuestions.GenesByRNASeq" + getDatasetName() + "DESeq");
	      injectTemplate("internalGeneSearchCategory");
	  }
	  if(getPropValueAsBoolean("isDEGseq")) {
	      injectTemplate("rnaSeqDEGseqQuestion");
	      setPropValue("searchCategory", "searchCategory-transcriptomics-differential-expression-degseq");
	      setPropValue("questionName", "GeneQuestions.GenesByRNASeq" + getDatasetName() + "DEGseq");
	      injectTemplate("internalGeneSearchCategory");
	  }

          if(getPropValue("graphPriorityOrderGrouping").equals("")) {
              setPropValue("graphPriorityOrderGrouping", "1");
          }

 // need to make sure there are no single quotes in the descrip
         String datasetDescrip = getPropValue("datasetDescrip");
         setPropValue("datasetDescrip", datasetDescrip.replace("'", ""));
  
        setPropValue("isGraphCustom", "false");
          injectTemplate("genePageGraphDescriptions");

          injectTemplate("datasetExampleGraphDescriptions");
         
      }

      // remove ':' which is gbrowse category separator.
      String organismAbbrevDisplay = getPropValue("organismAbbrevDisplay");
      setPropValue("organismAbbrevDisplay", organismAbbrevDisplay.replace(":", ""));

      injectTemplate("rnaSeqCoverageTrack");
      setPropValue("gbrowseTrackName", getDatasetName() + "Coverage");
      injectTemplate("gbrowseTrackCategory");

      injectTemplate("rnaSeqCoverageTrackUnlogged");
      setPropValue("gbrowseTrackName", getDatasetName() + "CoverageUnlogged");
      injectTemplate("gbrowseTrackCategory");




      String showIntronJunctions = getPropValue("showIntronJunctions");
      if(Boolean.parseBoolean(showIntronJunctions)) {
        
          String experimentName = datasetName.replace("_rnaSeq_RSRC", "");
          

          //String experimentName = experimentRsrc.replaceFirst("RSRC", "");
          setPropValue("experimentName", experimentName);

          // String organismAbbrev = getPropValue("organismAbbrev");
          
          /** junctions now use unified track.  no need for sample list
          List<String> sampleNames = getSampleList();
          String subtracks = "";
          for (int i=0; i<sampleNames.size(); i++) {
              String subtrack = sampleNames.get(i);
              if (i == sampleNames.size() -1) {
                  subtracks = subtracks + "'" + subtrack + "';";
              }
              else {
                  subtracks = subtracks + "'" + subtrack + "';\n                  ";
              }
          }
          setPropValue("subtracks", subtracks);
          **/


          if(projectName.equals("HostDB")) {
              setPropValue("intronSizeLimit", "100000");
          }
          else {
              setPropValue("intronSizeLimit", "9000");
          }

          injectTemplate("rnaSeqJunctionsTrack");
          setPropValue("gbrowseTrackName", getDatasetName() + "Junctions");
          injectTemplate("gbrowseTrackCategory");
      }

  }


  @Override
  public void addModelReferences() {
      if(getPropValueAsBoolean("isAlignedToAnnotatedGenome")) {

	  setPropValue("graphModule", "RNASeq");
	  /*  if(getPropValueAsBoolean("isStrandSpecific")) {
              setPropValue("graphModule", "RNASeq::StrandSpecific");
          } else {
              setPropValue("graphModule", "RNASeq::StrandNonSpecific");
	      } */
              addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "profile_graph", getPropValue("graphModule") + getDatasetName() ); 
              addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "ExpressionGraphs");
      
          if(getPropValueAsBoolean("hasMultipleSamples")) {

              addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
                              "GeneQuestions.GenesByRNASeq" + getDatasetName());

          }
	  
	  if (getPropValueAsBoolean("isDESeq")) {
	      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByRNASeq" +getDatasetName() +"DESeq");
	  }
	  if (getPropValueAsBoolean("isDEGseq")) {
	      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByRNASeq" +getDatasetName() +"DEGseq");
	  }

          addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
                          "GeneQuestions.GenesByRNASeq" + getDatasetName() + "Percentile");
      }
  }
  
  // declare properties required beyond those inherited from the datasetPresenter
  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {
                                 {"isEuPathDBSite", ""},
                                 {"switchStrandsProfiles", ""},
                                 {"switchStrandsGBrowse", ""},
                                 {"graphColor", ""},
                                 {"graphSampleLabels", ""},
                                 {"graphBottomMarginSize", ""},
                                 {"showIntronJunctions", ""},
                                 {"includeInUnifiedJunctions", ""},
                                 {"isAlignedToAnnotatedGenome", ""},
                                 {"hasMultipleSamples", ""},
                                 {"graphXAxisSamplesDescription", "will show up on the gene record page next to the graph"},
                                 {"graphPriorityOrderGrouping", "numeric grouping / ordering of graphs on the gene record page"},
                                 {"optionalQuestionDescription", "html text to be appended to the descriptions of all questions"},
                                 {"graphForceXLabelsHorizontal", "should the x axis labels be always horiz"},
				 {"isDESeq", ""},
				 {"isDEGseq",""},
      };

    return declaration;
  }
}
