import com.foundationdb.sql.types.DataTypeDescriptor;

// maps attributeName with attributeType
public class Attribute {
	
	public String attributeName;
	public String attributeType;
	public DataTypeDescriptor attrType;
	
	Attribute(String aName, String aType) {
		attributeName = aName;
		attributeType = aType;
	}
	
	Attribute(String aName, DataTypeDescriptor aType) {
		attributeName = aName;
		attrType = aType;
	}
	
	public String getType() {
		return attributeType;
	}
	
	public DataTypeDescriptor getDataType() {
		return attrType;
	}
	
	public String getName() {
		return attributeName;
	}
	
}
