package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.TeamMemberDTO;

import java.util.List;

public interface TeamMemberService {

	public List<TeamMemberDTO> getAllTeamMembers();

	public TeamMemberDTO addTeamMember(TeamMemberDTO teamMemberDTO);

	public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO updateTeamMemberDTO);

	public void deleteTeamMember(Long id);

	public TeamMemberDTO getTeamMemberById(Long id);

}