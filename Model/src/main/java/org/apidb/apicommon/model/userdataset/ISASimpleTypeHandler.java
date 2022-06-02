package org.apidb.apicommon.model.userdataset;

import org.gusdb.fgputil.json.JsonUtil;
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
  public final static String NAME = "ISA";
  public final static String VERSION = "0.0";
  public final static String DISPLAY = "ISA Simple";

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
      JsonUtil.Jackson.writeValue(new File(metaJsonTmpFile), meta);
    } catch (WdkModelException | IOException e) {
      throw new RuntimeException(e);
    }
    final String gusConfigBinding = String.format("%s/config/%s/gus.config:/gusApp/gus_home/config/gus.config",
        System.getenv("GUS_HOME"), System.getenv("PROJECT_ID"));
    String[] cmd = {"singularity", "run",
        "--bind", workingDir + ":/work",
        "--bind", gusConfigBinding,
        "--bind", System.getenv("ORACLE_HOME") + "/network/admin:/opt/oracle/instantclient_21_6/network/admin",
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
