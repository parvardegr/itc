package ir.rostami.itc.core.file_system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSystemManager {

	public static void createDirectory(String directoryUrl) throws IOException {
		Files.createDirectories(Paths.get(directoryUrl));
	}

}
