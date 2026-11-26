package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.TeamMemberDTO;
import ai.bzanalytics.ratls.model.TeamMember;
import ai.bzanalytics.ratls.repo.TeamMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

	private final TeamMemberRepository repository;

	private final S3Service s3Service;

	public List<TeamMemberDTO> getAllTeamMembers() {
		return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public TeamMemberDTO addTeamMember(TeamMemberDTO teamMemberDTO) {
		try {
			String imageUrl = s3Service.uploadImage(teamMemberDTO.getFile());
			TeamMember teamMember = convertToEntity(teamMemberDTO, imageUrl);
			TeamMember savedMember = repository.save(teamMember);
			return convertToDTO(savedMember);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public TeamMemberDTO getTeamMemberById(Long id) {
		TeamMember teamMember = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Team member not found with id: " + id));
		return convertToDTO(teamMember);
	}

	public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO updatedMemberDTO) {
		try {
			TeamMember existingMember = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Team member not found with id: " + id));
			existingMember.setName(updatedMemberDTO.getName());
			existingMember.setRole(updatedMemberDTO.getRole());
			existingMember.setBio(updatedMemberDTO.getBio());
			if (updatedMemberDTO.getFile() != null) {

				String imageUrl = s3Service.uploadImage(updatedMemberDTO.getFile());
				existingMember.setImageUrl(imageUrl);
			}

			TeamMember updatedMember = repository.save(existingMember);
			return convertToDTO(updatedMember);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

	public void deleteTeamMember(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Team member not found");
		}
	}

	// Convert Entity to DTO
	private TeamMemberDTO convertToDTO(TeamMember teamMember) {
		TeamMemberDTO dto = new TeamMemberDTO();
		dto.setId(teamMember.getId());
		dto.setName(teamMember.getName());
		dto.setRole(teamMember.getRole());
		dto.setBio(teamMember.getBio());
		dto.setImageUrl(teamMember.getImageUrl());
		return dto;
	}

	// Convert DTO to Entity
	private TeamMember convertToEntity(TeamMemberDTO dto, String imageUrl) {
		TeamMember teamMember = new TeamMember();
		teamMember.setName(dto.getName());
		teamMember.setRole(dto.getRole());
		teamMember.setBio(dto.getBio());
		teamMember.setImageUrl(imageUrl);
		return teamMember;
	}

}