/**
 * 
 */
package org.apidb.apicommon.model.report;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gusdb.fgputil.db.SqlUtils;
import org.gusdb.fgputil.db.pool.DatabaseInstance;
import org.gusdb.wdk.model.WdkModelException;
import org.gusdb.wdk.model.WdkUserException;
import org.gusdb.wdk.model.answer.AnswerValue;
import org.gusdb.wdk.model.dbms.CacheFactory;
import org.gusdb.wdk.model.dbms.QueryInfo;
import org.gusdb.wdk.model.dbms.ResultFactory;
import org.gusdb.wdk.model.query.QueryInstance;
import org.gusdb.wdk.model.record.RecordClass;
import org.gusdb.wdk.model.record.RecordInstance;
import org.gusdb.wdk.model.record.attribute.AttributeValue;
import org.gusdb.wdk.model.report.Reporter;
import org.json.JSONException;

/**
 * @author xingao
 * 
 */
public class Gff3CachedReporter extends Reporter {

    private static Logger logger = Logger.getLogger(Gff3Reporter.class);

    public static final String PROPERTY_TABLE_CACHE = "table_cache";
    public static final String PROPERTY_RECORD_ID_COLUMN = "record_id_column";

    public static final String PROPERTY_GFF_RECORD_NAME = "gff_record";
    public static final String PROPERTY_GFF_TRANSCRIPT_NAME = "gff_transcript";
    public static final String PROPERTY_GFF_PROTEIN_NAME = "gff_protein";

    public final static String FIELD_HAS_TRANSCRIPT = "hasTranscript";
    public final static String FIELD_HAS_PROTEIN = "hasProtein";

    private final static String COLUMN_CONTENT = "content";

    private String tableCache;
    private String recordIdColumn;
    private String recordName;
    private String proteinName;
    private String transcriptName;

    private boolean hasTranscript = false;
    private boolean hasProtein = false;

    public Gff3CachedReporter(AnswerValue answerValue, int startIndex,
            int endIndex) {
        super(answerValue, startIndex, endIndex);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.gusdb.wdk.model.report.Reporter#setProperties(java.util.Map)
     */
    @Override
    public void setProperties(Map<String, String> properties)
            throws WdkModelException {
        super.setProperties(properties);

        // check required properties
        tableCache = properties.get(PROPERTY_TABLE_CACHE);
        recordIdColumn = properties.get(PROPERTY_RECORD_ID_COLUMN);
        recordName = properties.get(PROPERTY_GFF_RECORD_NAME);
        proteinName = properties.get(PROPERTY_GFF_PROTEIN_NAME);
        transcriptName = properties.get(PROPERTY_GFF_TRANSCRIPT_NAME);

        if (tableCache == null || tableCache.length() == 0)
            throw new WdkModelException("The required property for reporter "
                    + this.getClass().getName() + ", " + PROPERTY_TABLE_CACHE
                    + ", is missing");
        if (recordIdColumn == null || recordIdColumn.length() == 0)
            throw new WdkModelException("The required property for reporter "
                    + this.getClass().getName() + ", "
                    + PROPERTY_RECORD_ID_COLUMN + ", is missing");
        if (recordName == null || recordName.length() == 0)
            throw new WdkModelException("The required property for reporter "
                    + this.getClass().getName() + ", "
                    + PROPERTY_GFF_RECORD_NAME + ", is missing");
        if (proteinName == null || proteinName.length() == 0)
            throw new WdkModelException("The required property for reporter "
                    + this.getClass().getName() + ", "
                    + PROPERTY_GFF_PROTEIN_NAME + ", is missing");
        if (transcriptName == null || transcriptName.length() == 0)
            throw new WdkModelException("The required property for reporter "
                    + this.getClass().getName() + ", "
                    + PROPERTY_GFF_PROTEIN_NAME + ", is missing");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gusdb.wdk.model.report.Reporter#configure(java.util.Map)
     */
    @Override
    public void configure(Map<String, String> config) {
        super.configure(config);

        // include transcript
        if (config.containsKey(FIELD_HAS_TRANSCRIPT)) {
            String value = config.get(FIELD_HAS_TRANSCRIPT);
            hasTranscript = (value.equalsIgnoreCase("yes") || value
                    .equalsIgnoreCase("true")) ? true : false;
        }

        // include protein
        if (config.containsKey(FIELD_HAS_PROTEIN)) {
            String value = config.get(FIELD_HAS_PROTEIN);
            hasProtein = (value.equalsIgnoreCase("yes") || value
                    .equalsIgnoreCase("true")) ? true : false;
        }
    }

    @Override
    public String getConfigInfo() {
        return "This reporter does not have config info yet.";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gusdb.wdk.model.report.Reporter#getHttpContentType()
     */
    @Override
    public String getHttpContentType() {
        if (format.equalsIgnoreCase("text")) {
            return "text/plain";
        } else { // use the default content type defined in the parent class
            return super.getHttpContentType();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gusdb.wdk.model.report.Reporter#getDownloadFileName()
     */
    @Override
    public String getDownloadFileName() {
        logger.info("Internal format: " + format);
        String name = getQuestion().getName();
        if (format.equalsIgnoreCase("text")) {
            return name + ".gff";
        } else { // use the default file name defined in the parent
            return super.getDownloadFileName();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.gusdb.wdk.model.report.IReporter#format(org.gusdb.wdk.model.Answer)
     */
    @Override
    protected void write(OutputStream out) throws WdkModelException,
            NoSuchAlgorithmException, SQLException, JSONException,
            WdkUserException {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));

        // this reporter only works for GeneRecordClasses.GeneRecordClass
        String rcName = getQuestion().getRecordClass().getFullName();
        if (!rcName.equals("GeneRecordClasses.GeneRecordClass"))
            throw new WdkModelException("Unsupported record type: " + rcName);

        // write header
        writeHeader(writer);

        // write record
        writeRecords(writer);

        // write sequence
        if (hasTranscript || hasProtein)
            writeSequences(writer);
    }

    private void writeHeader(PrintWriter writer) throws WdkModelException {
        writer.println("##gff-version\t3");
        writer.println("##feature-ontology\tso.obo");
        writer.println("##attribute-ontology\tgff3_attributes.obo");
        writer.flush();

        // get the sequence regions
        Map<String, int[]> regions = new LinkedHashMap<String, int[]>();

        // get page based answers with a maximum size (defined in
        // PageAnswerIterator)
        for (AnswerValue answerValue : this) {
            for (RecordInstance record : answerValue.getRecordInstances()) {
                String seqId = getValue(record.getAttributeValue("gff_seqid"));
                int start = Integer.parseInt(getValue(record
                        .getAttributeValue("gff_fstart")));
                int stop = Integer.parseInt(getValue(record
                        .getAttributeValue("gff_fend")));
                if (regions.containsKey(seqId)) {
                    int[] region = regions.get(seqId);
                    if (region[0] > start)
                        region[0] = start;
                    if (region[1] < stop)
                        region[1] = stop;
                    regions.put(seqId, region);
                } else {
                    int[] region = { start, stop };
                    regions.put(seqId, region);
                }
            }
        }

        // put sequence id into the header
        for (String seqId : regions.keySet()) {
            int[] region = regions.get(seqId);
            writer.println("##sequence-region\t" + seqId + "\t" + region[0]
                    + "\t" + region[1]);
        }
        writer.flush();
    }

    private void writeRecords(PrintWriter writer) throws WdkModelException {
        // get primary key columns
        RecordClass recordClass = getQuestion().getRecordClass();
        String[] pkColumns = recordClass.getPrimaryKeyAttributeField()
                .getColumnRefs();

        // get id sql, then combine with the gff cache.
        String idSql = baseAnswer.getSortedIdSql();

        StringBuffer sql = new StringBuffer("SELECT tc." + COLUMN_CONTENT);
        sql.append(" FROM " + tableCache + " tc, (" + idSql + ") ac");
        sql.append(" WHERE tc.table_name = '" + recordName + "' ");
        for (String column : pkColumns) {
            sql.append(" AND tc." + column + " = ac." + column);
        }

        DatabaseInstance db = getQuestion().getWdkModel().getAppDb();

        // get the result from database
        ResultSet rsTable = null;
        try {
            rsTable = SqlUtils.executeQuery(db.getDataSource(),
                    sql.toString(), "api-report-gff-select-content");

            while (rsTable.next()) {
                String content = db.getPlatform().getClobData(rsTable, "content");
                writer.print(content);
                writer.flush();
            }
        } catch (SQLException ex) {
            throw new WdkModelException(ex);
        } finally {
            SqlUtils.closeResultSetAndStatement(rsTable);
        }
    }

    private void writeSequences(PrintWriter writer) throws WdkModelException {
        // get primary key columns
        RecordClass recordClass = getQuestion().getRecordClass();
        String[] pkColumns = recordClass.getPrimaryKeyAttributeField()
                .getColumnRefs();

        // get cache info
        ResultFactory resultFactory = wdkModel.getResultFactory();
        CacheFactory cacheFactory = resultFactory.getCacheFactory();
        QueryInstance instance = baseAnswer.getIdsQueryInstance();
        QueryInfo queryInfo = cacheFactory.getQueryInfo(instance.getQuery());
        String cacheTable = queryInfo.getCacheTable();
        int instanceId = instance.getInstanceId();

        // construct in clause
        StringBuffer sqlIn = new StringBuffer();
        if (hasTranscript)
            sqlIn.append("'" + transcriptName + "'");
        if (hasProtein) {
            if (sqlIn.length() > 0)
                sqlIn.append(", ");
            sqlIn.append("'" + proteinName + "'");
        }

        StringBuffer sql = new StringBuffer("SELECT ");
        sql.append("tc.").append(COLUMN_CONTENT).append(" FROM ");
        sql.append(tableCache).append(" tc, ").append(cacheTable).append(" ac");
        sql.append(" WHERE tc.table_name IN (").append(sqlIn).append(")");
        for (String column : pkColumns) {
            sql.append(" AND tc.").append(column).append(" = ac.")
                    .append(column);
        }
        sql.append(" AND ac.").append(CacheFactory.COLUMN_INSTANCE_ID);
        sql.append(" = ").append(instanceId);
        sql.append(" ORDER BY tc.table_name ASC");

        DatabaseInstance db = getQuestion().getWdkModel().getAppDb();

        writer.println("##FASTA");

        // get the result from database
        ResultSet rsTable = null;
        try {
            rsTable = SqlUtils.executeQuery(db.getDataSource(),
                    sql.toString(), "api-report-gff-select-content");

            while (rsTable.next()) {
                String content = db.getPlatform().getClobData(rsTable, "content");
                writer.print(content);
                writer.flush();
            }
        } catch (SQLException ex) {
            throw new WdkModelException(ex);
        } finally {
            SqlUtils.closeResultSetAndStatement(rsTable);
        }
    }

    private String getValue(AttributeValue attrVal)
            throws WdkModelException {
        String value;
        if (attrVal == null) {
            return null;
        } else {
            value = attrVal.getValue().toString();
        }
        value = value.trim();
        if (value.length() == 0)
            return null;
        return value;
    }

    @Override
    protected void complete() {
        // do nothing
    }

    @Override
    protected void initialize() throws WdkModelException {
        // do nothing
    }
}
