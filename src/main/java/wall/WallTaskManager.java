package wall;

import api.VkApi;
import exceptions.EmptyWallException;
import exceptions.RequestAccessException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import users.Users;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WallTaskManager implements Runnable {
	private static final int MAX_POST_COUNT = 100;
	public static ConcurrentLinkedQueue<Long> unhandled_users = new ConcurrentLinkedQueue<Long>();

	private JSONParser parser;

	public WallTaskManager() {

		parser = new JSONParser();

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
			long uid = unhandled_users.poll();
			String wall = VkApi.getWall(uid, 0, 1);

			JSONObject jsonObj;
			JSONArray response;
			Long unread_post;
			ArrayList<Long> offsets = new ArrayList<Long>();

			try {

				jsonObj = (JSONObject) parser.parse(wall);
				if (jsonObj.containsKey("error")) throw new RequestAccessException(uid);
				response = (JSONArray) jsonObj.get("response");


				if ( response.size()==1 ) throw new EmptyWallException(uid);

				Long posts_count = (Long) response.get(0);
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


			} catch (EmptyWallException e){
				Users.handled_users.put(uid,0);
			}
			catch (RequestAccessException e) {
				WallTaskManager.unhandled_users.remove(e.getUser());
			} catch (Exception e) {

				System.out.println("WallTaskManager"+e);
			}


		}
	}
}
