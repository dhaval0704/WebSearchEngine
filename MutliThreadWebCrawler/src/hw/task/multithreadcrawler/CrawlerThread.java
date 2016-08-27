package hw.task.multithreadcrawler;

import hw.crawler.threads.*;

import java.net.*;
import java.io.*;
import java.util.UUID;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CrawlerThread extends ControllableThread {
	public void process(Object o) {
		try {
			URL pageURL = (URL) o;
			String filename = pageURL.getFile().toLowerCase();

			filename = filename.replace('/', '-');
			filename = UUID.randomUUID().toString();

			System.out.println("Saving to file " + filename + "\n");
			try {

				SaveURL.writeURLtoFile(pageURL, filename);

				if (SaveURL.getExtractValue() == 1) {
					// Now this code is used to extract metadata from files
					// which are stored in local storage.
					CustomParser objparser = new CustomParser();
					objparser.extractFromFile("../MutliThreadWebCrawler/"
							+ filename, pageURL.toString());
				}

			} catch (Exception e) {
				System.out.println("Saving to file " + filename + " from URL "
						+ pageURL.toString() + " failed due to a "
						+ e.toString());
			}
			String mimetype = pageURL.openConnection().getContentType();
			if (!mimetype.startsWith("text"))
				return;

			String rawPage = SaveURL.getURL(pageURL);
			String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

			Vector links = SaveURL.extractLinks(rawPage, smallPage);

			for (int n = 0; n < links.size(); n++) {
				try {

					URL link = new URL(pageURL, (String) links.elementAt(n));

					// Checking the maxLevel attribute
					if (tc.getMaxLevel() == -1)
						queue.push(link, level);
					else
						queue.push(link, level + 1);
				} catch (MalformedURLException e) {

				}
			}
		} catch (Exception e) {

		}
	}

}
