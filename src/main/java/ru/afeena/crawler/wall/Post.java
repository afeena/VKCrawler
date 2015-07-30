package ru.afeena.crawler.wall;

/**
 * Created by mainn_000 on 24.07.2015.
 */
public class Post {
	private int id;
	private int uid;
	private int date;


	private String text;
	private String type;

	public Post(int id,int uid, int date, String text, String type) {
		this.id=id;
		this.uid = uid;
		this.date = date;
		this.text = text;
		this.type = type;
	}

	public int getId(){
		return this.id;
	}
	public int getUid(){
		return this.uid;
	}
	public int getDate() {

		return this.date;
	}
	public String getText() {

		return this.text;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public boolean equals(Object obj) {
		Post post = (Post) obj;
		return this.text.equals(post.text) && this.date == post.date;
	}
}
