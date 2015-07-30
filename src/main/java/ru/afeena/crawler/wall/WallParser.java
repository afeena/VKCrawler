package ru.afeena.crawler.wall;

import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;
import ru.afeena.crawler.storage.Database;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class WallParser implements Runnable {

	private static ArrayList<WallParser> instances;
	private static AtomicInteger currentThread = new AtomicInteger(0);
	private Database database;
	private ConcurrentLinkedQueue<WallTask> tasks;

	public static void init(int threadCount) throws Exception {
		if (instances != null) throw new Exception("Instances already initialized");
		instances = new ArrayList<>(threadCount);
		for (int i = 0; i < threadCount; i++) {
			WallParser wallParser = new WallParser();
			Thread thread = new Thread(wallParser);
			thread.start();
			instances.add(wallParser);
		}

	}

	public static void addTask(WallTask task) {
		int thread = currentThread.addAndGet(1) % instances.size();
		WallParser currentParser = instances.get(thread);
		currentParser.put(task);

	}

	private synchronized void put(WallTask task) {
		this.tasks.add(task);
		this.notify();

	}

	private WallParser() {

		database = new Database();
		tasks = new ConcurrentLinkedQueue<>();

	}

	public synchronized void run() {
		try {
			while (true) {
				wait();
				this.writeWallPostsToDB();
			}

		} catch (InterruptedException ignored) {
		}


	}

	private void writeWallPostsToDB() {
		while (!tasks.isEmpty()) {
			WallTask task = tasks.poll();

			String wall = VkApi.getWall(task.getUid(), task.getOffset(), task.getCount());

			if (wall == null) {
				tasks.add(task);
				continue;
			}

			VkResponseParser parser = new VkResponseParser();
			ArrayList<Post> parsedPostsResultList = parser.parseWall(wall, task.getUid(), task.getCount());
			database.insertPostCollection(parsedPostsResultList);


		}
	}

}




