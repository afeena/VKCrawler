package ru.afeena.crawler.wall;

import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class WallParser implements Runnable {

	public static ArrayList<WallParser> instances;
	public static AtomicInteger current_thread = new AtomicInteger(0);

	private ConcurrentLinkedQueue<WallTask> tasks;

	public static void init(int thread_count) throws Exception {
		if (instances != null) throw new Exception("Instances already initialized");
		instances = new ArrayList<WallParser>(thread_count);
		for (int i = 0; i < thread_count; i++) {
			WallParser wall_parser = new WallParser();
			Thread thread = new Thread(wall_parser);
			thread.start();
			instances.add(wall_parser);
		}

	}

	public static void addTask(WallTask task) {
		int thread = current_thread.addAndGet(1) % instances.size();
		WallParser current_parser = instances.get(thread);
		current_parser.tasks.add(task);
		synchronized (current_parser) {
			current_parser.notify();
		}

	}

	private WallParser() {

		tasks = new ConcurrentLinkedQueue<WallTask>();

	}

	public synchronized void run() {

		while (true) {
			try {
				wait();
			} catch (InterruptedException ignored) {

			}
			while (!tasks.isEmpty()) {
				WallTask task = tasks.poll();

				String wall = VkApi.getWall(task.getUid(), task.getOffset(), task.getCount());

				VkResponseParser parse = new VkResponseParser();
				ArrayList<Post> res = parse.parseWall(wall, task.getCount());

//				for (String elem : res) {
//					System.out.println(elem);
//				}

			}
		}

	}


}

