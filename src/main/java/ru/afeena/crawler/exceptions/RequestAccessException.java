package ru.afeena.crawler.exceptions;

public class RequestAccessException extends Exception {
	private long user;
	public RequestAccessException(long uid){
		this.user=uid;
	}

	public long getUser(){
		return this.user;
	}
}
