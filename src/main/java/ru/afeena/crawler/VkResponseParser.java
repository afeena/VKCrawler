package ru.afeena.crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.afeena.crawler.exceptions.EmptyWallException;
import ru.afeena.crawler.exceptions.RequestAccessException;
import ru.afeena.crawler.exceptions.ResponseError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class VkResponseParser {
	private JSONParser parser;
	public VkResponseParser(){
		parser = new JSONParser();

	}


	public long postCount(String wall){
		JSONObject jsonObj;
		JSONArray response;
		Long posts_count=new Long(0);

		try {

			jsonObj = (JSONObject) parser.parse(wall);
			if (jsonObj.containsKey("error")) throw new ResponseError();
			response = (JSONArray) jsonObj.get("response");


			if (response.size() == 1) return 0;

			 posts_count = (Long) response.get(0);


		}
		catch (org.json.simple.parser.ParseException e) {

			System.out.println("WallTaskManager" + e);
		}
		catch (ResponseError e){
			return -1;
		}
		return posts_count;
	}

	public ArrayList<String> parseWall(String wall, long count){
		JSONObject jsonObj;
		JSONArray response;
		ArrayList<String> result=new ArrayList<String>();

		try {

			jsonObj = (JSONObject) parser.parse(wall);
			response = (JSONArray) jsonObj.get("response");


			for (int i = 1; i <= count; i++) {
				JSONObject post = (JSONObject) response.get(i);
				Iterator<String> keys = post.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();

					try {

						if (key.equals("text")) {
							String text = post.get("text").toString();
							result.add(text);
							break;
						}
					} catch (Exception e) {
						System.out.println(e);
					}


				}
			}

		} catch (org.json.simple.parser.ParseException e) {
			System.out.println(e);
		}
		return result;
	}
}
