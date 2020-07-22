package ApiCommonModel::Model::JBrowseUtil;

use strict;

use lib $ENV{GUS_HOME} . "/lib/perl";

use DBI;
use DBD::Oracle;

use WDK::Model::ModelConfig;

sub getDbh {$_[0]->{_dbh}}
sub getCacheFile {$_[0]->{_cache_file}}

sub new {
  my ($class, $args) = @_;

  my $self = bless($args, $class);

  my $organismAbbrev = $args->{organismAbbrev};
  my $fileName = $args->{fileName};
  my $type = $args->{type};

  my $cacheFile = $type && $type eq 'protein' 
      ? $ENV{GUS_HOME} . "/lib/jbrowse/auto_generated/$organismAbbrev/aa/$fileName" 
      : $ENV{GUS_HOME} . "/lib/jbrowse/auto_generated/$organismAbbrev/$fileName";;

  if($organismAbbrev && $fileName) {
    $self->{_cache_file} = $cacheFile;
  }

  unless($organismAbbrev && $fileName && -e $cacheFile) {
    my $modelConfig = new WDK::Model::ModelConfig($args->{projectName});

    my $dbh = DBI->connect( $modelConfig->getAppDbDbiDsn(),
                            $modelConfig->getAppDbLogin(),
                            $modelConfig->getAppDbPassword()
        )
        || die "unable to open db handle to ", $modelConfig->getAppDbDbiDsn();
    
    $dbh->{LongTruncOk} = 0;
    $dbh->{LongReadLen} = 10000000;
    $self->{_dbh} = $dbh;
  }

  return $self;
}

sub intronJunctionsQueryParams {
  my ($self, $level) = @_;

  my $projectName = $self->{projectName};

  my $querieParams = { 'PlasmoDB' => { 'refined' => { feature => "gsnap:unifiedintronjunction",
                                                     intronSizeLimit => 3000,
                                                     minIntronPercent => .01,
                                                     minNonContainedRatio => .2,
                                                     minContainedRatio => .05,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 20,
                                                     maxIsrpmMult => 50,
                                                     isrpmRatio => .05,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 3,
                                                      annotated_intron => "ALL",
                                                   },
                                      'inclusive' => {
                                                     feature => "gsnap:unifiedintronjunctioninclusive",
                                                     intronSizeLimit => 5000,
                                                     minIntronPercent => .001,
                                                     minNonContainedRatio => .0001,
                                                     minContainedRatio => .0001,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 10,
                                                     maxIsrpmMult => 50,
                                                     isrpmRatio => .05,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 1,
                                                      annotated_intron => "ALL",
                                                   },
                                    },
                       'HostDB' => { 'refined' => { feature => "gsnap:unifiedintronjunction",
                                                     intronSizeLimit => 200000,
                                                     minIntronPercent => .05,
                                                     minNonContainedRatio => .02,
                                                     minContainedRatio => .2,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 10,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 3,
                                                      annotated_intron => "ALL",
                                                   },
                                      'inclusive' => {
                                                     feature => "gsnap:unifiedintronjunctioninclusive",
                                                     intronSizeLimit => 500000,
                                                     minIntronPercent => .001,
                                                     minNonContainedRatio => .0001,
                                                     minContainedRatio => .0001,
                                                     minContainedScore => 1,
                                                     minNonContainedScore => 4,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 1,
                                                      annotated_intron => "ALL",
                                                   },
                                    },
                       'GiardiaDB' => { 'refined' => { feature => "gsnap:unifiedintronjunction",
                                                     intronSizeLimit => 5000,
                                                     minIntronPercent => .05,
                                                     minNonContainedRatio => .02,
                                                     minContainedRatio => .2,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 10,
                                                     maxIsrpmMult => 50,
                                                     isrpmRatio => .01,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 3,
                                                      annotated_intron => "ALL",
                                                   },
                                      'inclusive' => {
                                                     feature => "gsnap:unifiedintronjunctioninclusive",
                                                     intronSizeLimit => 5000,
                                                     minIntronPercent => .00001,
                                                     minNonContainedRatio => .000001,
                                                     minContainedRatio => .000001,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 4,
                                                     maxIsrpmMult => 50,
                                                     isrpmRatio => .01,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 1,
                                                      annotated_intron => "ALL",
                                                   },
                                    },
                       'VectorBase' => { 'refined' => { feature => "gsnap:unifiedintronjunction",
                                                     intronSizeLimit => 500000,
                                                     minIntronPercent => .05,
                                                     minNonContainedRatio => .1,
                                                     minContainedRatio => .05,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 10,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 3,
                                                      annotated_intron => "ALL",
                                                   },
                                      'inclusive' => {
                                                     feature => "gsnap:unifiedintronjunctioninclusive",
                                                     intronSizeLimit => 600000,
                                                     minIntronPercent => .001,
                                                     minNonContainedRatio => .0001,
                                                     minContainedRatio => .0001,
                                                     minContainedScore => 1,
                                                     minNonContainedScore => 4,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 1,
                                                      annotated_intron => "ALL",
                                                   },
                                    },
                       'default' => { 'refined' => { feature => "gsnap:unifiedintronjunction",
                                                     intronSizeLimit => 30000,
                                                     minIntronPercent => .01,
                                                     minNonContainedRatio => .1,
                                                     minContainedRatio => .05,
                                                     minContainedScore => 2,
                                                     minNonContainedScore => 10,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 3,
                                                      annotated_intron => "ALL",
                                                   },
                                      'inclusive' => {
                                                     feature => "gsnap:unifiedintronjunctioninclusive",
                                                     intronSizeLimit => 30000,
                                                     minIntronPercent => .001,
                                                     minNonContainedRatio => .0001,
                                                     minContainedRatio => .0001,
                                                     minContainedScore => 1,
                                                     minNonContainedScore => 4,
                                                     maxIsrpmMult => 5,
                                                     isrpmRatio => .5,
                                                     externalDatabaseName => "ALL",
                                                     minReadsMaxSample => 1,
                                                      annotated_intron => "ALL",
                                                   },
                                    },
  };

  if($querieParams->{$projectName}->{$level}) {
    return $querieParams->{$projectName}->{$level};
  }

  return $querieParams->{default}->{$level};
}

sub printFromCache {
  my ($self) = @_;

  my $cacheFile = $self->getCacheFile();

  if(-e $cacheFile) {
    open(FILE, $cacheFile) or die "Cannot open file $cacheFile for reading: $!";
    print <FILE>;
    close FILE;
    return 1
  }
  return 0;
}

1;
