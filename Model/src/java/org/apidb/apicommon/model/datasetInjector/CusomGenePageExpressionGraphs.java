package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public abstract class CusomGenePageExpressionGraphs extends DatasetInjector {

  public void injectTemplates() {

      String description = getPropValue("datasetDescrip");
      setPropValue("datasetDescrip", description.replace("'", ""));

      
      String xAxis = getPropValue("graphXAxisSamplesDescription");
      setPropValue("graphXAxisSamplesDescription", xAxis.replace("'", ""));

      String yAxis = getPropValue("graphYAxisDescription");
      setPropValue("graphYAxisDescription", yAxis.replace("'", ""));


      setPropValue("isGraphCustom", "true");
      injectTemplate("genePageGraphDescriptions");
  }


  // second column is for documentation
  public String[][] getPropertiesDeclaration() {
      String[][] propertiesDeclaration = {    {"graphModule", ""},
                                              {"graphXAxisSamplesDescription", ""},
                                              {"graphYAxisDescription", ""},
                                              {"graphVisibleParts", ""},
                                              {"graphPriorityOrderGrouping", ""},
      };
    return propertiesDeclaration;
  }


}
