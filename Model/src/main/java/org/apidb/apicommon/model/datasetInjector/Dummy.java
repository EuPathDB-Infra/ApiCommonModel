package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class Dummy extends DatasetInjector {

  @Override
  public void injectTemplates() {


    setOrganismAbbrevFromDatasetName();

  }

  @Override
  public void addModelReferences() {
  }

  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
    String[][] propertiesDeclaration = {};
    return propertiesDeclaration;
  }


}
