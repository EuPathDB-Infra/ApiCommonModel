package ApiCommonModel::Model::JBrowseTrackConfig::PopsetsTrackConfig;
use base qw(ApiCommonModel::Model::JBrowseTrackConfig::Segments);
use strict;
use warnings;

# TODO: Set border color here in new method
sub getBorderColor {$_[0]->{border_color}}
sub setBorderColor {$_[0]->{border_color} = $_[1]}

sub getBaseUrl {$_[0]->{baseUrl} }
sub setBaseUrl {
    my($self, $baseUrl) = @_;
    die "required baseUrl not set" unless $baseUrl;
    $self->{baseUrl} = $baseUrl;
}

sub new {
    my ($class, $args) = @_;
    my $self = $class->SUPER::new($args);

    $self->setColor($args->{color});
    $self->setCategory("Sequence Analysis");
    $self->setDisplayType("JBrowse/View/Track/CanvasFeatures");
    $self->setId("Popset Isolate Sequence Alignments");
    $self->setLabel("popsetIsolates");
    $self->setSubcategory("BLAT and Blast Alignments");
    $self->setBaseUrl($args->{baseUrl});

    return $self;
}

sub getJBrowseObject{
        my $self = shift;

        my $jbrowseObject = $self->SUPER::getJBrowseObject();
        $jbrowseObject->{onClick} = {content => "{popsetDetails}",};
        $jbrowseObject->{menuTemplate} = [
                            {label =>  "View Details", content => "{popsetDetails}",},
        ];
        $jbrowseObject->{style} = {color => "{popsetColor}"};
        $jbrowseObject->{baseUrl}= $self->getBaseUrl();
        $jbrowseObject->{query} = {'feature' => "match:IsolatePopset",};

    return $jbrowseObject;
}

# TODO:
sub getJBrowse2Object{
        my $self = shift;

        my $jbrowse2Object = $self->SUPER::getJBrowse2Object();


        return $jbrowse2Object;
}


1;
