package dataBaseService;

import helpers.TimeHelper;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

import messageSystem.MessageSystem;
import modules.Abonent;
import modules.Address;
import dataBaseService.users.UserDataSet;
import dataBaseService.users.UsersDAO;

public class DataBaseService implements Abonent, Runnable, UsersDAO{
	private Address address;
	private Connection connection;
    private UserDataSet user = new UserDataSet(-1, "", "", "");
    Executor executor= new Executor();

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
        //Executor executor= new Executor();
		executor.execUpdate(connection, query.toString());
	}

	
	public UserDataSet getUDSByLP(String login, String password) throws SQLException {

        StringBuilder query = new StringBuilder();
		query
		.append("SELECT * FROM users WHERE login = ")
		.append("'"+ login + "'")
		.append(" AND password = ")
		.append("'" + password + "'")
		.append(";");
		//Executor executor= new Executor();
		executor.execQuery(this.connection, query.toString(), new ResultHandler <UserDataSet>(){
				
				public UserDataSet handle(ResultSet result) throws SQLException {
					if (result.next()) {
						int id = result.getInt("id");
						String login = result.getString("login");
						String password = result.getString("password");
						String nick = result.getString("nick");
						user.setId(id);
                        user.setLogin( login);
                        user.setPassword(password);
                        user.setNick(nick);
                        return user;
					}
					else {
                        user.setId(-3);
                        user.setLogin("");
                        user.setPassword("");
                        user.setNick("");
                        return user;
                    }
					
				}
			});
		return user;
	}

    public void setConnection(Connection connection){
        this.connection = connection;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
