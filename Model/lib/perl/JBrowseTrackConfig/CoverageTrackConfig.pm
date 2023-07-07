package ApiCommonModel::Model::JBrowseTrackConfig::CoverageTrackConfig;
use base qw(ApiCommonModel::Model::JBrowseTrackConfig::TrackConfig);

use strict;
use warnings;

sub getUrlTemplate {$_[0]->{url_template} }
sub setUrlTemplate {$_[0]->{url_template} = $_[1]}

sub getCovMaxScoreDefault {$_[0]->{cov_max_score_default} || 1000}
sub setCovMaxScoreDefault {$_[0]->{cov_max_score_default} = $_[1]}

sub getCovMinScoreDefault {
    my $self = shift;
    my $min = $self->{cov_min_score_default};

    if(defined($min)) {
        return $min;
    }
    return $self->getScale eq "log" ? 1 : 0;
}
sub setCovMinScoreDefault {$_[0]->{cov_min_score_default} = $_[1]}

sub getScale {$_[0]->{scale} || "log"}
sub setScale {$_[0]->{scale} = $_[1]}

sub getYScalePosition {$_[0]->{yscale_position} || "left"}
sub setYScalePosition {$_[0]->{yscale_position} = $_[1]}

# These are optional  metadata
sub getStrand {$_[0]->{strand}}
sub setStrand {$_[0]->{strand} = $_[1]}

sub getAlignment {$_[0]->{alignment}}
sub setAlignment {$_[0]->{alignment} = $_[1]}


sub new {
    my ($class, $args) = @_;
    my $self = $class->SUPER::new($args);

    $self->setUrlTemplate($args->{url_template});

    $self->setCovMaxScoreDefault($args->{cov_max_score_default});
    $self->setCovMinScoreDefault($args->{cov_min_score_default});
    $self->setScale($args->{scale});

    if($self->getApplicationType() eq 'jbrowse' || $self->getApplicationType() eq 'apollo') {
        $self->setStoreType("JBrowse/Store/SeqFeature/BigWig");
        $self->setDisplayType("JBrowse/View/Track/Wiggle/XYPlot");
        $self->setTrackTypeDisplay("Coverage");
    }
    else {
        $self->setStoreType("BigWigAdapter");
        $self->setTrackType("QuantitativeTrack");
        $self->setDisplayType("LinearWiggleDisplay");

        $self->setTrackTypeDisplay("Coverage");
    }

    # These are optional
    $self->setStrand($args->{strand});
    $self->setAlignment($args->{alignment});

    return $self;
}

sub getJBrowseObject{
    my $self = shift;

    my $jbrowseObject = $self->SUPER::getJBrowseObject();

    my $scale = $self->getScale();
    my $minScore = $self->getCovMinScoreDefault();
    my $maxScore = $self->getCovMinScoreDefault();
    my $yScalePosition = $self->getYScalePosition();

    $jbrowseObject->{yScalePosition} = $yScalePosition;
    $jbrowseObject->{min_score} = $minScore;
    $jbrowseObject->{max_score} = $maxScore;
    $jbrowseObject->{scale} = $scale;

    return $jbrowseObject;
}

sub getJBrowse2Object{
    my $self = shift;

    my $jbrowse2Object = $self->SUPER::getJBrowse2Object();

    my $scale = $self->getScale();
    my $minScore = $self->getCovMinScoreDefault();
    my $maxScore = $self->getCovMaxScoreDefault();
    #my $yScalePosition = $self->getYScalePosition();

    $jbrowse2Object->{displays}->[0]->{minScore} = $minScore;
    $jbrowse2Object->{displays}->[0]->{maxScore} = $maxScore;
    $jbrowse2Object->{displays}->[0]->{scaleType} = $scale;

    return $jbrowse2Object;
}





sub getMetadata {
    my $self = shift;

    my $strand = $self->getStrand();
    my $alignment = $self->getAlignment();

    my $metadata = $self->SUPER::getMetadata();
    $metadata->{strand} = $strand if($strand);
    $metadata->{alignment} = $alignment if($alignment);

    return $metadata;
}





1;
