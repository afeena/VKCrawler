package ru.afeena.crawler.storage;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import ru.afeena.crawler.wall.Post;

import java.util.ArrayList;


public class Database {
	private MongoDatabase database;

	public Database() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("crawler");
	}

	public void insertPostCollection(ArrayList<Post> posts) {
		if (database.getCollection("posts") == null) {
			database.createCollection("posts");
		}
		MongoCollection collection = database.getCollection("posts");

		UpdateOptions updateOptions = new UpdateOptions();
		updateOptions.upsert(true);

		for (Post post : posts) {
			Document postDocument = new Document();
			postDocument.put("title", "Post");
			postDocument.put("id", post.getId());
			postDocument.put("uid", post.getUid());
			postDocument.put("date", post.getDate());
			postDocument.put("text", post.getText());
			postDocument.put("type", post.getType());

			collection.updateMany(
				Filters.and(
					Filters.eq("id", post.getId()),
					Filters.eq("uid", post.getUid())
				),
				new Document("$set", postDocument),
				updateOptions
			);

		}
	}
}

