package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.TeamMemberDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
@CrossOrigin
public class TeamMemberController {

	private final TeamMemberService teamMemberService;

	@GetMapping
	public List<TeamMemberDTO> getAllTeamMembers() {
		try {
			List<TeamMemberDTO> teamMembers = teamMemberService.getAllTeamMembers();
			if (teamMembers.isEmpty()) {
				throw new NotFoundException("No team members found");
			}
			return teamMembers;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving team members: " + e.getMessage());
		}
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TeamMemberDTO addTeamMember(@ModelAttribute TeamMemberDTO teamMemberDTO) {
		try {
			return teamMemberService.addTeamMember(teamMemberDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while adding team member: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public TeamMemberDTO updateTeamMember(@PathVariable Long id, @ModelAttribute TeamMemberDTO updatedMemberDTO) {
		try {
			if (updatedMemberDTO == null) {
				throw new NotFoundException("No inputs passed for updating the team member");
			}
			return teamMemberService.updateTeamMember(id, updatedMemberDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while updating team member with Id: " + id + " - " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteTeamMember(@PathVariable Long id) {
		try {
			teamMemberService.deleteTeamMember(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while deleting team member with Id: " + id + " - " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public TeamMemberDTO getMemberById(@PathVariable Long id) {
		try {
			return teamMemberService.getTeamMemberById(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Team member not found with Id: " + id + " - " + e.getMessage());
		}
	}

}
