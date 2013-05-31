package dataBaseService;

import helpers.TimeHelper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import messageSystem.MessageSystem;
import modules.Abonent;
import modules.Address;
import dataBaseService.users.UserDataSet;
import dataBaseService.users.UsersDAO;

public class DataBaseService implements Abonent, Runnable, UsersDAO{
	private Address address;
	private Connection connection;
	public DataBaseService() {
		this.address = new Address("DataBaseService");
		MessageSystem.addService(this);
	}
	
	public void run() {
		try {
			Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance(); //имя драйвера в ресурсы
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuilder url = new StringBuilder();
		url.append("jdbc:sqlite:db/game.db"); //путь к базе в ресурсы
		try {
			this.connection = DriverManager.getConnection(url.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		while (true) { 			
			MessageSystem.execForAbonent(this);
			TimeHelper.sleep(50);
		}
	}

	
	public Address getAddress() {
		return address;
	}

	public void addUDS(UserDataSet user) {
		StringBuilder query = new StringBuilder();
		query
		.append("INSERT INTO users(login, password, nick)")
		.append("VALUES (")
		.append(user.getLogin())
		.append(", ")
		.append(user.getPassword())
		.append(", ")
		.append(user.getNick())
		.append(");");
		Executor.execUpdate(connection, query.toString());	
	}

	
	public UserDataSet getUDSByLP(String login, String password) throws SQLException {
		StringBuilder query = new StringBuilder();
		query
		.append("SELECT * FROM users WHERE login = ")
		.append("'"+ login + "'")
		.append(" AND password = ")
		.append("'" + password + "'")
		.append(";");
				
		UserDataSet user = Executor.execQuery(connection, query.toString(), new ResultHandler <UserDataSet>(){
				
				public UserDataSet handle(ResultSet result) throws SQLException {
					if (result.next()) {
						int id = result.getInt("id");
						String login = result.getString("login");
						String password = result.getString("password");
						String nick = result.getString("nick");
						return new UserDataSet(id, login, password, nick);
					}
					else {
						return new UserDataSet(-3, "", "", "");
					}
					
				}
			});
		return user;
	}

}
