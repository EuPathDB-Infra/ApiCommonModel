package ApiCommonModel::Model::JBrowseTrackConfig::ProteinExpressionMassSpec;
use base qw(ApiCommonModel::Model::JBrowseTrackConfig::Segments);
use strict;
use warnings;

use ApiCommonModel::Model::JBrowseTrackConfig::RestStore;

sub getExtdbName {$_[0]->{extdbname}}
sub setExtdbName {$_[0]->{extdbname} = $_[1] }

sub new {
    my ($class, $args) = @_;
    my $self = $class->SUPER::new($args);

    my $datasetConfig = $self->getDatasetConfigObj();
    $datasetConfig->setCategory("Proteomics");
    $datasetConfig->setSubcategory("Protein Expression'");

    $self->setId("");
    $self->setLabel("");
    $self->setExtdbName($args->{extdbname});
  
    my $datasetExtdbName = $self->getExtdbName();

    my $store;

    if($self->getApplicationType() eq 'jbrowse' || $self->getApplicationType() eq 'apollo') {
        $store = ApiCommonModel::Model::JBrowseTrackConfig::RestStore->new($args);
        $store->setQuery("domain:MassSpecPeptide"); 
        #$store->setQueryParamsHash('edName' => "like '${datasetExtdbName}'");
        $store->setQueryParamsHash('edName' => "like 'test'");
    }
    else {
        # TODO
    }

    $self->setStore($store);

    my $detailsFunction = "{massSpecDetails}";
    $self->setOnClickContent($detailsFunction);
    $self->setViewDetailsContent($detailsFunction);

    return $self;
}

sub getJBrowseStyle {
    my $self = shift;

    my $jbrowseStyle = $self->SUPER::getJBrowseStyle();

    $jbrowseStyle->{color} = "{massSpecColor}";
    $jbrowseStyle->{label} = "Sample,sample,name";
    return $jbrowseStyle;
}



# TODO:
sub getJBrowse2Object{
        my $self = shift;

        my $jbrowse2Object = $self->SUPER::getJBrowse2Object();


        return $jbrowse2Object;
}


1;
