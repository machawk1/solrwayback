package dk.kb.netarchivesuite.solrwayback.parsers;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import dk.kb.netarchivesuite.solrwayback.UnitTestUtils;
import dk.kb.netarchivesuite.solrwayback.export.StreamingSolrWarcExportBufferedInputStream;
import dk.kb.netarchivesuite.solrwayback.solr.SolrGenericStreaming;
import dk.kb.netarchivesuite.solrwayback.solr.SolrStreamingExportClient;
import org.apache.commons.io.FileUtils;

import dk.kb.netarchivesuite.solrwayback.facade.Facade;
import dk.kb.netarchivesuite.solrwayback.properties.PropertiesLoader;
import dk.kb.netarchivesuite.solrwayback.service.dto.ArcEntry;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestExportWarcStreaming extends UnitTestUtils {

  @Test
  public void testSingleRecordStreamingExport() throws Exception {
    final String WARC = getFile("compressions_warc/transfer_compression_none.warc.gz").getCanonicalPath();
    final long OFFSET = 881;
    final int EXPECTED_CONTENT_LENGTH = 246;
    final int EXPECTED_EXPORT_LENGTH = 1102;

    byte[] upFrontBinary;
    {
      ArcEntry warcEntry = WarcParser.getWarcEntry(WARC, OFFSET, true);
      upFrontBinary = warcEntry.getBinary();
      assertEquals("Length for up front load should be as expected", EXPECTED_CONTENT_LENGTH, upFrontBinary.length);
    }

    {
      SolrDocument doc = new SolrDocument();
      doc.addField("id", "MockedDocument");
      doc.addField("source_file_path", WARC);
      doc.addField("source_file_offset", OFFSET);

      SolrDocumentList docs = new SolrDocumentList();
      docs.setMaxScore(1.0f);
      docs.setNumFound(1);
      docs.setStart(System.currentTimeMillis());
      docs.add(doc);

      SolrGenericStreaming mockedSolr = mock(SolrGenericStreaming.class);
      when(mockedSolr.nextDocuments()).thenReturn(docs).thenReturn(null); // Return docs on first call, then null

      StreamingSolrWarcExportBufferedInputStream exportStream = new
              StreamingSolrWarcExportBufferedInputStream(mockedSolr, 1);

      byte[] exportedBytes = new byte[EXPECTED_EXPORT_LENGTH];
      long exported = exportStream.read(exportedBytes);
      assertEquals("Expected the right number of bytes to be read", EXPECTED_EXPORT_LENGTH, exported);
      // TODO: This should return -1 to signal EOF, not 0!
      assertEquals("There should be no more content in the export stream", 0, exportStream.read());

      // TODO: Compare the binary part (using offsets in exportedBytes vs. the full UpFrontBinary)
      System.out.println(new String(exportedBytes));
    }

  }

  public static void main(String[] args) throws Exception{
    PropertiesLoader.initProperties();
    String source_file_path="/home/teg/workspace/solrwayback/storedanske_export-00000.warc";
    int offset = 515818793;
    ArcEntry warcEntry = WarcParser.getWarcEntry(source_file_path,offset,true);
    
    byte[] bytes = warcEntry.getBinary(); // <--------- The binary
    String fileFromBytes = "image1.jpg";
    String fileFromBytesStream = "image2.jpg";
    String fileFromBytesWarcInputStream = "image3.jpg";
    String fileFromBytesWarcInputStream2 = "image4.jpg";
    
    /*
    FileUtils.writeByteArrayToFile(new File(fileFromBytes), bytes);
    
    
    FileOutputStream fos = new FileOutputStream(fileFromBytesStream);
    fos.write(bytes);
    fos.close();
    
    InputStream is = new ByteArrayInputStream(bytes);         
    System.out.println(is.read());
    System.out.println(is.read());
    System.out.println(is.read());
    System.out.println(is.read());
    System.out.println(is.read());
    System.out.println(is.read());
    
    
    FileUtils.copyInputStreamToFile( is, new File(fileFromBytesWarcInputStream)); 
    */
    
    try (InputStream is1 = Facade.exportWarcStreaming(false, false, "hash:\"sha1:PROTE66RZ6GDXPZI3ZAHG6YPCXRKZMEN\"")) {
      FileUtils.copyInputStreamToFile(is1, new File("export_final.warc"));
    }
    
  }
  
}
