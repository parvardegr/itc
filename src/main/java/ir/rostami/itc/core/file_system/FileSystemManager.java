package ir.rostami.itc.core.file_system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ir.rostami.itc.model.file.NativeFile;

public class FileSystemManager {

	public static void createDirectory(String directoryUrl) throws IOException {
		Files.createDirectories(Paths.get(directoryUrl));
	}

	public static void deleteFile(String url) throws IOException {
		Files.deleteIfExists(Paths.get(url));
	}

	public static void add(byte[] data, String url) throws IOException {
		Files.write(Paths.get(url), data, StandardOpenOption.CREATE_NEW);
	}
	
	public static void addOrUpdate(byte[] data, String url) throws IOException {
		Files.write(Paths.get(url), data);
	}

	public static byte[] get(String url) throws IOException {
		return getFileData(Paths.get(url));
	}

	private static byte[] getFileData(Path path) throws IOException {
		return Files.readAllBytes(path);
	}

	public static String getContentType(String url) throws IOException {
		return Files.probeContentType(Paths.get(url));
	}

	public static List<NativeFile> list(String directoryUrl) throws IOException {
		Path path = Paths.get(directoryUrl);
		List<NativeFile> files = new ArrayList<>();
		
		if(Files.isDirectory(path)) {
			return Files.walk(path).filter(filePath -> Files.isRegularFile(filePath)).map(filePath -> {
				try {
					return createNew("", filePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}).filter(nativeFile -> nativeFile != null).collect(Collectors.toList());
			
		} else {
			files.add(createNew("", Paths.get(directoryUrl)));
		}
		return files;
	}
	
	public static NativeFile createNew(String base, Path filePath) throws IOException {
		NativeFile nativeFile = new NativeFile();
		
		nativeFile.setUrl(filePath.toString().replace(base, ""));
		nativeFile.setSha1(getFileSha1(filePath));
		nativeFile.setContentLength(Files.size(filePath)+"");
		nativeFile.setContentType(Files.probeContentType(filePath));
		
		return nativeFile;
	}

	
	private static MessageDigest messageDigest;
	public FileSystemManager() {
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getFileSha1(Path filePath) {
		// TODO Auto-generated method stub
		return "not implement yet";
	}
	
	

}
