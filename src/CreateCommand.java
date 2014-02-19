import java.util.ArrayList;

import com.foundationdb.sql.types.DataTypeDescriptor;

public class CreateCommand {

	public String tableName = null;
	public ArrayList<Attribute> attributeList;
	
	CreateCommand() {
		attributeList  = new ArrayList<Attribute>();
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public ArrayList<Attribute> getAttributeList() {
		return attributeList;
	}
	
	public void insertIntoList(String aName, DataTypeDescriptor aType) {
		Attribute A = new Attribute(aName,aType);
		attributeList.add(A);
	}
	
	public void setTableName(String tName) {
		tableName = tName;
	}
	
}
