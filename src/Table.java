import java.util.ArrayList;
import java.util.Iterator;

// stores Table information: tableName, attributeList, pageList
public class Table {
	
	private String tableName;
	private ArrayList<Attribute> metaData = new ArrayList<Attribute>();
	private ArrayList<Pages> pageList = new ArrayList<Pages>();
	public int noOfPages;
	public ArrayList<TableList> tableList = new ArrayList<TableList>();
	
	Table(String name) {
		tableName=name;
		noOfPages=0;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public ArrayList<Attribute> getMetaData() {
		return metaData;
	}
	
	public void insertAttribute(Attribute A) {
		metaData.add(A);
	}
	
	public void insertPage(Pages P) {
		pageList.add(P);
	}
	
	public void insertTableList(TableList T) {
		tableList.add(T);
	}
	
	public int numPages(){
		return noOfPages;
	}
	
	public Pages getPage(int recId) {
		Iterator<Pages> it = pageList.iterator();
		
		while(it.hasNext()) {
			Pages P = it.next();
			if (recId >= P.getStartId() && recId <= P.getEndId()) {
				return P;
			}
		}
		return null;
	}
	
	public TableList getLastPage() {
		return tableList.get(tableList.size()-1);
	}
	
	public int getPageId(int recId) {
		Iterator<TableList> it = tableList.iterator();
		
		while(it.hasNext()) {
			TableList TL = it.next();
			if (recId >= TL.getStartId() && recId <= TL.getEndId()) {
				return TL.getPageId();
			}
		}
		return -1;
	}
	
	public Pages getPageFromId(int pageId) {
		Iterator<Pages> it = pageList.iterator();
		
		while(it.hasNext()) {
			Pages P = it.next();
			if (P.getPageId() == pageId) {
				return P;
			}
		}
		return null;
	}
	
	public void updatePage(Pages P) {
		pageList.set(pageList.size()-1,P);
	}
}
