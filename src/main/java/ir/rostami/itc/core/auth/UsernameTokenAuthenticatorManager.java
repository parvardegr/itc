package ir.rostami.itc.core.auth;

import ir.rostami.itc.model.user.User;
import ir.rostami.itc.model.user.logic.UserMgr;

public class UsernameTokenAuthenticatorManager {
	
	//TODO must throw authenticationException
	public static User authenticate(String username, String token) {
		User user = UserMgr.getInstance().getByUsername(username);
		
		if(user == null) {
			return null;
		}
		
		if(user.getToken().equals(token))
			user.setAuthenticated(true);
		else 
			user.setAuthenticated(false);
		
		return user;
	}
}
