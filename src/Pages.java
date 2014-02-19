import java.util.ArrayList;

/* 
 * typical Page format containing the startRecId, endRecId and
 * records in page
 */
public class Pages {
	
	private int startId, endId, availableSpace, pageId;
	public ArrayList<String> recordsInPage;
	
	Pages(int startRec, int pageSize, int pid) {
		startId = startRec;
		endId = startRec - 1;
		availableSpace = pageSize;
		pageId = pid;
		recordsInPage = new ArrayList<String>();
	}
	
	public void insertRec(String Rec) {
		recordsInPage.add(Rec);
		endId = endId + 1;
		availableSpace = availableSpace - Rec.length();
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
	
	public ArrayList<String> getRec() {
		return recordsInPage;
	}
	
	public Boolean checkSpace(int len) {
		if(availableSpace > len) {
			return true;
		}
		return false;
	}
	
	public int getAvailableSpace() {
		return availableSpace;
	}
	
	public String getRecFromId(int recId) {
		return recordsInPage.get(recId-startId);
	}

}
