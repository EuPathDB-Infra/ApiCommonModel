"""
iRODS paths and path templates that together define the architecture of the user dataset iRODS system.
"""
WORKSPACES_PATH = "/ebrc/workspaces"
LANDING_ZONE_PATH = WORKSPACES_PATH + "/lz"
STAGING_PATH = WORKSPACES_PATH + "/staging"
USERS_PATH = WORKSPACES_PATH + "/users"
DEFAULT_QUOTA_DATA_OBJECT_PATH = USERS_PATH + "/quota"
USER_DATASETS_COLLECTION_TEMPLATE = USERS_PATH + "/{}/datasets"
USER_EXTERNAL_DATASETS_COLLECTION_TEMPLATE = USERS_PATH + "/{}/externalDatasets"
USER_DATASET_COLLECTION_TEMPLATE = USER_DATASETS_COLLECTION_TEMPLATE + "/{}"
USER_DATASET_METADATA_DATA_OBJECT_TEMPLATE = USER_DATASET_COLLECTION_TEMPLATE + "/meta.json"
USER_DATASET_DATASET_DATA_OBJECT_TEMPLATE = USER_DATASET_COLLECTION_TEMPLATE + "/dataset.json"
USER_DATASET_SHARED_WITH_COLLECTION_TEMPLATE = USER_DATASET_COLLECTION_TEMPLATE + "/sharedWith"
FLAGS_PATH = WORKSPACES_PATH + "/flags"
FLAG_DATA_OBJECT_TEMPLATE = FLAGS_PATH + "/{}"
EVENTS_PATH = WORKSPACES_PATH + "/events"
EVENTS_DATA_OBJECT_TEMPLATE = EVENTS_PATH + "/{}"