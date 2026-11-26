package ai.bzanalytics.ratls.service;
import ai.bzanalytics.ratls.dto.LoginDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtUtil jwtUtil;

    private LoginDTO validLoginDTO;
    private LoginDTO invalidLoginDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize LoginDTO objects
        validLoginDTO = new LoginDTO();
        validLoginDTO.setUserName("ratlscontracting123");
        validLoginDTO.setPassword("Ratls@123");

        invalidLoginDTO = new LoginDTO();
        invalidLoginDTO.setUserName("wrongUser");
        invalidLoginDTO.setPassword("wrongPassword");
    }

    @Test
    void testLoginUserWithValidCredentials() {
        // Mock the static method generateJwtToken using mockStatic
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            String expectedToken = "mockedAccessToken";
            mockedJwtUtil.when(() -> JwtUtil.generateJwtToken(anyString(), anyMap())).thenReturn(expectedToken);
            Object response = authService.loginUser(validLoginDTO);
            assertNotNull(response);
            assertTrue(response instanceof Map);
            Map<String, Object> responseMap = (Map<String, Object>) response;
            assertEquals("Login successfully.", responseMap.get("message"));
            assertEquals(expectedToken, responseMap.get("accessToken"));
        }
    }
    @Test
    void testLoginUserWithInvalidCredentials() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            authService.loginUser(invalidLoginDTO);
        });

        assertEquals("Provide valid Credentials: ", exception.getMessage());
    }
}
