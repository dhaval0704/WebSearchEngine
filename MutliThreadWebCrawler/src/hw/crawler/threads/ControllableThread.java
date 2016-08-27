package hw.crawler.threads;

abstract public class ControllableThread extends Thread {
	protected int level;
	protected int id;
	protected Queue queue;
	protected ThreadController tc;
	protected MessageReceiver mr;

	public void setId(int _id) {
		id = _id;
	}

	public void setLevel(int _level) {
		level = _level;
	}

	public void setQueue(Queue _queue) {
		queue = _queue;
	}

	public void setThreadController(ThreadController _tc) {
		tc = _tc;
	}

	public void setMessageReceiver(MessageReceiver _mr) {
		mr = _mr;
	}

	public ControllableThread() {
	}

	public void run() {

		for (Object newTask = queue.pop(level); newTask != null; newTask = queue
				.pop(level)) {
			mr.receiveMessage(newTask, id);
			process(newTask);
			if (tc.getMaxThreads() > tc.getRunningThreads()) {
				try {
					tc.startThreads();
				} catch (Exception e) {
					System.err.println("[" + id + "] " + e.toString());
				}
			}
		}

		tc.finished(id);
	}

	public abstract void process(Object o);
}
