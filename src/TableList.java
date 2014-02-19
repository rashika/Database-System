
public class TableList {
	
	private int startId, endId, availableSpace;
	private int pageId;
	
	TableList(int sid, int eid, int pid, int abs) {
		startId = sid;
		endId = eid;
		pageId = pid;
		availableSpace = abs;
	}
	
	public int getPageId() {
		return pageId;
	}
	
	public int getStartId() {
		return startId;
	}
	
	public int getEndId() {
		return endId;
	}
	
	public int getAvailableSpace() {
		return availableSpace;
	}
	
	public void setEndId(int eid) {
		endId = eid;
	}
	
	public void setAvailableSpace(int abs) {
		availableSpace = abs;
	}

}
