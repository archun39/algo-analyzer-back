package com.algoanalyzer.domain.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algoanalyzer.domain.analysis.problem.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.domain.analysis.problem.dto.response.ProblemAnalysisResponseDto;
import com.algoanalyzer.domain.analysis.problem.service.ProblemAnalysisService;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.dto.response.ProblemWithAnalysisResponseDto;
import com.algoanalyzer.domain.problem.service.ProblemService;
import com.algoanalyzer.domain.problem.document.ProblemDocument;
import com.algoanalyzer.domain.problem.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/problems")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {
    
    private final ProblemService problemService;
    private final ProblemAnalysisService problemAnalysisService;
    private final ProblemRepository problemRepository;

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemWithAnalysisResponseDto> getProblem(@PathVariable Long problemId) {
        // DB에서 문제 조회
        ProblemDocument problemDocument = (ProblemDocument) problemRepository.findByProblemId(problemId);
        ProblemResponseDto response;

        if (problemDocument != null) {
            // DB에 문제 정보가 있는 경우
            response = convertToResponseDto(problemDocument);
        } else {
            // DB에 문제 정보가 없는 경우, 문제를 크롤링
            response = problemService.getProblem(problemId);
        }

        // 문제 분석 요청
        ProblemAnalysisRequestDto analysisRequest = buildAnalysisRequest(response);
        ProblemAnalysisResponseDto analysisResult = problemAnalysisService.analyzeProblem(analysisRequest);

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