package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.BrochureDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.model.Brochure;
import ai.bzanalytics.ratls.repo.BrochureRepository;
import ai.bzanalytics.ratls.util.TempFileUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrochureServiceImpl implements BrochureService {

	private final BrochureRepository brochureRepository;

	private final S3Service s3Service;

	private final TempFileUtils tempFileUtils;

	public BrochureDTO getBrochureUrl() {
		Optional<Brochure> brochureOpt = brochureRepository.findById(1L);
		if (brochureOpt.isEmpty()) {
			throw new EntityNotFoundException("Brochure not found");
		}

		Brochure brochure = brochureOpt.get();
		return new BrochureDTO(brochure.getUrl());
	}

	public BrochureDTO saveBrochureUrl(BrochureDTO brochureDTO) {
		try {
			if (brochureDTO.getFile() == null || brochureDTO.getFile().isEmpty()) {
				throw new EntityNotFoundException(" No file uploaded.");
			}

			MultipartFile file = brochureDTO.getFile();
			if (!isValidPdf(file)) {
				throw new EntityNotFoundException(" Invalid file type. Please upload a PDF.");
			}
			String pdfUrl = s3Service.savePdf(file);
			Brochure brochure = new Brochure();
			brochure.setUrl(pdfUrl);
			brochureRepository.save(brochure);
			return new BrochureDTO(brochure.getUrl());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BrochureDTO updateBrochureUrl(BrochureDTO brochureDTO) {

		Brochure brochure = brochureRepository.findById(1L)
			.orElseThrow(() -> new NotFoundException(" Brochure not found"));

		if (brochureDTO.getFile() != null && !brochureDTO.getFile().isEmpty()) {

			if (!isValidPdf(brochureDTO.getFile())) {
				throw new NotFoundException(" Invalid file type. Please upload a PDF.");
			}

			try {
				String newUrl = s3Service.savePdf(brochureDTO.getFile());
				brochure.setUrl(newUrl);
			}
			catch (IOException e) {
				throw new RuntimeException("Error uploading new PDF file: " + e.getMessage());
			}
		}
		else if (brochureDTO.getUrl() != null && !brochureDTO.getUrl().isEmpty()) {

			brochure.setUrl(brochureDTO.getUrl());
		}
		else {

			throw new NotFoundException(" No file or URL provided for update.");
		}

		brochureRepository.save(brochure);

		return new BrochureDTO(brochure.getUrl());
	}

	private boolean isValidPdf(MultipartFile file) {
		String fileType = file.getContentType();
		return "application/pdf".equals(fileType);
	}

}
