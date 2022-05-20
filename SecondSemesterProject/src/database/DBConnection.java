package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DBConnection {

	private static final String driver = "jdbc:sqlserver:hildur.ucn.dk"; 
	private static final String databaseName = ";databaseName=CSC-CSD-S212_10407574";

	private static String userName = ";user=CSC-CSD-S212_10407574";
	private static String password = ";password=Password1!";
	private static String encrypt = ";encrypt=false";
	
	private static Connection con;
	private DatabaseMetaData dma;

	private static DBConnection instance = null;

	private DBConnection() {
		String url = driver + databaseName + userName + password + encrypt;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			System.out.println("Driver class loaded");

		} catch (Exception e) {
			System.out.println("Could not load driver class");
			System.out.println(e.getMessage());
		}

		try {
			
			SQLServerDataSource ds = new SQLServerDataSource();
			ds.setUser("CSC-CSD-S212_10407574");
			ds.setPassword("Password1!");
			ds.setServerName("hildur.ucn.dk");
			ds.setDatabaseName("CSC-CSD-S212_10407574");
			ds.setEncrypt(false);
			
			con = ds.getConnection();
			con.setAutoCommit(true);
			dma = con.getMetaData();
			System.out.println("Driver name: " + dma.getDriverName());
			System.out.println("Driver version: " + dma.getDriverVersion());
			System.out.println("Product name: " + dma.getDatabaseProductName());
			System.out.println("Product version: " + dma.getDatabaseProductVersion());
		} catch (Exception e) {
			System.out.println("Could not connect to the database");
			System.out.println(url);
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
//		try {
//			con.close();
//			instance = null;
//			System.out.println("Database connection was closed");
//		} catch (Exception e) {
//			System.out.println("Error trying to close the connection to the database.");
//			e.printStackTrace();
//		}
	}

	public Connection getDBcon() {
		return con;
	}

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		return instance;
	}
}