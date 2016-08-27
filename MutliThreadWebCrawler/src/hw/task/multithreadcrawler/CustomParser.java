package hw.task.multithreadcrawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

//Task Done By Sunil Panchal and Dhaval Shah
public class CustomParser {

	public void extractFromFile(String fileName,String linkUrl) throws IOException,
			SAXException, TikaException {

		Parser parser = new AutoDetectParser();
		long start = System.currentTimeMillis();
		BodyContentHandler handler = new BodyContentHandler(10000000);
		Metadata metadata = new Metadata();
		FileInputStream content = new FileInputStream(fileName);
		
		parser.parse(content, handler, metadata, new ParseContext());

		Collection<JSONObject> items = new ArrayList<JSONObject>();
		JSONObject obj = new JSONObject();

		for (String name : metadata.names()) {

			//System.out.println(name + ":\t" + metadata.get(name));
			obj.put("Url", linkUrl);
			obj.put(name, metadata.get(name));
		    items.add(obj);
		}
		//objOuter.put("result", items);
		saveMetaData(obj);
	//	System.out.println(String.format(
			//	"------------ Processing took %s millis\n\n",
				//System.currentTimeMillis() - start));
	}

	@SuppressWarnings("unchecked")
	public void saveMetaData(JSONObject obj) throws IOException {
		
		try {
			File userDataFile = new File("../MutliThreadWebCrawler/OutputData.json");
			if (userDataFile.exists()) {
				FileWriter file = new FileWriter(userDataFile,true);

				file.write(obj.toJSONString() + ",");
				file.close();
			}
			//System.out.println("Successfully Copied JSON Object to File...");
			//System.out.println("\nJSON Object: " + obj);
		} catch (Exception e) {
		}
	}

}
