
package org.apidb.apicommon.model.datasetInjector;


public class QuantitativeProteomicsNonRatio extends ExpressionOneChannelAndReferenceDesign {

    @Override
    protected void setProteinCodingProps() {
        setPropValue("defaultProteinCodingOnly", "no");
        setPropValue("proteinCodingParamVisible", "false");
	// setPropValue("hasPercentileData", "true");
    }


    @Override
    public void injectTemplates() {
        super.injectTemplates();

        injectTemplate("proteomicsSimpleNonRatio");
    }

    @Override
    protected void setExprPlotPartModule() {
        setPropValue("exprPlotPartModule", "QuantMassSpecNonRatio");
    }

    @Override
    protected void setExprGraphVisiblePart() {
        setPropValue("exprGraphVisiblePart", "exprn_val");
    }

    @Override
    protected void setGraphModule() {
        setPropValue("graphModule", "Proteomics::NonRatio");
    }


    @Override
    protected void setProfileSamplesHelp() {
        String profileSamplesHelp = "Protein Abundance Values.";

        setPropValue("profileSamplesHelp", profileSamplesHelp);
    }


    @Override
    protected void setGraphYAxisDescription() {
        String yAxisDescription = "Protein Abundance Values or abundance percentile values";

        setPropValue("graphYAxisDescription", yAxisDescription);
    }

    @Override
    protected void setDataType() {
        setDataType("Proteomics");
    }

       

  @Override
  public void addModelReferences() {
      super.addModelReferences();

      addWdkReference("GeneRecordClasses.GeneRecordClass", "table", "ProteinExpressionGraphs");

  }


    @Override
    public String[][] getPropertiesDeclaration() {
        String[][] exprDeclaration = super.getPropertiesDeclaration();
        
        String [][] declaration = {{"isLogged", "Is the Data Logged or not"},
        };

        return combinePropertiesDeclarations(exprDeclaration, declaration);
    }

}
