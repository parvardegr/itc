package ir.rostami.itc.model.user;

import java.util.Map;

public class User {
	private String username;
	private String token;
	private String ownedDir;
	private Map<String, String> allowedDirs;
	private boolean isAuthenticated;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOwnedDir() {
		return ownedDir;
	}

	public void setOwnedDir(String ownedDir) {
		this.ownedDir = ownedDir;
	}

	public Map<String, String> getAllowedDirs() {
		return allowedDirs;
	}

	public void setAllowedDirs(Map<String, String> allowedDirs) {
		this.allowedDirs = allowedDirs;
	}
	
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}
