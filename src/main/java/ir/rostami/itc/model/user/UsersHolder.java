package ir.rostami.itc.model.user;

import java.util.HashMap;
import java.util.Map;

public class UsersHolder {
	private static UsersHolder instance;
	private Map<String, User> users;
	
	public static UsersHolder getInstance() {
		if(instance == null) {
			instance = new UsersHolder();
		}
		return instance;
	}
	
	public UsersHolder() {
		initPreRegisterUsers();
	}

	private void initPreRegisterUsers() {
		users = new HashMap<>();
		User user1 = new User();
		user1.setToken("demo");
		user1.setUsername("demo");
		user1.setOwnedDir("demo");
		user1.setAllowedDirs(new HashMap<>());
		
		users.put(user1.getUsername(), user1);
		
		User user2 = new User();
		user2.setToken("D9471198273E14B1");
		user2.setUsername("ICTCHALLENGE");
		user2.setOwnedDir("ICTCHALLENGE");
		user2.setAllowedDirs(new HashMap<>());
		
		users.put(user2.getUsername(), user2);
	}
	
	public User getByUsername(String username) {
		return users.get(username);
	}
	
}
