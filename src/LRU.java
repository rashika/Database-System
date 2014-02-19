import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

@SuppressWarnings("serial")
public class LRU<K,V> extends LinkedHashMap<K,V>{
	
	private int maxCap;
	
	LRU(int init_cap, int maxcap) {
		super(init_cap, 0.75f, true);
		maxCap = maxcap;
	}
	
	@Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
    	return size() > maxCap;
    }
    
    public V getEldestEntry() {
    	Set<Map.Entry<K, V>> entrySet = this.entrySet();
		Iterator<Map.Entry<K, V>> it = entrySet.iterator();
		if (it.hasNext())
			return it.next().getValue();
		return null;
    }

}
