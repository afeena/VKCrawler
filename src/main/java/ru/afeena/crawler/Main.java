package ru.afeena.crawler;

import ru.afeena.crawler.users.Users;
import ru.afeena.crawler.wall.WallParser;
import ru.afeena.crawler.wall.WallTaskManager;

public class Main {
	public static void main(String[] args) throws Exception {


		WallParser.init(1);
		Users.init(15595268);
//		WallTaskManager taskmanager = WallTaskManager.getInstance();
//		Thread thread = new Thread(taskmanager);
//		thread.start();


//		Thread.sleep(Long.MAX_VALUE);
	}
}
