package org.apidb.apicommon.model.report;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.gusdb.wdk.model.AnswerValue;
import org.gusdb.wdk.model.AttributeValue;
import org.gusdb.wdk.model.Question;
import org.gusdb.wdk.model.RecordInstance;
import org.gusdb.wdk.model.TableValue;
import org.gusdb.wdk.model.WdkModelException;
import org.gusdb.wdk.model.WdkUserException;
import org.gusdb.wdk.model.report.Reporter;
import org.json.JSONException;

public class GenBankReporter extends Reporter {

    private static final String PROPERTY_GENE_QUESTION = "gene_question";
    private static final String PROPERTY_SEQUENCE_ID_PARAM = "sequence_param";
    private static final String PROPERTY_SEQUENCE_ID_COLUMN = "sequence_id";
    private static final String CONFIG_SELECTED_COLUMNS = "selectedFields";

    public GenBankReporter(AnswerValue answerValue, int startIndex,
			   int endIndex) {
	super(answerValue, startIndex, endIndex);
    }

    // Take a sequence question, collect the sequence ids
    // For each page of sequence ids, pass the list of
    // ids to some other question
    // Go through the pages of gene records, writing as
    // genbank table format

    public void configure(Map<String, String> config) {
	super.configure(config);
    }

    public String getConfigInfo() {
	return "This reporter does not have config info yet.";
    }

    public void write(OutputStream out) throws WdkModelException,
            NoSuchAlgorithmException, SQLException, JSONException,
            WdkUserException {
	String rcName = getQuestion().getRecordClass().getFullName();
	if (!rcName.equals("SequenceRecordClasses.SequenceRecordClass"))
            throw new WdkModelException("Unsupported record type: " + rcName);


	PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
	pageSequences(writer);
	writer.flush();
    }

    private void pageSequences(PrintWriter writer) throws WdkModelException,
            NoSuchAlgorithmException, SQLException, JSONException,
            WdkUserException {
	String lastSequenceId = null;
	for (AnswerValue answerValue : this) {
	    StringBuffer sequenceIdList = new StringBuffer();
	    for (RecordInstance record : answerValue.getRecordInstances()) {
		// Collect the sequence ids in this page
		if (sequenceIdList.length() > 0) {
		    sequenceIdList.append(',');
		}
		sequenceIdList.append(record.getAttributeValue("source_id"));
	    }
	    Map<String,String> params = new LinkedHashMap<String,String>();
	    params.put(properties.get(PROPERTY_SEQUENCE_ID_PARAM), sequenceIdList.toString());

	    Map<String,Boolean> sorting = new LinkedHashMap<String,Boolean>();
	    sorting.put(properties.get(PROPERTY_SEQUENCE_ID_COLUMN), true);

	    Question geneQuestion = (Question) wdkModel.resolveReference(properties.get(PROPERTY_GENE_QUESTION));
	    AnswerValue geneAnswer = geneQuestion.makeAnswerValue(answerValue.getUser(), params, 0,
								  maxPageSize, sorting, null, 0);
	    
	    lastSequenceId = writeGenes(geneAnswer, writer, lastSequenceId);
	}
    }

    private String writeGenes(AnswerValue geneAnswer, PrintWriter writer, String lastSequenceId) throws WdkModelException,
													NoSuchAlgorithmException, SQLException, JSONException,
													WdkUserException {
	PageAnswerIterator pagedGenes = new PageAnswerIterator(geneAnswer, 1, geneAnswer.getResultSize(), maxPageSize);
	while (pagedGenes.hasNext()) {
	    AnswerValue answerValue = pagedGenes.next();
	    
	    for (RecordInstance record : answerValue.getRecordInstances()) {
		if (wdkModel.getProjectId().compareTo("GiardiaDB") != 0 || record.getAttributeValue("is_deprecated").toString().compareTo("Yes") != 0) {
		    String recordSequenceId = record.getAttributeValue(properties.get(PROPERTY_SEQUENCE_ID_COLUMN)).toString();
		    if (lastSequenceId == null || lastSequenceId.compareTo(recordSequenceId) != 0) {
			// If a new sequence id, need to write that out first!
			writer.println(">Feature\t" + recordSequenceId);
		    }
		
		    // Write this record out in GenBank Table Format
		    String sourceId = record.getAttributeValue("source_id").toString();
		    String strand = record.getAttributeValue("strand").toString();
		    String product = record.getAttributeValue("product").toString();
		    boolean isPseudo = (record.getAttributeValue("is_pseudo").toString().compareTo("Yes") == 0
					|| product.toLowerCase().contains("pseudo"));
		    String start, end;
		    if (strand.compareTo("forward") == 0) {
			start = record.getAttributeValue("start_min").toString();
			end = record.getAttributeValue("end_max").toString();
		    }
		    else {
			start = record.getAttributeValue("end_max").toString();
			end = record.getAttributeValue("start_min").toString();
		    }   

		    // Check for start codon at sequence start & stop codon at sequence end
		    // If not found, set the appropriate partial sequence modifiers
		    String sequence;
		    String proteinSequence = null;

		    String geneType = record.getAttributeValue("gene_type").toString();
		    if (geneType.compareTo("protein coding") == 0) {
			geneType = "CDS";
			sequence = record.getAttributeValue("cds").toString();
			proteinSequence = record.getAttributeValue("protein_sequence").toString();
		    }
		    else {
			sequence = record.getAttributeValue("transcript_sequence").toString();
		    }

		    String partialStart = "";
		    String partialEnd = "";
		    if (sequence != null) {
			// Take off any trailing bases that don't form a complete codon.
			sequence = sequence.substring(0,sequence.length() - (sequence.length() % 3));
			if (!sequence.startsWith("ATG")) {
			    partialStart = "<";
			}
			if (!sequence.endsWith("TAG")
			    && !sequence.endsWith("TAA")
			    && !sequence.endsWith("TGA")
			    || sequence.length() % 3 != 0) {
			    partialEnd = ">"; 
			}
		    }

		    // RULE : Give gene bounds, including partial markers if needed.
		    writer.println(partialStart + start + "\t" + partialEnd + end + "\tgene");
		    writer.println("\t\t\tgene\t" + sourceId);

		    // RULE : Include locus tag

		    // RULE : Include old locus tag

		    // RULE : Include gene synonym
		    

		    // RULE : gene type, etc., not valid for pseudo genes, skip this if isPseudo
		    if (!isPseudo) {
			// write exon locations in record entry if exons exist
			List<String> exonLocations = new ArrayList<String>();
			TableValue exons = record.getTableValue("GenBankExons");
			if (exons.size() > 0) {
			    int count = 0;
			    for (Map<String, AttributeValue> exon: exons) {
				String exonStart, exonEnd, exonLocation;
				if (strand.compareTo("forward") == 0) {
				    exonStart = exon.get("exon_start").toString();
				    exonEnd = exon.get("exon_end").toString();	    
				}
				else {
				    exonStart = exon.get("exon_end").toString();
				    exonEnd = exon.get("exon_start").toString();	    
				}
				// RULE : Give exon bounds, including 5' partial marker if this
				// is the first exon, and 3' partial marker if this is the last
				if (count == 0) exonStart = partialStart + exonStart;
				if (count == exons.size() - 1) exonEnd = partialEnd + exonEnd;
				exonLocation = exonStart + "\t" + exonEnd;
				exonLocations.add(exonLocation);
				if (count == 0) writer.println(exonLocation + "\t" + geneType);
				else writer.println(exonLocation);
				count++;
			    }
			}
			else {
			    String codingStart,codingEnd;
			    if (strand.compareTo("forward") == 0) {
				codingStart = record.getAttributeValue("coding_start").toString();
				codingEnd = record.getAttributeValue("coding_end").toString();
			    }
			    else {
				codingStart = record.getAttributeValue("coding_end").toString();
				codingEnd = record.getAttributeValue("coding_start").toString();
			    }
			    writer.println(partialStart + start + "\t" + partialEnd + end + "\t" + geneType);
			}
			
			writer.println("\t\t\tgene\t" + sourceId);
			
			if (geneType.compareTo("CDS") == 0) {
			    /* RULE : Include the translation table to handle, e.g.,
			       stop codons translated as tryptophan */
			    String sequenceSoTerm = record.getAttributeValue("sequence_so_term").toString();
			    
			    // Use the mitochondrial translation table if this is a mitochondrial gene
			    if (sequenceSoTerm.compareTo("mitochondrial_chromosome") == 0) {
				writer.println("\t\t\ttransl_table\t" + record.getAttributeValue("mitochondrial_genetic_code"));
			    }
			    else {
				writer.println("\t\t\ttransl_table\t" + record.getAttributeValue("genetic_code"));
			    }
			    
			    // RULE : Include protein id
			}
		    }
		    else {
			// RULE : Include pseudo flag for pseudogenes
			writer.println("\t\t\tpseudo");
		    }

		    // RULE : Include product field
		    writer.println("\t\t\tproduct\t" + product);

		    // RULE : Include notes
		    TableValue comments = record.getTableValue("Notes");
		    for (Map<String, AttributeValue> comment : comments) {
			String commentString = comment.get("comment_string").toString();
			if (commentString != null)
			    writer.println("\t\t\tnote\t" + commentString);
		    }
		    
		    // RULE : Include EC numbers
		    try {
			TableValue ecNumbers = record.getTableValue("EcNumber");
			for (Map<String, AttributeValue> ecNumber : ecNumbers) {
			    writer.println("\t\t\tEC_number\t" + ecNumber.get("ec_number"));
			}
		    }
		    catch (WdkModelException ex) {
			// Trap exception?  In case model doesn't have EC numbers.
		    }
		    
		    // RULE : Include GO terms as dbxrefs
		    try {
			TableValue goTerms = record.getTableValue("GoTerms");
			for (Map<String, AttributeValue> goTerm : goTerms) {
			    writer.println("\t\t\tdb_xref\t" + goTerm.get("go_id"));
			}
		    }
		    catch (WdkModelException ex) {
			// Trap exception?  In case model doesn't have EC numbers.
		    }

		    // RULE : Provide ncRNA class for ncRNAs
		    if (geneType.compareTo("ncRNA") == 0) {
			writer.println("\t\t\tncRNA_class\tother");
		    }
		    
		    lastSequenceId = recordSequenceId;
		}
	    }
	}
	
	return lastSequenceId;
    }
}
