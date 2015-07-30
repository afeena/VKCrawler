package ru.afeena.crawler.wall;

public class WallTask {
	private int uid;
	private int count;
	private int offset;

	public WallTask(int uid, int offset, int count) {
		this.uid = uid;
		this.offset = offset;
		this.count = count;
	}

	public int getUid() {
		return this.uid;
	}

	public int getCount() {
		return this.count;
	}

	public int getOffset() {
		return this.offset;
	}


}
