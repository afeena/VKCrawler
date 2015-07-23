package ru.afeena.crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.afeena.crawler.exceptions.ResponseError;
import ru.afeena.crawler.wall.Post;

import java.util.ArrayList;
import java.util.Iterator;


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

	public ArrayList<Long> parseFriend(String friend){
		JSONObject jsonObj;
		JSONArray response;
		ArrayList<Long> parsed_friend=new ArrayList<Long>();



		try {

			jsonObj = (JSONObject) parser.parse(friend);
			if(jsonObj.containsKey("error")) return null;
			response = (JSONArray) jsonObj.get("response");
			parsed_friend.addAll(response);
		}
		catch (org.json.simple.parser.ParseException e) {
			System.out.println(e);
		}
		return parsed_friend;
	}

	public ArrayList<Post> parseWall(String wall, long count){
		JSONObject jsonObj;
		JSONArray response;
		ArrayList<Post> result=new ArrayList<Post>();

		try {

			jsonObj = (JSONObject) parser.parse(wall);
			response = (JSONArray) jsonObj.get("response");


			for (int i = 1; i <= count; i++) {

				JSONObject raw_post = (JSONObject) response.get(i);				
				String searched_key;
				String post_type = raw_post.get("post_type").toString();

				if(post_type.equals("copy"))
					searched_key="copy_text";
				else
					searched_key="text";

				String text=raw_post.get(searched_key).toString();
				long date=(Long)raw_post.get("date");

				Post post = new Post(date,text);
				result.add(post);


			}

		} catch (org.json.simple.parser.ParseException e) {
			System.out.println(e);
		}
		return result;
	}
}
