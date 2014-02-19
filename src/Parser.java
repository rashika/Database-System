import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;

public class Parser
{
	private String sqlQuery;
	public CreateCommand createInfo;
	public SelectCommand selectInfo;

    public int ParseQuery(String query) {
    	
    	sqlQuery = query;
        boolean echoStatement = false;

        SQLParser parser = new SQLParser();
        StatementNode stmt = null;

		try {
			stmt = parser.parseStatement(sqlQuery);
		} catch (StandardException e) {
			// Auto-generated catch block

			System.out.println("Query Invalid");
			return -1;
		}
        
        if(echoStatement) {
             System.out.println(sqlQuery.substring(stmt.getBeginOffset(), stmt.getEndOffset() + 1));
        }

        try {
        	stmt.accept(new QueryTreeVisitor());
		} catch (StandardException e) {
			// Auto-generated catch block
			System.out.println("Query Invalid");
			//e.printStackTrace();
		}
        
        createInfo = QueryTreeVisitor.createInfo;
        selectInfo = QueryTreeVisitor.selectInfo;
        return 1;
    }
    
    public CreateCommand getcreateInfo() {
    	return createInfo;
    }
    
    public SelectCommand getselectInfo() {
    	return selectInfo;
    }

}