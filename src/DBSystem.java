import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class DBSystem {

	private int pageSize;
	private int numPages;
	private String pathForData;
	public static ArrayList<Table> Tables;
	public MainMemory MM;

	public void readConfig(String configFilePath) throws IOException {

		Scanner scanner = null;
		File configFile = new File(configFilePath);

		try {

			scanner = new Scanner(configFile);
			scanner.useDelimiter("[,\\s]+");

			/* 
			 * read pageSize, numPages, pathForData from Config file
			 * and store
			 */
			scanner.next("PAGE_SIZE");
			pageSize = scanner.nextInt();
			scanner.next("NUM_PAGES");
			numPages = scanner.nextInt();
			scanner.next("PATH_FOR_DATA");
			pathForData = scanner.next();

			// list of all tables in Config file
			Tables = new ArrayList<Table>();

			while(scanner.hasNext("BEGIN")) {
				scanner.next("BEGIN");
				Table newTable = new Table(scanner.next());

				while(!scanner.hasNext("END")) {
					// store MetaData in Table
					Attribute newAttribute = new Attribute(scanner.next(), scanner.next());
					newTable.insertAttribute(newAttribute);
				}

				scanner.next("END");
				Tables.add(newTable);
			}

			MM = new MainMemory(numPages);

		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void populateDBInfo() {

		Iterator<Table> it = Tables.iterator();
		String fileName, line;
		Pages P;
		TableList TL=null;
		Table T;
		int recCount, pageCount;

		while(it.hasNext()) {
			// for all the tables
			T = it.next();

			P = null;
			TL = null;
			fileName = pathForData + T.getTableName() + ".csv";
			recCount = 0;
			pageCount = 0;

			try {
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				// create an empty page
				P = new Pages(recCount,pageSize,pageCount++);

				while ((line = br.readLine()) != null) { 
					// process the line.
					if(P.checkSpace(line.length())) {
						P.insertRec(line);
						recCount += 1;
					}
					else{
						// save the current page
						TL = new TableList(P.getStartId(),P.getEndId(),P.getPageId(),P.getAvailableSpace());
						T.insertPage(P);
						T.insertTableList(TL);
						/*System.out.println(T.noOfPages);
						for (String s : P.recordsInPage ){
							System.out.println(s);
						}*/

						T.noOfPages+=1;

						// start a new page to store records.
						P = new Pages(recCount,pageSize,pageCount++);
						if(P.checkSpace(line.length())){
							P.insertRec(line);
							recCount += 1;
						}
					}	
				}
				br.close();
			} catch (FileNotFoundException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

			// saving the last page
			if(P.getAvailableSpace() < pageSize){
				TL = new TableList(P.getStartId(),P.getEndId(),P.getPageId(),P.getAvailableSpace());
				T.insertPage(P);
				T.insertTableList(TL);
				/*System.out.println(T.noOfPages);
				for (String s : P.recordsInPage ){
					System.out.println(s);
				}*/

				T.noOfPages+=1;
			}

		}
	}

	public String getRecord(String tableName, int recordId) {

		Iterator<Table> it = Tables.iterator();
		int pageID = -2;
		Pages P = null;

		while(it.hasNext()) {
			Table T = it.next();
			if(T.getTableName().equals(tableName)) {
				pageID = T.getPageId(recordId);
				break;
			}
		}

		if(pageID == -1) {
			System.out.println("Invalid Record Request");
			return null;
		}
		else if(pageID == -2) {
			System.out.println("Invalid Table Name");
			return null;
		}
		else {
			P = MM.getPage(pageID, tableName, 0);
			return P.getRecFromId(recordId);
		}
	}

	public void insertRecord(String tableName, String record) {

		Iterator<Table> it = Tables.iterator();
		TableList TL = null;
		Pages P = null;
		Table T = null;

		while(it.hasNext()) {
			T = it.next();
			if(T.getTableName().equals(tableName)) {
				TL = T.getLastPage();
				break;
			}
		}

		if(record.length() < TL.getAvailableSpace()) {
			P = MM.getPage(TL.getPageId(), tableName, 1);
			P.insertRec(record);
			T.updatePage(P);
			TL.setEndId(P.getEndId());
			TL.setAvailableSpace(P.getAvailableSpace());
		}
		else {
			P = new Pages(TL.getEndId()+1,pageSize,TL.getPageId()+1);
			P.insertRec(record);
			TL = new TableList(P.getStartId(),P.getEndId(),P.getPageId(),P.getAvailableSpace());
			P = MM.getPage(TL.getPageId(), tableName, 1);
			T.insertPage(P);
			T.insertTableList(TL);
		}

	}

	public void queryType(String query) {

		// check the type of query
		if(query.indexOf("select") == 0) {
			selectCommand(query);
		}
		else if(query.indexOf("create") == 0) {
			createCommand(query);
		}
		else {
			System.out.println("Invalid Query!");
		}
	}

	public void createCommand(String query) {

		int tableExists = -1;
		Parser sqlP = new Parser();
		int invalidQuery = sqlP.ParseQuery(query);

		CreateCommand createInfo;
		createInfo = sqlP.getcreateInfo();

		Iterator<Table> it = Tables.iterator();

		if(invalidQuery == 1) {
			while(it.hasNext()) {
				if(it.next().getTableName().equals(createInfo.getTableName())) {
					tableExists = 1;
				}
			}

			if(tableExists == -1) {

				File csvFile = new File(pathForData+createInfo.getTableName()+".csv");
				File dataFile = new File(pathForData+createInfo.getTableName()+".data");
				File configFile = new File("/tmp/config.txt");

				try {
					BufferedWriter csvOutput = new BufferedWriter(new FileWriter(csvFile));
					BufferedWriter dataOutput = new BufferedWriter(new FileWriter(dataFile));
					PrintWriter configOutput = new PrintWriter(new BufferedWriter(new FileWriter(configFile,true)));

					configOutput.println("BEGIN");
					System.out.println("QueryType:create");
					System.out.println("Tablename:"+createInfo.getTableName());
					configOutput.println(createInfo.getTableName());

					ArrayList<Attribute> attributeList = createInfo.getAttributeList();
					System.out.print("Attribute:");

					for(int i=0; i<(attributeList.size()-1); i++) {
						Attribute A = attributeList.get(i);
						System.out.print(A.getName()+" "+A.getDataType()+",");
						configOutput.println(A.getName()+", "+A.getDataType());
						dataOutput.write(A.getName()+":"+A.getDataType()+",");
					}
					Attribute A = attributeList.get(attributeList.size()-1);
					System.out.println(A.getName()+" "+A.getDataType());
					configOutput.println(A.getName()+", "+A.getDataType());
					dataOutput.write(A.getName()+":"+A.getDataType());
					configOutput.println("END");

					configOutput.close();
					csvOutput.close();
					dataOutput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				System.out.println("Query Invalid");
			}
		}
	}

	public void selectCommand(String query) {
		Parser sqlP = new Parser();
		int invalidQuery = sqlP.ParseQuery(query);

		SelectCommand selectInfo;
		selectInfo = sqlP.getselectInfo();


		if (invalidQuery == 1) {

			ValidateSelect validateSelect = new ValidateSelect(selectInfo, Tables);
			boolean validTable = true, validCol = true, validWhere = true, validHaving = true;
			validTable = validateSelect.validateTableName();
			validCol = validateSelect.validateAttribute();
			validWhere = validateSelect.validateWhere();
			validHaving = validateSelect.validateHaving();
			
			if(validTable && validCol && validWhere && validHaving) {

				System.out.println("QueryType:select");

				System.out.print("Tablename:");
				selectInfo.printArrayList(selectInfo.getTableName());			
				System.out.print("Columns:");
				selectInfo.printArrayList(selectInfo.getColumns());
				System.out.print("Distinct:");
				if(selectInfo.isDistinct())
					System.out.println("True");
				else
					System.out.println("NA");
				System.out.print("Condition:");
				selectInfo.printCondArrayList(selectInfo.getCondition());
				System.out.print("Orderby:");
				selectInfo.printArrayList(selectInfo.getOrderBy());
				System.out.print("GroupBy:");
				selectInfo.printArrayList(selectInfo.getGroupBy());
				System.out.print("Having:");
				selectInfo.printCondArrayList(selectInfo.getHaving());
			}
			else {
				System.out.println("Query Invalid");
			}
		}
	}

}
