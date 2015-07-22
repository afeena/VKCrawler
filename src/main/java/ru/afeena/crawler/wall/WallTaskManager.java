package ru.afeena.crawler.wall;


import ru.afeena.crawler.VkResponseParser;
import ru.afeena.crawler.api.VkApi;
import ru.afeena.crawler.exceptions.EmptyWallException;
import ru.afeena.crawler.exceptions.RequestAccessException;
import ru.afeena.crawler.users.Users;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WallTaskManager implements Runnable {
	private static final int MAX_POST_COUNT = 100;
	public static ConcurrentLinkedQueue<Long> unhandled_users = new ConcurrentLinkedQueue<Long>();


	public WallTaskManager() {


	}

	public synchronized void run() {
		while (true) {
			try {
				wait();
			} catch (InterruptedException ignored) {
			}
			usersExecute();
		}
	}

	private void usersExecute() {

		while (!unhandled_users.isEmpty()) {
			VkResponseParser parse = new VkResponseParser();
			long uid = unhandled_users.poll();
			String wall = VkApi.getWall(uid, 0, 1);
			Long unread_post;
			ArrayList<Long> offsets = new ArrayList<Long>();
			Long posts_count = parse.postCount(wall);

			try {
				if (posts_count.equals(-1)) throw new RequestAccessException(uid);
				if (posts_count.equals(0)) throw new EmptyWallException(uid);


				Users.handled_users.put(uid, posts_count.intValue());

				unread_post = posts_count;

				for (long i = 0; i < posts_count; i += 100) {
					offsets.add(i);
				}

				for (int i = 0; i < offsets.size(); i++) {
					if (unread_post > MAX_POST_COUNT) {
						WallParser.addTask(new WallTask(uid, offsets.get(i), MAX_POST_COUNT));
					} else {
						WallParser.addTask(new WallTask(uid, offsets.get(i), unread_post.intValue()));

					}

					unread_post -= MAX_POST_COUNT;

				}


			} catch (EmptyWallException e) {
				Users.handled_users.put(uid, 0);
			} catch (RequestAccessException e) {
				WallTaskManager.unhandled_users.remove(e.getUser());
			} catch (Exception e) {

				System.out.println("WallTaskManager" + e);
			}


		}
	}
}
