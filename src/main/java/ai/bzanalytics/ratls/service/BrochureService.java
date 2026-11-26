package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.BrochureDTO;

public interface BrochureService {

	public BrochureDTO getBrochureUrl();

	public BrochureDTO saveBrochureUrl(BrochureDTO brochureDTO);

	public BrochureDTO updateBrochureUrl(BrochureDTO brochureDTO);

}
