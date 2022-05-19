package org.apidb.apicommon.model.userdataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gusdb.wdk.model.WdkModelException;
import org.gusdb.wdk.model.user.dataset.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ISASimpleTypeHandler extends UserDatasetTypeHandler {
  public final static String NAME = "ISASimple";
  public final static String VERSION = "1.0";
  public final static String DISPLAY = "ISA Simple";

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
  @Override
  public UserDatasetCompatibility getCompatibility(UserDataset userDataset, DataSource appDbDataSource) {
    return new UserDatasetCompatibility(true, "");
  }

  @Override
  public UserDatasetType getUserDatasetType() {
    return UserDatasetTypeFactory.getUserDatasetType(NAME, VERSION);
  }
  
  @Override
  public String getDisplay() {
	return DISPLAY;
  }

  @Override
  public String[] getInstallInAppDbCommand(UserDataset userDataset,
                                           Map<String, Path> fileNameToTempFileMap,
                                           String projectId,
                                           Path workingDir) {
    final Path datasetTmpFile = fileNameToTempFileMap.values().stream()
        .findFirst()
        .orElseThrow();
    final String metaJsonTmpFile = Path.of(workingDir.toString(), "tmp-meta.json").toString();
    try {
      final var meta = userDataset.getMeta();
      OBJECT_MAPPER.writeValue(new File(metaJsonTmpFile), meta);
    } catch (WdkModelException | IOException e) {
      throw new RuntimeException(e);
    }
    String[] cmd = {"singularity", "run",
        "--bind", workingDir + ":/work",
        "--bind", "$GUS_HOME/config/gus.config:/gusApp/gus_home/config/gus.config",
        "--bind", "$ORACLE_HOME/network/admin:/opt/oracle/instantclient_21_6/network/admin",
        "docker://veupathdb/dataset-installer-isasimple:latest",
        "loadStudy.bash",
        datasetTmpFile.toString(),
        userDataset.getUserDatasetId().toString(),
        metaJsonTmpFile};
    return cmd;
  }

  @Override
  public Set<String> getInstallInAppDbFileNames(UserDataset userDataset) {
    try {
      return userDataset.getFiles().values().stream()
          .map(file -> file.getFileName())
          .collect(Collectors.toSet());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String[] getUninstallInAppDbCommand(Long userDatasetId, String projectId) {
    String[] cmd = {"singularity", "run",
        "--bind", "$componentGusConfigFile:/gusApp/gus_home/gus.config",
        "docker://veupathdb/dataset-installer-isasimple:latest",
        "deleteStudy.pl",
        userDatasetId.toString()};
    return cmd;
  }

  @Override
  public String[] getRelevantQuestionNames(UserDataset userDataset) {
    String[] empty = {};
    return empty;
  }
}
