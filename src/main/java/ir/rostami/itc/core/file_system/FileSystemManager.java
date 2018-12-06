package ir.rostami.itc.core.file_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;

import ir.rostami.itc.model.file.NativeFile;
import ir.rostami.itc.model.user.User;

public class FileSystemManager {
	private static String BASE_FILES_DIR = "/opt/itc/files/";

	private static Path resolveFullPath(User user, String url) {
		return Paths.get(BASE_FILES_DIR + user.getOwnedDir() + url);
	}

	public static void mkdir(User user, String url) throws IOException {
		Files.createDirectories(resolveFullPath(user, url));
	}

	public static void delete(User user, String url) throws IOException {
		Files.deleteIfExists(resolveFullPath(user, url));
	}

	public static void add(User user, String url, byte[] data) throws IOException {
		File file = new File(resolveFullPath(user, url).toString());
		file.getParentFile().mkdirs();
		Files.write(resolveFullPath(user, url), data, StandardOpenOption.CREATE_NEW);
	}

	public static void addOrUpdate(User user, String url, byte[] data) throws IOException {
		Files.write(resolveFullPath(user, url), data);
	}

	public static byte[] get(User user, String url) throws IOException {
		return getFileData(resolveFullPath(user, url));
	}

	private static byte[] getFileData(Path path) throws IOException {
		return Files.readAllBytes(path);
	}

	public static String getContentType(User user, String url) throws IOException {
		return Files.probeContentType(resolveFullPath(user, url));
	}

	public static List<NativeFile> list(User user, String url) throws IOException {
		Path path = resolveFullPath(user, url);
		String toRemoveFromPath = BASE_FILES_DIR + user.getOwnedDir();
		List<NativeFile> files = new ArrayList<>();

		if (Files.isDirectory(path)) {
			return Files.walk(path).sorted(Comparator.comparing(spath -> spath.getFileName().toString()))
					.map(filePath -> {
						try {
							return createNew(toRemoveFromPath, filePath);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return null;
					}).filter(nativeFile -> nativeFile != null).collect(Collectors.toList());

		} else {
			files.add(createNew(toRemoveFromPath, path));
		}
		return files;
	}

	public static NativeFile createNew(String base, Path filePath) throws IOException {
		NativeFile nativeFile = new NativeFile();

		if (Files.isDirectory(filePath)) {
			nativeFile.setUrl(filePath.toString().replace(base, ""));
			nativeFile.setSha1("");
			nativeFile.setContentLength("");
			nativeFile.setContentType("");
		} else {
			nativeFile.setUrl(filePath.toString().replace(base, ""));
			nativeFile.setSha1(getFileSha1(filePath));
			nativeFile.setContentLength(Files.size(filePath) + "");
			nativeFile.setContentType(getContentType(filePath));
		}
		return nativeFile;
	}

	private static String getContentType(Path filePath) {
		return URLConnection.guessContentTypeFromName(filePath.toString());
	}

	private static String getFileSha1(Path filePath) {
		try {
			return DigestUtils.sha1Hex(getFileData(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void copy(User user, String urlSource, String urlDest) throws IOException {
		Path srcPath = resolveFullPath(user, urlSource);
		Path destPath = resolveFullPath(user, urlDest);
		
		if(Files.exists(srcPath)) {
			Files.copy(srcPath, destPath);
		} else {
			throw new FileNotFoundException("src file not exists.");
		}
	}

}
