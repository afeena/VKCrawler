package wall;

public class WallTask {
	private long uid;
	private int count;
	private long offset;

	public WallTask(long uid, long offset, int count) {
		this.uid = uid;
		this.offset = offset;
		this.count = count;
	}

	public long getUid() {
		return this.uid;
	}

	public long getCount() {
		return this.count;
	}

	public long getOffset() {
		return this.offset;
	}


}
