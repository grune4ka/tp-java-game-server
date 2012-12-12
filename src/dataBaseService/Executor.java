package dataBaseService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor {
	public static <T> T execQuery(Connection connection, String query, 
			ResultHandler<T> handler) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.execute(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultSet result = stmt.getResultSet();
		T value = handler.handle(result);
		result.close();
		stmt.close();
		return value;
	}
	
	public static int execUpdate(Connection connection, String query) {
		int updated = 0;
		try {
			Statement stmt = connection.createStatement();
			stmt.execute(query);
			updated = stmt.getUpdateCount();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return updated;
	}
}
