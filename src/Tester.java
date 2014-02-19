import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Tester {

	public static void main(String[] args) throws Exception{
		int input[] = {0,1,2,1,2,2,3,41,9,39,28,1,30,38,39,31,-1, 42,28};

		List<String> lines = new ArrayList<String>();
		RandomAccessFile f = new RandomAccessFile("/tmp/countries.csv", "r");
		String s = new String();
		while((s=f.readLine()) != null) {
			lines.add(s);
		}

		DBSystem dbs = new DBSystem();
		dbs.readConfig("/tmp/config.txt");
		dbs.populateDBInfo();
		for(int inp : input) {
			if(inp !=-1) {
				String op = dbs.getRecord("countries",inp);
				if(lines.get(inp).equals(op)){

				} else {
					System.out.println("Fail for record number " + inp + " expected = "+ lines.get(inp) + " actual= " + op);
					System.exit(-1);
				}
			}
			else
				dbs.insertRecord("countries", "record");
		}
		//String query = "create table studentdata(name STRING,roll_number int,marks double,phone_number int,sex boolean, a char, f float);";
		//dbs.queryType("select * from employee,projects where pid=ppid group by Name having name>monam order by rollno");
		String query = "select * from countries where name=code groupby code having ID=50;";
		query = query.replaceAll("groupby", "group by");
		query = query.replaceAll("orderby", "order by");
		query = query.replace(query.substring(query.length()-1),"");
		dbs.queryType(query);
		System.exit(0);
	}

}
