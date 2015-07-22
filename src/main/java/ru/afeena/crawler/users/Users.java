package ru.afeena.crawler.users;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.afeena.crawler.exceptions.RequestAccessException;
import ru.afeena.crawler.wall.WallTaskManager;

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

		while(!ru.afeena.crawler.users.Users.unhandled_users.isEmpty()){
			WallTaskManager.unhandled_users.addAll(unhandled_users);
			synchronized (taskmanager){
				taskmanager.notifyAll();
			}
			parseFriend(ru.afeena.crawler.users.Users.unhandled_users.poll());
		}


		//String info = ru.afeena.crawler.api.VkApi.getInfo(uid);

	}

	private void parseFriend(long uid){
			System.out.println("parse friend");
			String friend = ru.afeena.crawler.api.VkApi.getFriends(uid);
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

