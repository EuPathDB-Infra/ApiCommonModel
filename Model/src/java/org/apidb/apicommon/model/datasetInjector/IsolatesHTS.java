package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class IsolatesHTS extends DatasetInjector {

  public void injectTemplates() {
      String datasetName = getDatasetName();

      String[] datasetWords = datasetName.split("_");
      setPropValue("organismAbbrev", datasetWords[0]);

      // trim off the prefix and suffix from the experiment name
      String experimentRsrc = datasetName.replaceFirst(datasetWords[0] + "_SNP_", "");
      String experimentName = experimentRsrc.replaceFirst("_RSRC", "");
      setPropValue("experimentName", experimentName);

      String sampleList = getPropValue("sampleList");
      String[] samples = sampleList.split(" ");
      for(int i = 0; i < samples.length; i++) {
          setPropValue("sampleName", samples[i]);

          injectTemplate("htsSnpSampleDatabase");
          injectTemplate("htsSnpSampleCoverageXYTrack");
          injectTemplate("htsSnpSampleCoverageDensityTracks");
          injectTemplate("htsSnpSampleAlignmentTrack");
      }
  }

  public void addModelReferences() {
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.SnpBySourceId");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.HtsSnpsByGeneId");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.HtsSnpsByLocation");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.HtsSnpsByStrain");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.SnpsByAlleleFrequency");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "question", "SnpQuestions.SnpsByIsolatePattern");

      addWdkReference("SnpRecordClasses.SnpRecordClass", "attribute", "snp_overview");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "attribute", "gene_context");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "Strains");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "Providers_other_SNPs");
      addWdkReference("SnpRecordClasses.SnpRecordClass", "table", "HTSStrains");


      addWdkReference("GeneRecordClasses.GeneRecordClass", "attribute", "total_snps_all_strains");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesByHtsSnps");
      addWdkReference("GeneRecordClasses.GeneRecordClass", "question", "GeneQuestions.GenesByTajimasDHtsSnps");

      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByIsolateId");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByTaxon");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByHost");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByIsolationSource");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByProduct");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByGenotypeNumber");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByRFLPGenotype");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByStudy");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByCountry");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolateByAuthor");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolatesByTextSearch");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "question", "IsolateQuestions.IsolatesByClustering");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "table", "Reference");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "table", "HtsContacts");
      addWdkReference("IsolateRecordClasses.IsolateRecordClass", "attribute", "overview");
  }


  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {{"sampleList", "space del list of sample (sample name = directory name in webservices)"}
      };

    return declaration;
  }


}
