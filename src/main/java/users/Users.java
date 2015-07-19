package users;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import exceptions.RequestAccessException;
import wall.WallTaskManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Users {
	public static HashMap<Long,Integer> handled_users = new HashMap<Long, Integer>();
	public static ConcurrentLinkedQueue<Long> unhandled_users=new ConcurrentLinkedQueue<Long>();
	private JSONParser parser;

	public Users() {
		WallTaskManager taskmanager = new WallTaskManager();
		Thread thread = new Thread(taskmanager);
		thread.start();

		parser = new JSONParser();
		Users.unhandled_users.add(Long.parseLong("15595268"));

		while(!Users.unhandled_users.isEmpty()){
			WallTaskManager.unhandled_users.addAll(unhandled_users);
			synchronized (taskmanager){
				taskmanager.notifyAll();
			}
			parseFriend(Users.unhandled_users.poll());
		}


		//String info = api.VkApi.getInfo(uid);

	}

	private void parseFriend(long uid){
			System.out.println("parse friend");
			String friend = api.VkApi.getFriends(uid);
			JSONObject jsonObj;
			JSONArray response;



			try {

				jsonObj = (JSONObject) parser.parse(friend);
				if(jsonObj.containsKey("error")) throw new RequestAccessException(uid);
				response = (JSONArray) jsonObj.get("response");
				Users.unhandled_users.addAll(response);









			} catch (RequestAccessException e){

			}
			catch (Exception e) {
				System.out.println(e);
			}

		}
	}

