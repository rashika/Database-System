import java.util.ArrayList;
import java.util.Iterator;

import com.foundationdb.sql.types.DataTypeDescriptor;



public class ValidateSelect {

	ArrayList<Table> tables = null;
	SelectCommand selectInfo = null;
	ArrayList<Attribute> totAttributes = new ArrayList<Attribute>();
	ValidateSelect(SelectCommand sInfo, ArrayList<Table> tab) {
		selectInfo = sInfo;
		tables = tab;
	}

	public boolean validateTableName() {

		Iterator<String> fromList = selectInfo.getTableName().iterator();
		while(fromList.hasNext()) {

			String tName = fromList.next();
			boolean tableExists = false;
			Iterator<Table> it = tables.iterator();

			while(it.hasNext()) {
				if(it.next().getTableName().equals(tName)) {
					tableExists = true;
				}
			}
			if(!tableExists) {
				return false;
			}
		}
		return true;

	}

	public boolean validateAttribute() {
		ArrayList<String> colNames = new ArrayList<String>();

		Iterator<String> fromList = selectInfo.getTableName().iterator();
		Iterator<Table> it = tables.iterator();

		while(fromList.hasNext()) {
			String tName = fromList.next();
			while(it.hasNext()) {
				Table T = it.next();
				if(T.getTableName().equals(tName)) {
					Iterator<Attribute> Meta = T.getMetaData().iterator();
					while(Meta.hasNext()) {
						Attribute M = Meta.next();
						colNames.add(M.getName());
						totAttributes.add(M);
					}
				}
			}
		}
		Iterator<String> colList = selectInfo.getColumns().iterator();
		while(colList.hasNext()) {
			String columnName = colList.next();
			if(columnName == "*") {
				selectInfo.setColumnNames(colNames);
				return true;
			}
			boolean has = false;
			Iterator<String> totColList = colNames.iterator();
			while(totColList.hasNext()) {
				if(totColList.next().toLowerCase().equals(columnName)) {
					has = true;
				}
			}
			if(!has) {
				return false;
			}
		}

		return true;
	}

	public boolean validateCondition(Iterator<Pair> condList) {

		Pair op, cond1, cond2;
		while(condList.hasNext()) { 
			op = condList.next();
			cond1 = condList.next();
			cond2 = condList.next();
			String lopType = cond1.getR();
			String ropType = cond2.getR();
			if(lopType.equals("ColumnReference")) {
				lopType = lookup(cond1.getL());
				if(lopType.equals("int")) {
					lopType = "Numeric";
				}
				lopType = lopType + "ConstantNode";
			}
		if(ropType.equals("ColumnReference")) {
			ropType = lookup(cond2.getL());	
			if(ropType.equals("int")) {
				ropType = "Numeric";
			}
			ropType = ropType + "ConstantNode";
		}
		if(!lopType.toLowerCase().equals(ropType.toLowerCase())) {
			return false;
		}
	}
	return true;
}
public boolean validateWhere() {
	Iterator<Pair> condList = selectInfo.getCondition().iterator();
	return validateCondition(condList);
}
public boolean validateHaving() {
	Iterator<Pair> condList = selectInfo.getHaving().iterator();
	return validateCondition(condList);
}

public String lookup(String colName) {
	Iterator<Attribute> it = totAttributes.iterator();

	while(it.hasNext()) {
		Attribute next = it.next();

		if(next.getName().toLowerCase().equals(colName)) {
			return next.getType(); 
		}
	}
	return "String";
}

}
