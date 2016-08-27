package hw.task.multithreadcrawler;

import hw.crawler.threads.*;

import java.net.*;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

//Task Done By Dhaval Shah
// I have used the mechanism of two queue for multithreading
//First I will put start url in first queue and all links find from that url will store in second queue
//I have designed flow chart to explain how this code works
//I have taken reference from http://andreas-hess.info/programming/webcrawler/ for multithreading
//But I have customized the code by storing the files in local storage in json format
//I have used data dump class to parse the json and displaying on console
public class Crawler implements MessageReceiver {
	public Crawler(Queue q, int maxLevel, int maxThreads)
			throws InstantiationException, IllegalAccessException {
		ThreadController tc = new ThreadController(CrawlerThread.class,
				maxThreads, maxLevel, q, 0, this);
	}

	public void finishedAll() {

	}

	public void receiveMessage(Object o, int threadId) {

		System.out.println("[" + threadId + "] " + o.toString());
	}

	public void finished(int threadId) {
		System.out.println("[" + threadId + "] finished");
	}

	public static void main(String[] args) {
		try {
			Options options = new Options();

			options.addOption("d", true, "display current time");

			options.addOption("u", true, "display current time");
			options.addOption("e", false, "display current time");

			CommandLineParser parser = new BasicParser();

			CommandLine cmd = parser.parse(options, args);

			int maxLevel = 2;
			int maxThreads = 10;
			int maxDoc = -1;

			if (args.length == 4) {

				maxThreads = 10;
			}

			if (args.length >= 3) {

				maxDoc = -1;
			}

			if (args.length >= 2) {
				URLQueue q = new URLQueue();
				maxLevel = Integer.parseInt(cmd.getOptionValue("d"));
				q.setFilenamePrefix("c");
				q.setMaxElements(maxDoc);
				if(cmd.hasOption("e"))
				{
					SaveURL.setExtractValue(1);
				}
				else
				{
					SaveURL.setExtractValue(0);
				}
				q.push(new URL(cmd.getOptionValue("u")), 0);
				new Crawler(q, maxLevel, maxThreads);
				return;
			}
		} catch (Exception e) {
			System.err.println("An error occured: ");
			e.printStackTrace();

		}
		System.err.println("Usage: java Crawler -d depth -u url");
		System.err
				.println("Crawls the web for html page, jpeg pictures and mpeg, avi or wmv movies.");
		System.err
				.println("-1 for either maxLevel or maxDoc means 'unlimited'.");
	}
}
