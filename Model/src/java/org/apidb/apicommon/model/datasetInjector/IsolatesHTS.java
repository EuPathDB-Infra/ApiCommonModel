package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;
import java.util.List;

public class IsolatesHTS extends DatasetInjector {

  @Override
  public void injectTemplates() {
      String datasetName = getDatasetName();
      String[] datasetWords = datasetName.split("_");


      setOrganismAbbrevFromDatasetName();

      // trim off the prefix and suffix from the experiment name
      // new workflow uses different naming convention for HTS isolates, need to handle both.
      // pfal3D7_SNP_Conway_HTS_SNP_RSRC (build-18)
      // pfal3D7_HTS_SNP_Conway_HTS_SNP_RSRC (build-19)
      String experimentRsrc = datasetName.replaceFirst(datasetWords[0] + "_HTS_SNP_", "");
      experimentRsrc = experimentRsrc.replaceFirst(datasetWords[0] + "_SNP_", "");

      String experimentName = experimentRsrc.replaceFirst("_RSRC", "");
      setPropValue("experimentName", experimentName);

      // use getSampleList method, refer to - https://redmine.apidb.org/issues/16510
      String organismAbbrev = getPropValue("organismAbbrev");
      String sampleNameSuffix = "_HTS_SNPSample_RSRC";
      List<String> sampleNames = getSampleList();

      String organismAbbrevDisplay = getPropValue("organismAbbrevDisplay");
      setPropValue("organismAbbrevDisplay", organismAbbrevDisplay.replace(":", ""));

      for (int i=0; i<sampleNames.size(); i++){

          setPropValue("sampleName", sampleNames.get(i));

          String gbrowseDBName = organismAbbrev + "_" + experimentName + "_" + sampleNames.get(i) + sampleNameSuffix;
          setPropValue("gbrowseDBName", gbrowseDBName);

          injectTemplate("htsSnpSampleDatabase");
          injectTemplate("htsSnpSampleCoverageXYTrack");

          setPropValue("gbrowseTrackName", gbrowseDBName);
          injectTemplate("gbrowseTrackCategory");

          injectTemplate("htsSnpSampleCoverageDensityTracks");
          injectTemplate("htsSnpSampleAlignmentTrack");
      }

      if(getPropValueAsBoolean("hasCNVData")) {

          setPropValue("datasetName", datasetName.replaceFirst("_HTS_SNP_", "_copyNumberVariations_"));
          
          for (int i=0; i<sampleNames.size(); i++) {
              setPropValue("sampleName", sampleNames.get(i));
              injectTemplate("copyNumberVariationsDatabase");
              injectTemplate("copyNumberVariationsTrack");

              setPropValue("gbrowseTrackName", getPropValue("datasetName") + getPropValue("sampleName"));
              injectTemplate("gbrowseTrackCategory");
          }       

      }

  }

  @Override
  public void addModelReferences() {
      // NGS SNPs
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.NgsSnpBySourceId");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.NgsSnpsByIsolateGroup");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.NgsSnpsByLocation");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.NgsSnpsByGeneIds");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.NgsSnpsByTwoIsolateGroups");


      addWdkReference("SnpRecordClasses.SnpRecordClass", "attribute", "snp_overview");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "attribute", "gene_context");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "Strains");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "Providers_other_SNPs");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "HTSStrains");


      addWdkReference("GeneRecordClasses.GeneRecordClass", "attribute", "total_hts_snps");
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByNgsSnps");
      //addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByTajimasDHtsSnps");

      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByPopsetId");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByTaxon");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByHost");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByIsolationSource");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByProduct");
      //addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByGenotypeNumber");
      //addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByRFLPGenotype");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByStudy");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByCountry");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetByAuthor");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetsByTextSearch");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "question", "PopsetQuestions.PopsetsByClustering");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "table", "Reference");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "table", "HtsContacts");
      addWdkReference("PopsetRecordClasses.PopsetRecordClass", "attribute", "overview");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "SNPsAlignment");

      addWdkReference("SequenceRecordClasses.SequenceRecordClass", "question", "GenomicSequenceQuestions.SequencesByPloidy");
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByCopyNumber");
      addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question", "GeneQuestions.GenesByCopyNumberComparison");
  }



  // second column is for documentation
    @Override
  public String[][] getPropertiesDeclaration() {
      //String [][] declaration = {{"sampleList", "space del list of sample (sample name = directory name in webservices)"}
      //};
      String[][] declaration = {
         {"hasCNVData", ""},
      };


    return declaration;
  }


}
