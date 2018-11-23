package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public static final String JDBCDriverSQLite = "org.sqlite.JDBC";
	public static final String JDBCURLSQLite = "jdbc:sqlite:database.db";

	public static final String JDBCDriverMySQL = "com.mysql.jdbc.Driver";
	public static final String JDBCURLMySQL = "jdbc:mysql://localhost:3306/Esercizijava?user=Nicolo&password=prova";

	protected Statement statement;
	protected Connection connection;
	
	public DBManager(String JDBCDriver, String JDBCURL) throws ClassNotFoundException, SQLException {
		Class.forName(JDBCDriver);
		connection = DriverManager.getConnection(JDBCURL);
		statement = connection.createStatement();
		statement.setQueryTimeout(30); 
		showMetadata();
	}

	public DBManager(String JDBCDriver, String JDBCURL, 
			int resultSetType, int resultSetConcurrency) throws ClassNotFoundException, SQLException {
		Class.forName(JDBCDriver);
		connection = DriverManager.getConnection(JDBCURL);
		statement = connection.createStatement(resultSetType, resultSetConcurrency);
		statement.setQueryTimeout(30); 
		showMetadata();
	}
	
	public void showMetadata() throws SQLException {
		DatabaseMetaData md = connection.getMetaData();

		System.out.println("-- ResultSet Type --");
		System.out.println("Supports TYPE_FORWARD_ONLY: "
				+ md.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
		System.out.println("Supports TYPE_SCROLL_INSENSITIVE: "
				+ md.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
		System.out.println("Supports TYPE_SCROLL_SENSITIVE: "
				+ md.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

		System.out.println("-- ResultSet Concurrency --");
		System.out.println("Supports CONCUR_READ_ONLY: "
				+ md.supportsResultSetType(ResultSet.CONCUR_READ_ONLY));
		System.out.println("Supports CONCUR_UPDATABLE: "
				+ md.supportsResultSetType(ResultSet.CONCUR_UPDATABLE));
	}

	public ResultSet executeQuery(String query) throws SQLException {
		return statement.executeQuery(query);
	}

	public int executeUpdate(String query) throws SQLException {
		return statement.executeUpdate(query);
	}

	public void close() throws SQLException {
		if (connection != null) {
			statement.close();
			connection.close();
		}
	}
}
