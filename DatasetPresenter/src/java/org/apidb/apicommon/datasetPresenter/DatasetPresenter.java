package org.apidb.apicommon.datasetPresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gusdb.fgputil.xml.NamedValue;
import org.gusdb.fgputil.xml.Text;

/**
 * A specification for adding a dataset to the presentation layer. A simple data
 * holder that contains a set of properties and a set of
 * DatasetInjectorConstructors. At processing time it is transformed into a set
 * of DatasetInjector subclasses.
 * 
 * @author steve
 * 
 */
public class DatasetPresenter {

  // use prop values for properties that might be injected into templates.
  Map<String, String> propValues = new HashMap<String, String>();

  // use instance variables for properties that have no chance of being
  // injected.
  private String displayCategory;
  private String protocol;
  private String usage;
  private String acknowledgement;
  private String caveat;
  private String releasePolicy;
  private DatasetInjector datasetInjector;
  private String datasetNamePattern;
  private String type;
  private String subtype;
  private Boolean isSpeciesScope;
  private boolean foundInDb = false;

  private List<DatasetInjectorConstructor> datasetInjectorConstructors = new ArrayList<DatasetInjectorConstructor>();
  private List<String> contactIds = new ArrayList<String>(); // includes primary
  private String primaryContactId;
  private List<Contact> contacts;
  private List<Publication> publications = new ArrayList<Publication>();
  private List<HyperLink> links = new ArrayList<HyperLink>();
  private Map<String, NameTaxonPair> nameTaxonPairs = new HashMap<String, NameTaxonPair>(); // expanded from pattern if we have one
  private String override = null;

  void setFoundInDb() {
    foundInDb = true;
  }
  
  boolean getFoundInDb() {
    return foundInDb;
  }
  
  public void setName(String datasetName) {
    propValues.put("datasetName", datasetName);
  }

  public String getDatasetName() {
    return propValues.get("datasetName");
  }

  String getPropValue(String propName) {
    return propValues.get(propName);
  }

  public void setDatasetDescrip(Text datasetDescrip) {
    propValues.put("datasetDescrip", datasetDescrip.getText());
  }

  public String getDatasetDescrip() {
    return propValues.get("datasetDescrip");
  }

  public void setDatasetDisplayName(Text datasetDisplayName) {
    propValues.put("datasetDisplayName", datasetDisplayName.getText());
  }

  public String getDatasetDisplayName() {
    return propValues.get("datasetDisplayName");
  }

  public void setDatasetShortDisplayName(Text datasetShortDisplayName) {
    propValues.put("datasetShortDisplayName", datasetShortDisplayName.getText());
  }

  public String getDatasetShortDisplayName() {
    return propValues.get("datasetShortDisplayName");
  }

  public void setSummary(Text summary) {
    propValues.put("summary", summary.getText());
  }

  public String getSummary() {
    return propValues.get("summary");
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setSubtype(String subtype) {
    this.subtype = subtype;
  }

  public String getSubtype() {
    return subtype;
  }

  public void setIsSpeciesScope(Boolean isSpeciesScope) {
    this.isSpeciesScope = isSpeciesScope;
  }

  public Boolean getIsSpeciesScope() {
    return isSpeciesScope;
  }

  public void setDatasetNamePattern(String pattern) {
    if (!pattern.contains("%") || pattern.contains("*"))
      throw new UserException(
          "Dataset "
              + getDatasetName()
              + " contains an illegal datasetNamePattern attribute.  It must contain one or more SQL wildcard (%) and no other type of wildcards");
    propValues.put("datasetNamePattern", pattern);
    datasetNamePattern = pattern;
  }

  public String getDatasetNamePattern() {
    return datasetNamePattern;
  }
  
  public void setOverride(String datasetName) {
    this.override = datasetName;
  }
  
  public String getOverride() {
    return override;
  }

  public void addNameTaxonPair(NameTaxonPair pair) {
    nameTaxonPairs.put(pair.getName(), pair);
  }
  
  public void removeNameTaxonPair(String name) {
    nameTaxonPairs.remove(name);
  }
  
  boolean containsNameTaxonPair(String name) {
    return nameTaxonPairs.containsKey(name);
  }

  public Collection<NameTaxonPair> getNameTaxonPairs() {
    return nameTaxonPairs.values();
  }

  public void setProjectName(String projectName) {
    propValues.put("projectName", projectName);
  }

  public void setOrganismShortName(String organismShortName) {
    propValues.put("organismShortName", organismShortName);
  }

  public void setBuildNumberIntroduced(Integer buildNumberIntroduced) {
     propValues.put("buildNumberIntroduced", buildNumberIntroduced.toString());
  }

  public Integer getBuildNumberIntroduced() {
    if (propValues.get("buildNumberIntroduced") == null)
      return null;
    return new Integer(propValues.get("buildNumberIntroduced"));
  }

  public void setBuildNumberRevised(Integer buildNumberRevised) {
     propValues.put("buildNumberRevised", buildNumberRevised.toString());
  }

  public Integer getBuildNumberRevised() {
    if (propValues.get("buildNumberRevised") == null)
      return null;
    return new Integer(propValues.get("buildNumberRevised"));
  }

  public void setDisplayCategory(Text displayCategory) {
    this.displayCategory = displayCategory.getText();
  }

  public String getDisplayCategory() {
    return displayCategory;
  }

  public void setCaveat(Text caveat) {
    this.caveat = caveat.getText();
  }

  public String getCaveat() {
    return caveat;
  }

  public void setReleasePolicy(Text releasePolicy) {
    this.releasePolicy = releasePolicy.getText();
  }

  public String getReleasePolicy() {
    return releasePolicy;
  }

  public void setProtocol(Text protocol) {
    this.protocol = protocol.getText();
  }

  public String getProtocol() {
    return protocol;
  }

  public void setUsage(Text usage) {
    this.usage = usage.getText();
  }

  public String getUsage() {
    return usage;
  }

  public void setAcknowledgement(Text acknowledgement) {
    this.acknowledgement = acknowledgement.getText();
  }

  public String getAcknowledgement() {
    return acknowledgement;
  }

  public void addContactId(Text contactId) {
    contactIds.add(contactId.getText());
  }

  public void setPrimaryContactId(Text contactId) {
    primaryContactId = contactId.getText();
    contactIds.add(contactId.getText());
  }

  public List<String> getContactIds() {
    return contactIds;
  }

  public List<Contact> getContacts(Contacts allContacts) {
    if (contacts == null) {
      contacts = new ArrayList<Contact>();
      for (String contactId : contactIds) {
        Contact contact = allContacts.get(contactId);
        if (contact == null) {
          String datasetName = propValues.get("datasetName");
          throw new UserException("Dataset name " + datasetName
              + " has a contactId " + contactId
              + " that has no corresponding contact in contacts file "
              + allContacts.getContactsFileName());
        }
        Contact contactCopy = (Contact) contact.clone();
        contacts.add(contactCopy);
        if (contactId.equals(primaryContactId))
          contactCopy.setIsPrimary(true);

      }
    }
    return contacts;
  }

  public void addPublication(Publication publication) {
    publications.add(publication);
  }

  public List<Publication> getPublications() {
    return publications;
  }

  public void addLink(HyperLink link) {
    links.add(link);
  }

  public List<HyperLink> getLinks() {
    return links;
  }

  /**
   * Add a DatasetInjector. This method should be changed to setDatasetInjector
   * as we no longer support multiple DasasetInjectors per DatasetPresenter
   * 
   * @param datasetInjector
   */
  public void addDatasetInjector(DatasetInjectorConstructor datasetInjector) {
    datasetInjectorConstructors.add(datasetInjector);
    datasetInjector.inheritDatasetProps(this);
  }

  private DatasetInjector getDatasetInjector() {
    if (datasetInjector == null && datasetInjectorConstructors.size() != 0) {
      DatasetInjectorConstructor dic = datasetInjectorConstructors.get(0);
      datasetInjector = dic.getDatasetInjector();
      datasetInjector.addModelReferences();
    }
    return datasetInjector;
  }

  void setDefaultDatasetInjector(
      Map<String, Map<String, String>> defaultDatasetInjectors) {
    if (type == null
        || defaultDatasetInjectors == null
        || !defaultDatasetInjectors.containsKey(type)
        || !defaultDatasetInjectors.get(type).containsKey(subtype)
        || datasetInjectorConstructors.size() != 0)
      return;
    DatasetInjectorConstructor constructor = new DatasetInjectorConstructor();
    constructor.setClassName(defaultDatasetInjectors.get(type).get(subtype));
    addDatasetInjector(constructor);
  }

  public List<ModelReference> getModelReferences() {
    List<ModelReference> answer = new ArrayList<ModelReference>();
    DatasetInjector di = getDatasetInjector();
    if (di != null) {
      answer = di.getModelReferences();
    }
    return answer;
  }

  public List<DatasetInjectorConstructor> getDatasetInjectors() {
    return datasetInjectorConstructors;
  }

  public void addProp(NamedValue propValue) {
    if (propValues.containsKey(propValue.getName())) {
      throw new UserException("datasetPresenter '" + getDatasetName()
          + "' has redundant property: " + propValue.getName());
    }
    propValues.put(propValue.getName(), propValue.getValue());
  }

  public Map<String, String> getPropValues() {
    return propValues;
  }
}
