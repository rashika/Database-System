import java.util.ArrayList;

import com.foundationdb.sql.types.DataTypeDescriptor;

public class SelectCommand {

	private ArrayList<String> tableName;
	private ArrayList<String> columnNames;
	private boolean isDistinct;
	private ArrayList<Pair> condition;
	private ArrayList<String> orderBy;
	private ArrayList<String> groupBy;
	private ArrayList<Pair> having;

	SelectCommand() {
		tableName = new ArrayList<String>();
		columnNames = new ArrayList<String>();
		condition = new ArrayList<Pair>();
		orderBy = new ArrayList<String>();
		groupBy = new ArrayList<String>();
		having = new ArrayList<Pair>();
		isDistinct = false;
	}

	public boolean isDistinct() {
		return isDistinct;
	}

	public void setDistinct() {
		isDistinct = true;
	}
	
	public void setColumnNames(ArrayList<String> cNames) {
		columnNames = cNames;
	}

	public ArrayList<String> getTableName() {
		return tableName;
	}

	public void insertTableName(String tName) {
		tableName.add(tName);
	}

	public ArrayList<String> getColumns() {
		return columnNames;
	}

	public void insertColumnName(String cName) {
		columnNames.add(cName);
	}

	public ArrayList<Pair> getCondition() {
		return condition;
	}

	public void insertCondition(Pair cond) {
		condition.add(cond);
	}

	public ArrayList<String> getOrderBy() {
		return orderBy;
	}

	public void insertOrderBy(String cName) {
		orderBy.add(cName);
	}

	public ArrayList<String> getGroupBy() {
		return groupBy;
	}

	public void insertGroupBy(String cName) {
		groupBy.add(cName);
	}

	public ArrayList<Pair> getHaving() {
		return having;
	}

	public void insertHaving(Pair pair) {
		having.add(pair);
	}

	public void printArrayList(ArrayList<String> List) {
		String S;
		for(int i=0; i<(List.size()-1); i++) {
			S = List.get(i);
			System.out.print(S+",");
		}
		if(List.size() > 0) {
			S = List.get(List.size()-1);
			System.out.println(S);
		}
		else 
			System.out.println("NA");
	}

	public void printCondArrayList(ArrayList<Pair> List) {
		Pair op,cond1,cond2;
		for(int i=0; i<(List.size()-3); i++) {
			op = List.get(i);
			cond1 = List.get(++i);
			cond2 = List.get(++i);
			System.out.print(cond1.getL()+op.getL()+cond2.getL()+",");
		}
		if(List.size()>2) {
			op = List.get(List.size()-3);
			cond1 = List.get(List.size()-2);
			cond2 = List.get(List.size()-1);
			System.out.println(cond1.getL()+op.getL()+cond2.getL());
		}
		else 
			System.out.println("NA");
	}

}