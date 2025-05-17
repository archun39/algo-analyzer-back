package com.algoanalyzer.problem.application.mapper;

import org.mapstruct.Mapper;

import com.algoanalyzer.problem.domain.model.Problem;
import com.algoanalyzer.problem.presentation.dto.response.ProblemResponseDto;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;

@Mapper(componentModel = "spring")
public interface ProblemMapper {
    ProblemResponseDto toResponseDto(Problem problem);
    ProblemAnalysisRequestDto toRequestDto(Problem problem);
}
