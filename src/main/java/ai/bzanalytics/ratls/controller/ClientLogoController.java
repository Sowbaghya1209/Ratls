package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.ClientLogoDTO;
import ai.bzanalytics.ratls.service.ClientLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/client-logo")
@RequiredArgsConstructor
@CrossOrigin
public class ClientLogoController {

	private final ClientLogoService clientLogoService;

	@GetMapping("/{id}")
	public ClientLogoDTO getClientLogoById(@PathVariable Long id) {
		try {
			return clientLogoService.getClientLogoById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Client logo not found with ID: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving client logo: " + e.getMessage());
		}
	}

	@GetMapping
	public List<ClientLogoDTO> getAllClientLogos() {
		try {
			return clientLogoService.getAllClientLogos();
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving all client logos: " + e.getMessage());
		}
	}

	@PostMapping(consumes = "multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
	public ClientLogoDTO createClientLogo(@ModelAttribute ClientLogoDTO clientLogoDTO) {
		try {
			return clientLogoService.createClientLogo(clientLogoDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while creating client logo: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public String deleteClientLogo(@PathVariable Long id) {
		try {
			clientLogoService.deleteClientLogo(id);
			return "Client logo deleted successfully";
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while deleting client logo: " + e.getMessage());
		}
	}

}
