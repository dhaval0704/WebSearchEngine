package hw.task.multithreadcrawler;

import java.net.*;
import java.util.*;

public class URLQueue implements hw.crawler.threads.Queue {

	//We are going to maintain two queues.
	LinkedList evenQueue;
	LinkedList oddQueue;
	Set gatheredLinks;
	Set processedLinks;

	int maxElements;

	String filenamePrefix;

	public URLQueue() {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = -1;
		filenamePrefix = "";
	}

	public URLQueue(int _maxElements, String _filenamePrefix) {
		evenQueue = new LinkedList();
		oddQueue = new LinkedList();
		gatheredLinks = new HashSet();
		processedLinks = new HashSet();
		maxElements = _maxElements;
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Setter for filename prefix
	 */
	public void setFilenamePrefix(String _filenamePrefix) {
		filenamePrefix = _filenamePrefix;
	}

	/**
	 * Getter for filename prefix
	 */
	public String getFilenamePrefix() {
		return filenamePrefix;
	}

	/**
	 * Set the maximum number of allowed elements
	 */
	public void setMaxElements(int _maxElements) {
		maxElements = _maxElements;
	}

	public Set getGatheredElements() {
		return gatheredLinks;
	}

	public Set getProcessedElements() {
		return processedLinks;
	}

	public int getQueueSize(int level) {
		if (level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
	}

	public int getProcessedSize() {
		return processedLinks.size();
	}

	public int getGatheredSize() {
		return gatheredLinks.size();
	}

	public synchronized Object pop(int level) {
		String s;
		// try to get element from the appropriate queue
		// is the queue is empty, return null
		if (level % 2 == 0) {
			if (evenQueue.size() == 0) {
				return null;
			} else {
				s = (String) evenQueue.removeFirst();
			}
		} else {
			if (oddQueue.size() == 0) {
				return null;
			} else {
				s = (String) oddQueue.removeFirst();
			}
		}

		try {
			URL url = new URL(s);
			processedLinks.add(s);
			return url;
		} catch (MalformedURLException e) {

			return null;
		}
	}

	public synchronized boolean push(Object url, int level) {
		if (maxElements != -1 && maxElements <= gatheredLinks.size())
			return false;
		String s = ((URL) url).toString();
		if (gatheredLinks.add(s)) {
			if (level % 2 == 0) {
				evenQueue.addLast(s);
			} else {
				oddQueue.addLast(s);
			}
			return true;
		} else {

			return false;
		}
	}

	public synchronized void clear() {
		evenQueue.clear();
		oddQueue.clear();
	}
}
