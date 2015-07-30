package ru.afeena.crawler.users;

import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;
import ru.afeena.crawler.wall.WallTaskManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Users implements Runnable {
	private static ArrayList<Users> instances;
	private HashSet<Integer> handledUsers;
	private ConcurrentLinkedQueue<Integer> unhandledUsers;
	private Thread thread;
	private Integer startId;

	public static void init(Integer... uids) throws Exception {
		if (instances != null) throw new Exception("Instances already initialized");
		instances = new ArrayList<>(uids.length);
		for (Integer uid : uids) {
			Users users = new Users(uid);
			instances.add(users);
		}
	}

	public Users(Integer startId) {
		this.handledUsers = new HashSet<>();
		this.unhandledUsers = new ConcurrentLinkedQueue<>();
		this.startId = startId;
		this.thread = new Thread(this);
		this.thread.start();

	}

	public void run() {

		this.unhandledUsers.clear();
		this.unhandledUsers.add(startId);

		while (!this.unhandledUsers.isEmpty()) {
			Integer currentUid = this.unhandledUsers.poll();
			if (handledUsers.contains(currentUid))
				continue;

			WallTaskManager.getInstance().putIntoUnhandled(currentUid);
			this.getFriend(currentUid);
		}
	}

	private void getFriend(Integer uid) {

		String friend = VkApi.getFriends(uid);
		if(friend==null) {
			unhandledUsers.add(uid);
			return;
		}
		VkResponseParser friendParser = new VkResponseParser();

		ArrayList<Integer> friendUids = friendParser.parseFriend(friend);

		if (friendUids == null) {
			this.handledUsers.add(uid);
			return;
		}

		for (Integer current : friendUids) {
			if (this.handledUsers.contains(current))
				continue;

			this.unhandledUsers.add(current);
		}

	}
}




