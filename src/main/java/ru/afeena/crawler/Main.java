package ru.afeena.crawler;

import ru.afeena.crawler.users.Users;
import ru.afeena.crawler.wall.WallParser;
import ru.afeena.crawler.wall.WallTaskManager;

public class Main {
	public static void main(String[] args) throws Exception {


		WallParser.init(8);
		Users.init(15595268,1);

	}
}
