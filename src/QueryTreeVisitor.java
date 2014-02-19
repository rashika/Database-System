import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.BinaryComparisonOperatorNode;
import com.foundationdb.sql.parser.BinaryOperatorNode;
import com.foundationdb.sql.parser.ColumnDefinitionNode;
import com.foundationdb.sql.parser.CreateTableNode;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.GroupByColumn;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.OrderByColumn;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.Visitable;


public class QueryTreeVisitor implements com.foundationdb.sql.parser.Visitor {

	public static CreateCommand createInfo = new CreateCommand();
	public static SelectCommand selectInfo = new SelectCommand();
	private int where = -1;
	private int having = -1;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Visitable visit(Visitable visitable) throws StandardException {

		QueryTreeNode node = (QueryTreeNode) visitable;	

		if(node.getNodeType() == NodeTypes.CREATE_TABLE_NODE) {
			CreateTableNode createNode = (CreateTableNode) node;
			createInfo.setTableName(createNode.getFullName());
		} 
		else if(node.getNodeType() == NodeTypes.COLUMN_DEFINITION_NODE) {
			ColumnDefinitionNode attribute = (ColumnDefinitionNode) node;
			createInfo.insertIntoList(attribute.getName(),attribute.getType());
		}		
		else if (node.getNodeType() == NodeTypes.SELECT_NODE) {
			SelectNode i = (SelectNode) node;
			if(i.isDistinct()) 
				selectInfo.setDistinct();
			if(i.getWhereClause() != null) {
				where = i.getWhereClause().getNodeType();
				i.getWhereClause().setUserData("Where");
			}
			if(i.getHavingClause() != null)
				having =  i.getHavingClause().getNodeType();
		}
		else if (node.getNodeType() == NodeTypes.GROUP_BY_COLUMN) {
			GroupByColumn i = (GroupByColumn) node;
			selectInfo.insertGroupBy(i.getColumnName());
		}
		else if (node.getNodeType() == NodeTypes.ORDER_BY_COLUMN) {
			OrderByColumn i = (OrderByColumn) node;
			selectInfo.insertOrderBy(i.getExpression().getColumnName());
		}
		else if (node.getNodeType() == NodeTypes.FROM_LIST) {
			FromList i = (FromList) node;
			int c=0;
			while(c < i.size()) {
				selectInfo.insertTableName(i.get(c++).getExposedName());
			}

		}
		else if(node.getNodeType() == where && node.getUserData() == "Where") {
			BinaryComparisonOperatorNode i = (BinaryComparisonOperatorNode) node;
			String leftop = i.getLeftOperand().toString().split("[\n ]")[1];
			String rightop = i.getRightOperand().toString().split("[\n ]")[1];
			selectInfo.insertCondition(new Pair(i.getOperator(),i.getMethodName()));
			String l[] = i.getLeftOperand().getClass().toString().split("[.]");
			selectInfo.insertCondition(new Pair(leftop,l[l.length-1]));
			String r[] = i.getRightOperand().getClass().toString().split("[.]");
			selectInfo.insertCondition(new Pair(rightop, r[r.length-1]));
		}
		else if(node.getNodeType() == having) {
			BinaryOperatorNode i = (BinaryOperatorNode) node;
			String leftop = i.getLeftOperand().toString().split("[\n ]")[1];
			String rightop = i.getRightOperand().toString().split("[\n ]")[1];
			selectInfo.insertHaving(new Pair(i.getOperator(),i.getMethodName()));
			String l[] = i.getLeftOperand().getClass().toString().split("[.]");
			selectInfo.insertHaving(new Pair(leftop,l[l.length-1]));
			String r[] = i.getRightOperand().getClass().toString().split("[.]");
			selectInfo.insertHaving(new Pair(rightop,r[r.length-1]));
		}
		else if(node.getNodeType() == NodeTypes.RESULT_COLUMN_LIST) {
			ResultColumnList i = (ResultColumnList) node;
			if(i.get(0).getName() == null) {
				selectInfo.insertColumnName("*");
			}
			else {
				int c=0;
				while(c < i.size()) {
					selectInfo.insertColumnName(i.get(c++).getName());
				}
			}
		}
		return visitable;
	}

	@Override
	public boolean visitChildrenFirst(Visitable node) {
		return false;
	}

	@Override
	public boolean stopTraversal() {
		return false;
	}

	@Override
	public boolean skipChildren(Visitable node) throws StandardException {
		return false;
	}
}
