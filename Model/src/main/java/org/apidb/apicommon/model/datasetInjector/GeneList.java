package org.apidb.apicommon.model.datasetInjector;

public class GeneList {

  @Override
    public void injectTemplates() {
      setShortAttribution();


      String projectName = getPropValue("projectName");
      //String presenterId = getPropValue("presenterId");
      String datasetName = getDatasetName();

      setOrganismAbbrevFromDatasetName();

     if(getPropValueAsBoolean("isEuPathDBSite")) {
          setPropValue("includeProjects", projectName + ",EuPathDB,UniDB");
      } else {
          setPropValue("includeProjects", projectName + ",UniDB");
      }

     String searchCategory = "searchCategory-functional-gene-list";
     String questionName = "TODO";

     setPropValue("searchCategory", searchCategory);
     setPropValue("questionName", questionName);

     injectTemplate("geneListFunctional");
     injectTemplate("internalGeneSearchCategory");
    }


  @Override
    public void addModelReferences() {
                addWdkReference("TranscriptRecordClasses.TranscriptRecordClass", "question",
                                "GeneQuestions.GenesByFunctionalGeneList" + getDatasetName());
  }


  @Override
  public String[][] getPropertiesDeclaration() {
      String [][] declaration = {
                                 {"isEuPathDBSite", ""}
                                 };
          }

  
}
