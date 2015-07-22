package ru.afeena.crawler;

import ru.afeena.crawler.users.Users;
import ru.afeena.crawler.wall.WallParser;

/**
 * Created by afeena on 10.07.2015.
 */
public class Main {
	public static void main(String[] args) throws Exception{

		WallParser.init(1);
		new Users();
	}
}
