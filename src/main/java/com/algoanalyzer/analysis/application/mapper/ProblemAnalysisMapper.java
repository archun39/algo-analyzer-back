package com.algoanalyzer.analysis.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Builder;
import org.mapstruct.ReportingPolicy;

import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProblemAnalysisMapper {
    ProblemAnalysisResponseDto toResponseDto(ProblemAnalysis analysis);
    ProblemAnalysis toDomain(ProblemAnalysisResponseDto responseDto);
} 