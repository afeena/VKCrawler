package ru.afeena.crawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.afeena.crawler.exceptions.ResponseError;
import ru.afeena.crawler.wall.Post;

import java.util.ArrayList;
import java.util.Arrays;


public class VkResponseParser {
	private JSONParser parser;

	public VkResponseParser() {
		parser = new JSONParser();

	}


	public int postCount(String wall) {
		JSONArray responseBody;
		int postsCount;

		responseBody = getResponseBody(wall);
		if(responseBody==null) return -1;

		if (responseBody.size() == 1)
			return 0;

		postsCount = ((Long) responseBody.get(0)).intValue();


		return postsCount;
	}

	public ArrayList<Integer> parseFriend(String friend) {
		JSONArray responseBody;
		ArrayList<Integer> parsedFriendList = new ArrayList<>();

		responseBody = getResponseBody(friend);
		if (responseBody == null) return null;


		for (Object aResponseBody : responseBody) {
			parsedFriendList.add(((Long) aResponseBody).intValue());
		}

		return parsedFriendList;
	}

	public ArrayList<Post> parseWall(String wall, int uid, int count) {

		JSONArray responseBody;
		ArrayList<Post> parsedWallPostsResult = new ArrayList<>();

		responseBody = getResponseBody(wall);

		if (responseBody == null) return null;


		for (int i = 1; i <= count; i++) {
			JSONObject postData;

			int id;
			int date;

			String postType;
			String searchedKey;
			String text;

			postData = (JSONObject) responseBody.get(i);
			postType = postData.get("post_type").toString();
			id = ((Long)postData.get("id")).intValue();
			date = ((Long)postData.get("date")).intValue();

			if (postType.equals("copy"))
				searchedKey = "copy_text";
			else
				searchedKey = "text";

			if (postData.containsKey(searchedKey)) {
				text = postData.get(searchedKey).toString();
			} else {
				text = "";
			}

			Post post = new Post(id, uid, date, text, postType);
			parsedWallPostsResult.add(post);

		}
		return parsedWallPostsResult;
	}

	private JSONArray getResponseBody(String vkEntity) {
		JSONObject vkEntityParsedObject=new JSONObject();
		JSONArray responseBody = new JSONArray();

		try {
			vkEntityParsedObject = (JSONObject) parser.parse(vkEntity);
			if (vkEntityParsedObject.containsKey("error")) {
				throw new ResponseError();
			}
			responseBody = (JSONArray) vkEntityParsedObject.get("response");

		} catch (org.json.simple.parser.ParseException parseError) {
			System.out.println(parseError);
		}
		catch (ResponseError responseError){
			//JSONObject errorBody=(JSONObject) vkEntityParsedObject.get("error");
			return null;

		}

		return responseBody;
	}
}
