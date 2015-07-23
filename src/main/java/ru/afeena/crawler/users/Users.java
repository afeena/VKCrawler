package ru.afeena.crawler.users;

import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;
import ru.afeena.crawler.exceptions.RequestAccessException;
import ru.afeena.crawler.wall.WallTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Users {
	public static HashMap<Long, Integer> handled_users = new HashMap<Long, Integer>();
	public static ConcurrentLinkedQueue<Long> unhandled_users = new ConcurrentLinkedQueue<Long>();


	public Users() {
		WallTaskManager taskmanager = new WallTaskManager();
		Thread thread = new Thread(taskmanager);
		thread.start();


		Users.unhandled_users.add(Long.parseLong("15595268"));

		while (!Users.unhandled_users.isEmpty()) {
			WallTaskManager.unhandled_users.addAll(Users.unhandled_users);
			synchronized (taskmanager) {
				taskmanager.notifyAll();
			}
			getFriend(Users.unhandled_users.poll());
		}




	}

	public void getFriend(long uid) {

		String friend = VkApi.getFriends(uid);
		VkResponseParser friend_parser = new VkResponseParser();

		ArrayList<Long> friend_uids=friend_parser.parseFriend(friend);

		try {
			if (friend_uids.equals(null)) throw new RequestAccessException(uid);

			Iterator<Long> uids_iterator = friend_uids.iterator();
			while (uids_iterator.hasNext()){
				Long current = uids_iterator.next();
				if(!Users.handled_users.containsKey(current)|| !Users.unhandled_users.contains(current))
				Users.unhandled_users.add(current);
			}

		} catch (RequestAccessException e) {

		}
	}
}

