package org.apidb.apicommon.model.datasetInjector;

import org.apidb.apicommon.datasetPresenter.DatasetInjector;

public class MicroarrayProbes extends DatasetInjector {

  @Override
  public void injectTemplates() {

      setShortAttribution();
      setOrganismAbbrevFromDatasetName();
      String organismAbbrevDisplay = getPropValue("organismAbbrevDisplay");
      setPropValue("organismAbbrevDisplay", organismAbbrevDisplay.replace(":", ""));

      injectTemplate("microarrayProbesGBrowseTrack");
      injectTemplate("jbrowseMicroarrayProbes");

      setPropValue("gbrowseTrackName", "MicroarrayProbes_" + getDatasetName() );
      injectTemplate("gbrowseTrackCategory");

  }

  @Override
  public void addModelReferences() {
  }

  // second column is for documentation
  @Override
  public String[][] getPropertiesDeclaration() {
      String[][] propertiesDeclaration = {
      };
      return propertiesDeclaration;
  }

}
