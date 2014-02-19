
public class PageInMem {
	private Pages P;
	private int pos;
	
	PageInMem(Pages pg, int position) {
		P = pg;
		pos = position;
	}
	public int getpos() {
		return pos;
	}
	public Pages getPage() {
		return P;
	}
}
