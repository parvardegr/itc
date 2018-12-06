package ir.rostami.itc.api.file;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ir.rostami.itc.core.file_system.FileSystemManager;
import ir.rostami.itc.model.RequestMethods;

@WebServlet("/")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = -1807683086808197402L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String method = RequestMethods.resolveRequestMethod(request);
		if(method == null) {
			method = "NOT-SUPPORT";
		}
		
		switch (method) {
		case RequestMethods.GET:
			doGet_(request, response);
			return;
		case RequestMethods.PUT:
			doPut_(request, response);
			return;
		case RequestMethods.COPY:
			doCopy(request, response);
			return;
		case RequestMethods.LIST:
			doList(request, response);
			return;
		case RequestMethods.DELETE:
			doDelete_(request, response);
			return;
		case RequestMethods.MKDIR:
			doMkdir(request, response);
			return;
		default:
			response.setStatus(404);
			return;
		}
	}
	
	private String resolveUrl(HttpServletRequest request) {
		return request.getHeader("Url");
	}
	
	private void doMkdir(HttpServletRequest request, HttpServletResponse response) {
		String directoryUrl = resolveUrl(request);
		
		try {
			FileSystemManager.createDirectory(directoryUrl);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
		}
		
		response.setStatus(202);
	}

	private void doDelete_(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	private void doList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	private void doCopy(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	private void doPut_(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	private void doGet_(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}
}
