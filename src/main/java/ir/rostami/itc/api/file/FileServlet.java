package ir.rostami.itc.api.file;

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

import ir.rostami.itc.core.file_system.FileSystemManager;
import ir.rostami.itc.model.RequestMethods;
import ir.rostami.itc.model.file.NativeFile;

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
			return;
		}
		
		response.setStatus(202);
	}

	private void doDelete_(HttpServletRequest request, HttpServletResponse response) {
		String directoryUrl = resolveUrl(request);
		
		try {
			FileSystemManager.deleteFile(directoryUrl);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(500);
			return;
		}
		
		response.setStatus(202);
	}

	private void doList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String directoryUrl = resolveUrl(request);
		
		List<NativeFile> list = FileSystemManager.list(directoryUrl);
		
		response.setCharacterEncoding("UTF-8");
		response.setStatus(200);
		response.getWriter().print(new ObjectMapper().writeValueAsString(list));
	}

	private void doCopy(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
	}

	private void doPut_(HttpServletRequest request, HttpServletResponse response) {
		String url = resolveUrl(request);
		
		ServletInputStream dataInputStream;
		try {
			dataInputStream = request.getInputStream();
			byte[] data = IOUtils.toByteArray(dataInputStream);
			dataInputStream.close();
			//if(hasUpdateAccess) {
			//	Files.addOrUpdate(Paths.get(url), data);
			//} else {
			FileSystemManager.add(data, url);
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

	private void doGet_(HttpServletRequest request, HttpServletResponse response) {
		String url = resolveUrl(request);
		
		try {
			byte[] data = FileSystemManager.get(url);
			
			//response.setContentType(type);
			String fileName = FilenameUtils.getName(url);
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength(data.length);
			
			String contentType = FileSystemManager.getContentType(url);
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
