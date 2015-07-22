package ru.afeena.crawler.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class VkApi {

	private static final String api_url = "https://api.vk.com/method/";

	public static String getFriends(long uid) {
		String method_name = "friends.get";

		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", String.valueOf(uid));

		return VkApi.connect(method_name, params);
	}

	public static String getSubscription(long uid, long offset, long count) {
		String method_name = "users.getSubscriptions";

		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", String.valueOf(uid));
		params.put("extended", "1");
		params.put("offset", String.valueOf(offset));
		params.put("count", String.valueOf(count));

		return VkApi.connect(method_name, params);
	}

	public static String getInfo(long uid) {
		String method_name = "users.get";

		Map<String, String> params = new HashMap<String, String>();
		params.put("user_ids", String.valueOf(uid));
		params.put("fields", "bdate,city,country,sex");

		return VkApi.connect(method_name, params);
	}


	public static String getWall(long uid, long offset, long count) {
		String method_name = "wall.get";

		Map<String, String> params = new HashMap<String, String>();
		params.put("owner_id", String.valueOf(uid));
		params.put("offset", String.valueOf(offset));
		params.put("count", String.valueOf(count));
		params.put("filter", "owner");

		return VkApi.connect(method_name, params);
	}

	private static String connect(String method_name, Map params) {
		Set<String> keys = params.keySet();
		String parametres = new String();
		for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
			String key = it.next();
			Object parameter = params.get(key);
			parametres += "&" + key + "=" + parameter.toString();

		}

		String url_string = api_url + method_name + "?" + parametres;
		HttpURLConnection connection = null;
		InputStream input;
		int content_size;
		byte[] buffer = new byte[1024];
		int read;

		try {
			URL url = new URL(url_string);
			connection = (HttpURLConnection) url.openConnection();

			input = connection.getInputStream();
			content_size = connection.getContentLength();
			byte[] result = new byte[content_size];
			ByteBuffer result_buffer = ByteBuffer.wrap(result);

			while ((read = input.read(buffer)) != -1) {
				result_buffer.put(buffer, 0, read);

			}

			return new String(result, StandardCharsets.UTF_8);


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != connection) {
				connection.disconnect();
			}
		}
		return null;
	}
}
