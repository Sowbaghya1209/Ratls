package ai.bzanalytics.ratls.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class TempFileUtils {

	public File saveToTemp(MultipartFile file) throws IOException {
		File tempFile = File.createTempFile("temp", file.getOriginalFilename());
		file.transferTo(tempFile);
		return tempFile;
	}

	public void removeTempFile(File tempFile) {
		if (tempFile.exists()) {
			tempFile.delete();
		}
	}

}