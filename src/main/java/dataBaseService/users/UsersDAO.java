package dataBaseService.users;

import java.sql.SQLException;

public interface UsersDAO {
	public void addUDS(UserDataSet user);
	public UserDataSet getUDSByLP(String login, String password) throws SQLException;
}
