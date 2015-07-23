package ru.afeena.crawler.wall;

/**
 * Created by mainn_000 on 24.07.2015.
 */
public class Post {
	long date;
	String text;
	public Post(long date, String text){
		this.date =date;
		this.text=text;
	}

	public void setDate(long value){
		this.date =value;
	}

	public void setText(String value){
		this.text=value;
	}

	public String getText(){
		return this.text;
	}
	public long getDate(){
		return this.date;
	}

	@Override
	public boolean equals(Object obj) {
		Post post = (Post) obj;
		return this.text.equals(post.text)&&this.date==post.date;
	}
}
