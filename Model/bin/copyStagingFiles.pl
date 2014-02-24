#!/usr/bin/perl

use strict;
use File::Copy::Recursive qw(dircopy);
use File::Path qw(rmtree);
use File::Spec qw(splitdir);
use File::Find;
use File::Basename;
use Getopt::Long;

my ($help, $configFile, $apiSiteFilesDir, $includeProjects, $buildNumber);
&GetOptions('help|h' => \$help,
            'configFile=s' => \$configFile,
            'apiSiteFilesDir=s' => \$apiSiteFilesDir,
	    'includeProjects=s' => \$includeProjects,
	    'buildNumber=s' => \$buildNumber,
            );
&usage() if($help);
&usage("ERROR: configFile not specfied.") unless(-e $configFile);
&usage("ERROR: buildNumber not specfied.") if (!$buildNumber);

my $destRootDir = '/eupath/data/apiSiteFiles/webServices/';
$destRootDir = $apiSiteFilesDir if ($apiSiteFilesDir);

if ($includeProjects eq 'EuPath'){
    $includeProjects = qw/AmoebaDB CryptoDB GiardiaDB HostDB MicrosporidiaDB PiroplasmaDB PlasmoDB ToxoDB TriTrypDB TrichDB/;
}

if ($includeProjects eq 'ALL'){
    $includeProjects = qw/AmoebaDB CryptoDB GiardiaDB HostDB MicrosporidiaDB PiroplasmaDB PlasmoDB ToxoDB TriTrypDB TrichDB FungiDB/;
}

# build hash of project_id and staging_dir
my %stagingDir;
open (IN, "$configFile") || die "ERROR: Couldn't open prop file '$configFile'\n";;
while (<IN>) {
  chomp;
  $stagingDir{$1} = $2   if ($_ =~/^(.*)\t(.*)$/  );
}
close (IN);

my @blastExtns = qw/nhr nin nsq phr pin psq/;  # for NCBI Blast
# my @blastExtns = qw/xnd xns xnt xpd xps xpt/;  # for WU-Blast

# for each specified project
my @projects = split(',', $includeProjects);
foreach my $p (@projects) {
  die "ERROR: No entry in config file for $p\n" if !($stagingDir{$p});

  my $destDir = "$destRootDir$p/build-$buildNumber";

  ## handle the case when destDir exists
  if (-d $destDir) {
    print "WARNING: existing $destDir is being deleted, and will be remade.\n";
    rmtree ($destDir);
  }

  umask 002; # resulting files will have mode 0644, directories 0755

  local $File::Copy::Recursive::SkipFlop = 1;

  ## do the copy from Staging
  my $startDir = $stagingDir{$p};
  # print "copy from $startDir into $destDir\n";
  my($num_of_files_and_dirs,$num_of_dirs,$depth_traversed) = File::Copy::Recursive::dircopy($startDir,$destDir);
  print "\n$p DONE: $num_of_files_and_dirs,$num_of_dirs,$depth_traversed \n";

  ## fix Blast file names
  unless ($p eq 'TrichDB') {
      finddepth { 'wanted' => \&process_file, 'no_chdir' => 0 }, $destDir;
  }

  ## fix permissions
  find(\&fixPerm, $destDir);
}

sub process_file {
  my $dir_name = (File::Spec -> splitdir ($File::Find::dir)) [-2];
  my $file_name = basename $_;
  my $extension = ($file_name =~ m/([^.]+)$/)[0];

  if ( -f $_ && /$extension/ ~~  @blastExtns )   {
    print "RENAMED $file_name TO  $dir_name$file_name\n";
    rename $_, "$dir_name$file_name";
  }
}


sub usage {
  my $e = shift;

  if($e) {
    print STDERR $e . "\n";
  }
  print STDERR "usage:  copyStagingFiles.pl --configFile <FILE>  --includeProjects <LIST|EuPath|ALL> -- buildNumber <NUM> (--apiSiteFilesDir <DIR>)\n";
  exit;
}

sub fixPerm {
  my $perm = -d $File::Find::name ? 0775 : 0664;
  chmod $perm, $File::Find::name;
}


1;
