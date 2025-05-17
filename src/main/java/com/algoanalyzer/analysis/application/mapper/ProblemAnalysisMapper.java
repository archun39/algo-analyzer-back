package com.algoanalyzer.analysis.application.mapper;

import org.mapstruct.Mapper;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;

@Mapper(componentModel = "spring")
public interface ProblemAnalysisMapper {
    ProblemAnalysisResponseDto toResponseDto(ProblemAnalysis analysis);
    ProblemAnalysis toDomain(ProblemAnalysisResponseDto responseDto);
} 