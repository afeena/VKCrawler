package ru.afeena.crawler.wall;


import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WallTaskManager implements Runnable {
	private static final int MAX_POST_COUNT = 100;
	private static final Integer ACCESS_DENIED = -1;
	private static final Integer EMPTY_WALL = 0;
	private Thread thread;

	private static final WallTaskManager instance;

	static {
		instance = new WallTaskManager();

	}

	private ConcurrentLinkedQueue<Integer> unhandledUsers;
	private HashSet<Integer> handledUsers;

	private WallTaskManager() {
		unhandledUsers = new ConcurrentLinkedQueue<>();
		handledUsers = new HashSet<>();
		thread=new Thread(this);
		thread.start();
	}

	public static WallTaskManager getInstance() {
		return instance;
	}

	public synchronized void run() {
		try {
			while (true) {
				wait();
				this.usersExecute();
			}
		} catch (InterruptedException ignored) {
		}
	}

	public synchronized void putIntoUnhandled(Integer uid) {
		this.unhandledUsers.add(uid);
		this.notify();
	}

	private void usersExecute() {
		VkResponseParser parser = new VkResponseParser();
		while (!unhandledUsers.isEmpty()) {
			Integer uid = unhandledUsers.poll();
			if (handledUsers.contains(uid))
				continue;

			String wall = VkApi.getWall(uid, 0, 1);
			ArrayList<Integer> offsets = new ArrayList<>();
			Integer postsCount = parser.postCount(wall);
			Integer unreadPost;

			if (postsCount.equals(ACCESS_DENIED) || postsCount.equals(EMPTY_WALL)) {
				this.handledUsers.add(uid);
				continue;
			}

			for (int i = 0; i < postsCount; i += 100) {
				offsets.add(i);
			}

			unreadPost = postsCount;
			for (Integer offset : offsets) {
				if (unreadPost > MAX_POST_COUNT)
					WallParser.addTask(new WallTask(uid, offset, MAX_POST_COUNT));
				else
					WallParser.addTask(new WallTask(uid, offset, unreadPost.intValue()));

				unreadPost -= MAX_POST_COUNT;
			}
		}
	}


}
