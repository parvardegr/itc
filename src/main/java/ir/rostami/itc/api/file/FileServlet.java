package ir.rostami.itc.api.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import ir.rostami.itc.core.auth.UsernameTokenAuthenticatorManager;
import ir.rostami.itc.core.file_system.FileSystemManager;
import ir.rostami.itc.model.RequestMethods;
import ir.rostami.itc.model.file.NativeFile;
import ir.rostami.itc.model.user.User;

@WebServlet("/")
public class FileServlet extends HttpServlet {
	private static final long serialVersionUID = -1807683086808197402L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = authenticate(request);
		if(user == null || !user.isAuthenticated()) {
			response.setStatus(401);
			return;
		}
		
		String method = RequestMethods.resolveRequestMethod(request);
		if(method == null) {
			method = "NOT-SUPPORT";
		}
		
		switch (method) {
		case RequestMethods.GET:
			doGet_(user, request, response);
			return;
		case RequestMethods.PUT:
			doPut_(user, request, response);
			return;
		case RequestMethods.COPY:
			doCopy(user, request, response);
			return;
		case RequestMethods.LIST:
			doList(user, request, response);
			return;
		case RequestMethods.DELETE:
			doDelete_(user, request, response);
			return;
		case RequestMethods.MKDIR:
			doMkdir(user, request, response);
			return;
		default:
			response.setStatus(404);
			return;
		}
	}
	
	private User authenticate(HttpServletRequest request) {
		String token = request.getHeader("Token");
		String username = request.getHeader("Username");
		if(token == null || username == null) {
			return null;
		}
		return UsernameTokenAuthenticatorManager.authenticate(username, token);
	}

	private String resolveUrl(HttpServletRequest request) {
		return request.getHeader("Url");
	}
	
	private void doMkdir(User user, HttpServletRequest request, HttpServletResponse response) {
		String directoryUrl = resolveUrl(request);
		
		try {
			FileSystemManager.mkdir(user, directoryUrl);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		response.setStatus(202);
	}

	private void doDelete_(User user, HttpServletRequest request, HttpServletResponse response) {
		String directoryUrl = resolveUrl(request);
		
		try {
			FileSystemManager.delete(user, directoryUrl);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		response.setStatus(202);
	}

	private void doList(User user, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String directoryUrl = resolveUrl(request);
		
		List<NativeFile> list = FileSystemManager.list(user, directoryUrl);
		
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		response.getWriter().print(new ObjectMapper().writeValueAsString(list));
	}

	private void doCopy(User user, HttpServletRequest request, HttpServletResponse response) {
		String urlSource = request.getHeader("source-url");
		String urlDest = request.getHeader("dest-url");
		try {
			FileSystemManager.copy(user, urlSource, urlDest);
			
			response.setStatus(202);
		} catch(FileNotFoundException fnfe) {
			response.setStatus(404);
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
	}

	private void doPut_(User user, HttpServletRequest request, HttpServletResponse response) {
		String url = resolveUrl(request);
		
		ServletInputStream dataInputStream;
		try {
			dataInputStream = request.getInputStream();
			byte[] data = IOUtils.toByteArray(dataInputStream);
			dataInputStream.close();
			//if(hasUpdateAccess) {
			//	Files.addOrUpdate(Paths.get(url), data);
			//} else {
			FileSystemManager.add(user, url, data);
			//}
		} catch(FileAlreadyExistsException fae) {
			response.setStatus(401);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		} 
		
		response.setStatus(202);
	}

	private void doGet_(User user, HttpServletRequest request, HttpServletResponse response) {
		String url = resolveUrl(request);
		
		try {
			byte[] data = FileSystemManager.get(user, url);
			
			//response.setContentType(type);
			String fileName = FilenameUtils.getName(url);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength(data.length);
			
			String contentType = FileSystemManager.getContentType(user, url);
			response.setContentType(contentType);
			
			response.setStatus(200);
			response.getOutputStream().write(data);
			
		} catch(NoSuchFileException nsf) {
			response.setStatus(404);
			return;
		}catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
	}
}
