package com.algoanalyzer.domain.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemWithAnalysisResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;
import com.algoanalyzer.domain.problem.model.ProblemDocument;
import com.algoanalyzer.domain.problem.repository.ProblemRepository;
import com.algoanalyzer.domain.analysis.problem.model.AnalysisLevel1Document;
import com.algoanalyzer.domain.analysis.problem.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {
    
    private final ProblemService problemService;
    private final ProblemAnalysisService problemAnalysisService;
    private final ProblemRepository problemRepository;
    private final AnalysisRepository analysisRepository;
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemWithAnalysisResponseDto> getProblem(@PathVariable Long problemId) {
        // DB에서 문제 조회
        ProblemDocument problemDocument = (ProblemDocument) problemRepository.findByProblemId(problemId);
        ProblemResponseDto response;

        if (problemDocument != null) {
            // DB에 문제 정보가 있는 경우
            response = convertToResponseDto(problemDocument);
            log.info("DB에서 문제 정보 조회 완료: {}", problemId);
        } else {
            // DB에 문제 정보가 없는 경우, 문제를 크롤링
            response = problemService.getProblem(problemId);
            log.info("DB에서 문제 정보 조회 실패: {}", problemId);
        }

        // 문제 분석 요청

        AnalysisLevel1Document analysisLevel1Document = (AnalysisLevel1Document) analysisRepository.findByProblemId(problemId);
        ProblemAnalysisResponseDto analysisResult;
        if(analysisLevel1Document == null) {
            ProblemAnalysisRequestDto analysisRequest = buildAnalysisRequest(response);
            analysisResult = problemAnalysisService.analyzeProblem(analysisRequest);
        }
        else {
            analysisResult = convertToResponseDto(analysisLevel1Document);
        }

        ProblemWithAnalysisResponseDto combinedResponse = ProblemWithAnalysisResponseDto.builder()
                .problemResponse(response)
                .analysisResponse(analysisResult)
                .build();

        return ResponseEntity.ok(combinedResponse);
    }

    private ProblemResponseDto convertToResponseDto(ProblemDocument problemDocument) {
        return ProblemResponseDto.builder()
                .problemId(problemDocument.getProblemId())
                .title(problemDocument.getTitle())
                .description(problemDocument.getDescription())
                .input(problemDocument.getInput())
                .output(problemDocument.getOutput())
                .timeLimit(problemDocument.getTimeLimit())
                .memoryLimit(problemDocument.getMemoryLimit())
                .tags(problemDocument.getTags())
                .build();
    }

    private ProblemAnalysisResponseDto convertToResponseDto(AnalysisLevel1Document analysisLevel1Document) {
        return ProblemAnalysisResponseDto.builder()
                .problemId(analysisLevel1Document.getProblemId())
                .timeComplexity(analysisLevel1Document.getTimeComplexity())
                .timeComplexityReasoning(analysisLevel1Document.getTimeComplexityReasoning())
                .spaceComplexity(analysisLevel1Document.getSpaceComplexity())
                .spaceComplexityReasoning(analysisLevel1Document.getSpaceComplexityReasoning())
                .algorithmType(analysisLevel1Document.getAlgorithmType())
                .algorithmTypeReasoning(analysisLevel1Document.getAlgorithmTypeReasoning())
                .dataStructures(analysisLevel1Document.getDataStructures())
                .dataStructuresReasoning(analysisLevel1Document.getDataStructuresReasoning())
                .solutionImplementation(analysisLevel1Document.getSolutionImplementation())
                .solutionImplementationReasoning(analysisLevel1Document.getSolutionImplementationReasoning())
                .build();
    }

    // 문제 분석 요청 빌드
    private ProblemAnalysisRequestDto buildAnalysisRequest(ProblemResponseDto response) {
        return ProblemAnalysisRequestDto.builder()
                .problemId(response.getProblemId())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags())
                .build();
    }
} 