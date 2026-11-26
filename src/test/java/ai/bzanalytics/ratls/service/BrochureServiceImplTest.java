package ai.bzanalytics.ratls.service;
import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;
import ai.bzanalytics.ratls.dto.BrochureDTO;
import ai.bzanalytics.ratls.model.Brochure;
import ai.bzanalytics.ratls.repo.BrochureRepository;
import ai.bzanalytics.ratls.util.TempFileUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BrochureServiceImplTest {

    @InjectMocks
    private BrochureServiceImpl brochureService;

    @Mock
    private BrochureRepository brochureRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private TempFileUtils tempFileUtils;

    @Mock
    private MultipartFile mockFile;

    private BrochureDTO brochureDTO;

    @BeforeEach
    void setUp() {
        brochureDTO = new BrochureDTO();
    }

    @Test
    void testGetBrochureUrl_Success() {
        Brochure brochure = new Brochure();
        brochure.setUrl("http://example.com/brochure.pdf");
        when(brochureRepository.findById(1L)).thenReturn(Optional.of(brochure));
        BrochureDTO result = brochureService.getBrochureUrl();
        assertNotNull(result);
        assertEquals("http://example.com/brochure.pdf", result.getUrl());
    }

    @Test
    void testGetBrochureUrl_NotFound() {
        when(brochureRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> brochureService.getBrochureUrl());
    }
    @Test
    void testSaveBrochureUrl_Success() throws Exception {
        when(mockFile.getContentType()).thenReturn("application/pdf");
        when(mockFile.isEmpty()).thenReturn(false);
        when(s3Service.savePdf(mockFile)).thenReturn("mockedPdfUrl");
        BrochureDTO brochureDTO = new BrochureDTO();
        brochureDTO.setFile(mockFile);
        Brochure mockBrochure = new Brochure();
        mockBrochure.setUrl("mockedPdfUrl");
        when(brochureRepository.save(any(Brochure.class))).thenReturn(mockBrochure);

        // Call the service method
        BrochureDTO result = brochureService.saveBrochureUrl(brochureDTO);

        // Verify the result
        assertNotNull(result);
        assertEquals("mockedPdfUrl", result.getUrl());

        // Verify interactions
        verify(brochureRepository).save(any(Brochure.class));
    }

    @Test
    void testSaveBrochureUrl_InvalidFile() throws IOException {
        when(mockFile.getContentType()).thenReturn("application/msword");
        brochureDTO.setFile(mockFile);
        assertThrows(EntityNotFoundException.class, () -> brochureService.saveBrochureUrl(brochureDTO));
    }

    @Test
    void testSaveBrochureUrl_NoFile() {
        brochureDTO.setFile(null);
        assertThrows(EntityNotFoundException.class, () -> brochureService.saveBrochureUrl(brochureDTO));
    }

    @Test
    void testUpdateBrochureUrl_Success_WithFile() throws IOException {
        Brochure existingBrochure = new Brochure();
        existingBrochure.setUrl("http://example.com/old_brochure.pdf");
        when(brochureRepository.findById(1L)).thenReturn(Optional.of(existingBrochure));
        when(mockFile.getContentType()).thenReturn("application/pdf");
        when(s3Service.savePdf(mockFile)).thenReturn("http://example.com/new_brochure.pdf");
        brochureDTO.setFile(mockFile);
        BrochureDTO result = brochureService.updateBrochureUrl(brochureDTO);
        assertNotNull(result);
        assertEquals("http://example.com/new_brochure.pdf", result.getUrl());
    }

    @Test
    void testUpdateBrochureUrl_Success_WithUrl() {
        Brochure existingBrochure = new Brochure();
        existingBrochure.setUrl("http://example.com/old_brochure.pdf");
        when(brochureRepository.findById(1L)).thenReturn(Optional.of(existingBrochure));
        brochureDTO.setUrl("http://example.com/new_brochure_url.pdf");
        BrochureDTO result = brochureService.updateBrochureUrl(brochureDTO);
        assertNotNull(result);
        assertEquals("http://example.com/new_brochure_url.pdf", result.getUrl());
    }

    @Test
    void testUpdateBrochureUrl_NotFound() {
        when(brochureRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> brochureService.updateBrochureUrl(brochureDTO));
    }

    @Test
    void testUpdateBrochureUrl_InvalidFileType() throws IOException {
        Brochure existingBrochure = new Brochure();
        existingBrochure.setUrl("http://example.com/old_brochure.pdf");
        when(brochureRepository.findById(1L)).thenReturn(Optional.of(existingBrochure));
        when(mockFile.getContentType()).thenReturn("application/msword");
        brochureDTO.setFile(mockFile);
        assertThrows(NotFoundException.class, () -> brochureService.updateBrochureUrl(brochureDTO));
    }

    @Test
    void testUpdateBrochureUrl_NoFileOrUrl() {
        Brochure existingBrochure = new Brochure();
        existingBrochure.setUrl("http://example.com/old_brochure.pdf");
        when(brochureRepository.findById(1L)).thenReturn(Optional.of(existingBrochure));
        brochureDTO.setFile(null);
        brochureDTO.setUrl(null);
        assertThrows(NotFoundException.class, () -> brochureService.updateBrochureUrl(brochureDTO));
    }
}
