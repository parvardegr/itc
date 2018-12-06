package ir.rostami.itc.model.user.logic;

import ir.rostami.itc.model.user.User;
import ir.rostami.itc.model.user.dao.InMemoryUserDAO;

public class UserMgr {
	private static UserMgr instance;
	
	public static UserMgr getInstance() {
		if(instance == null) {
			instance = new UserMgr();
		}
		return instance;
	}
	
	public User getByUsername(String username) {
		return InMemoryUserDAO.getInstance().getByUsername(username);
	}
}
