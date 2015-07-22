package ru.afeena.crawler.exceptions;

/**
 * Created by mainn_000 on 20.07.2015.
 */
public class EmptyWallException extends Exception {
	private long user;
	public EmptyWallException(long uid){
		this.user=uid;
	}

	public long getUser(){
		return this.user;
	}
}
