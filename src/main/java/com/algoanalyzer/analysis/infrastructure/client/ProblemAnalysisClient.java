package com.algoanalyzer.analysis.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import com.algoanalyzer.analysis.domain.model.ProblemAnalysis;
import com.algoanalyzer.analysis.presentation.dto.request.ProblemAnalysisRequestDto;
import com.algoanalyzer.analysis.presentation.dto.response.ProblemAnalysisResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.algoanalyzer.analysis.application.port.out.AnalyzeProblemClient;
import com.algoanalyzer.analysis.domain.exception.ProblemAnalysisException;


@Component
@RequiredArgsConstructor
public class ProblemAnalysisClient implements AnalyzeProblemClient {
    //todo: 문제 분석 클라이언트 구현
    private final RestTemplate restTemplate;

    @Value("${python.api.base-url}")
    private String baseUrl;

    @Override
    public ProblemAnalysis callPythonApi(ProblemAnalysisRequestDto request) {
        String url = baseUrl + "/api/internal/problems/" + request.getProblemId() + "/analysis";

        // HTTP 요청용 헤더 및 엔티티 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProblemAnalysisRequestDto> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<ProblemAnalysisResponseDto> resp = restTemplate
                .exchange(url, HttpMethod.POST, entity, ProblemAnalysisResponseDto.class);
    
            ProblemAnalysisResponseDto responseDto = resp.getBody();
            if (!resp.getStatusCode().is2xxSuccessful() || responseDto == null) {
                throw new ProblemAnalysisException("문제 분석 실패: HTTP " + resp.getStatusCodeValue());
            }
    
            ProblemAnalysis analysis = buildResponseDto(responseDto);
   
            return analysis;
    
        } catch (Exception e) {
            // 예외 스택트레이스 로깅
            System.err.println("[AI Client] 호출 중 예외 발생:");
            e.printStackTrace();
            throw e;
        }
    }

    private ProblemAnalysis buildResponseDto(ProblemAnalysisResponseDto response) {
        return ProblemAnalysis.builder()
            .problemId(response.getProblemId())
            .timeComplexity(response.getTimeComplexity())
            .timeComplexityReasoning(response.getTimeComplexityReasoning())
            .spaceComplexity(response.getSpaceComplexity())
            .spaceComplexityReasoning(response.getSpaceComplexityReasoning())
            .algorithmType(response.getAlgorithmType())
            .algorithmTypeReasoning(response.getAlgorithmTypeReasoning())
            .dataStructures(response.getDataStructures())
            .dataStructuresReasoning(response.getDataStructuresReasoning())
            .solutionImplementation(response.getSolutionImplementation())
            .solutionImplementationReasoning(response.getSolutionImplementationReasoning())
            .build();
    }
}
