/**
 * 
 */
package com.km.appd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * @author Madan Kavarthapu
 *
 */
public class GenericMysqlMetricsSender {

	final static Logger log = Logger.getLogger(GenericMysqlMetricsSender.class);

	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private String DB_URL = "jdbc:mysql://%s:%s/";
	private Connection connection = null;
	private Statement statement;
	private PreparedStatement eventsPreparedStatement;

	private String DB_CREATE_SQL = "CREATE DATABASE %s";
	private String TABLE_CREATE_EVENTS_SQL = "CREATE TABLE IF NOT EXISTS  %s.events ("
			+ "  `time` timestamp NOT NULL,"
			+ "  `application` varchar(30),"
			+ "  `text` varchar(40)," + "  `title` varchar(20))";
	private String TABLE_CREATE_JMETER_SQL = "CREATE TABLE IF NOT EXISTS %s.jmeter ("
			+ "  `tStamp` timestamp NOT NULL,"
			+ "  `application` varchar(30),"
			+ "  `avg` decimal(7,3),"
			+ "  `count` int(11),"
			+ "  `countError` int(11),"
			+ "  `endedT` int(11),"
			+ "  `hit` int(11),"
			+ "  `max` decimal(7,3),"
			+ "  `maxAT` decimal(7,3),"
			+ "  `meanAT` decimal(7,3),"
			+ "  `min` decimal(7,3),"
			+ "  `minAT` decimal(7,3),"
			+ "  `pct90` decimal(7,3),"
			+ "  `pct95` decimal(7,3),"
			+ "  `pct99` decimal(7,3),"
			+ "  `startedT` int(11),"
			+ "  `status`varchar(5),"
			+ "  `transaction` varchar(256),"
			+ "  `responsecode` varchar(256),"
			+ "  `responsemessage` varchar(512))";

	private String INSERT_EVENTS_SQL = "INSERT INTO %s.events"
			+ "(time, application, text, title) VALUES" + "(?,?,?,?)";

	private String INSERT_TRANSACTIONS_SQL = "INSERT INTO %s.jmeter ( %s ) VALUES ( %s )";

	public void setup(String mysqlHost, String mysqlPort, String appdSchema,
			String userName, String password) {
		boolean schemaExists = false;
		try {
			Class.forName(JDBC_DRIVER);

			connection = DriverManager.getConnection(
					String.format(DB_URL, mysqlHost, mysqlPort), userName,
					password);

			ResultSet resultSet = connection.getMetaData().getCatalogs();

			// iterate each schema in the ResultSet
			log.debug("**List of available Schemas**");
			while (resultSet.next()) {
				// Get the database name, which is at position 1
				String databaseName = resultSet.getString(1);
				log.debug(databaseName);
				if (databaseName.equalsIgnoreCase(appdSchema)) {
					log.debug("Database already exists");
					schemaExists = true;
					break;
				}
			}
			resultSet.close();

			if (!schemaExists) {
				// Create a new Schema with the given name.
				statement = connection.createStatement();
				statement.executeUpdate(String
						.format(DB_CREATE_SQL, appdSchema));
				log.debug("Database created successfully..");
				// Create Required Tables in DB.
				statement.executeUpdate(String.format(TABLE_CREATE_EVENTS_SQL,
						appdSchema));
				statement.executeUpdate(String.format(TABLE_CREATE_JMETER_SQL,
						appdSchema));
			}

			// Update Schema name
			INSERT_EVENTS_SQL = String.format(INSERT_EVENTS_SQL, appdSchema);
			INSERT_TRANSACTIONS_SQL = String.format(INSERT_TRANSACTIONS_SQL,
					appdSchema, "%s", "%s");

			connection.setCatalog(appdSchema);
			log.debug("Catalog: " + connection.getCatalog());

			// move this to destroy method.
			// connection.close();

		} catch (ClassNotFoundException e) {
			log.error("Class Not found: " + JDBC_DRIVER);
			e.printStackTrace();
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void writeMetricsToDB(){
		
	}

	public void destroy() {
		// closing the MYSQL Connection
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
}
