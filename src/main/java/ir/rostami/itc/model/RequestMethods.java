package ir.rostami.itc.model;

import javax.servlet.http.HttpServletRequest;

public class RequestMethods {
	public static final String GET = "GET";
	public static final String PUT = "PUT"; 
	public static final String DELETE = "DELETE"; 
	public static final String COPY = "COPY"; 
	public static final String LIST = "LIST";
	public static final String MKDIR = "MKDIR";
	
	public static String resolveRequestMethod(HttpServletRequest request) {
		return request.getHeader("Method");
	}
}
