
package ai.bzanalytics.ratls.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class S3Service {

	private final S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Value("${aws.s3.domain}")
	private String domain;

	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadImage(MultipartFile file) throws IOException {

		String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

		byte[] bytes = file.getBytes();
		Path path = Paths.get(fileName);
		Files.write(path, bytes);

		try {
			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).acl("public-read").build(),
					RequestBody.fromFile(path));

			return "https://" + domain + fileName;
		}
		catch (S3Exception e) {
			e.printStackTrace();
			throw new IOException("Error occurred while uploading file to S3", e);
		}
	}

	public String savePdf(MultipartFile file) throws IOException {

		String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

		byte[] bytes = file.getBytes();
		Path path = Paths.get(fileName);
		Files.write(path, bytes);

		try {
			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).acl("public-read").build(),
					RequestBody.fromFile(path));

			return "https://" + domain + fileName;
		}
		catch (S3Exception e) {
			e.printStackTrace();
			throw new IOException("Error occurred while uploading file to S3", e);
		}
	}

	public String uploadVideo(MultipartFile file) throws IOException {

		String contentType = file.getContentType();
		if (!contentType.startsWith("video/")) {
			throw new IllegalArgumentException("File is not a valid video");
		}
		String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

		byte[] bytes = file.getBytes();
		Path path = Paths.get(fileName);
		Files.write(path, bytes);

		try {

			s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(fileName).acl("public-read").build(),
					RequestBody.fromFile(path));

			return "https://" + domain + fileName;
		}
		catch (S3Exception e) {
			e.printStackTrace();
			throw new IOException("Error occurred while uploading file to S3", e);
		}
	}

}
