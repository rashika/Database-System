import java.util.Iterator;


public class MainMemory {
	
	private static int maxSize;	
	private LRU<Integer,PageInMem> lru;
	
	MainMemory(int numPages) {
		maxSize = numPages;
		lru = new LRU<Integer, PageInMem>(0, maxSize);
	}
	
	public Pages getPage(int pid, String tableName, int flag) {
		
		if(lru.containsKey(pid)) {
			if(flag == 0)
			System.out.println("HIT");
			PageInMem PM = lru.get(pid);
			return PM.getPage();
		}
		else {
			PageInMem key = insertPage(pid ,tableName);
			if(flag == 0)
			System.out.println("Miss "+ key.getpos());
			return key.getPage();
		}
	}
	
	protected PageInMem insertPage(int pid ,String tableName ) {
		
		Iterator<Table> it = DBSystem.Tables.iterator();
		Pages P = null;
		PageInMem PM = null, key = null;
		
		while(it.hasNext()) {
			Table T = it.next();
			if(T.getTableName().equals(tableName)) {
				P = T.getPageFromId(pid);
				break;
			}
		}
		
		if(lru.size() < maxSize) {
			PM = new PageInMem(P, lru.size());
			lru.put(pid, PM);
		}
		else {
			key = lru.getEldestEntry();
			PM = new PageInMem(P, key.getpos());
			lru.put(pid, PM);
		}
		return PM;
	}
	
}
