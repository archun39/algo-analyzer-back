package com.algoanalyzer.domain.problem.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.algoanalyzer.domain.problem.api.dto.SolvedAcProblemResponse;
import com.algoanalyzer.domain.problem.dto.response.ProblemResponseDto;
import com.algoanalyzer.domain.problem.exception.ProblemNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/problem/show?problemId=";
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";
    private final RestTemplate restTemplate;

    public ProblemResponseDto getProblem(Long problemId) {
        try {
            log.info("문제 정보 조회 시작: {}", problemId);
            
            // 1. solved.ac API에서 문제 제목과 태그 정보 가져오기
            String url = SOLVED_AC_API_URL + problemId;
            SolvedAcProblemResponse response = restTemplate.getForObject(url, SolvedAcProblemResponse.class);
            
            if (response == null) {
                throw new ProblemNotFoundException("문제를 찾을 수 없습니다: " + problemId);
            }
            
            // 2. 백준 웹사이트에서 문제 상세 정보 크롤링
            fetchProblemDetails(problemId, response);
            
            log.info("문제 정보 조회 완료: {}", problemId);
            return convertToResponseDto(response);
            
        } catch (Exception e) {
            log.error("문제 정보 조회 실패: {}", problemId, e);
            throw new ProblemNotFoundException("문제 정보를 가져오는데 실패했습니다: " + problemId);
        }
    }

    private void fetchProblemDetails(Long problemId, SolvedAcProblemResponse response) {
        try {
            String bojUrl = BOJ_PROBLEM_URL + problemId;
            Document doc = Jsoup.connect(bojUrl).get();
            
            // 문제 설명
            Elements descriptionElement = doc.select("#problem_description");
            if (!descriptionElement.isEmpty()) {
                response.setDescription(descriptionElement.text());
            }
            
            // 입력 설명
            Elements inputElement = doc.select("#problem_input");
            if (!inputElement.isEmpty()) {
                response.setInput(inputElement.text());
            }
            
            // 출력 설명
            Elements outputElement = doc.select("#problem_output");
            if (!outputElement.isEmpty()) {
                response.setOutput(outputElement.text());
            }
            
            Elements limitElement = doc.select("table#problem-info tbody tr");
            if (!limitElement.isEmpty()) {
                Elements tds = limitElement.select("td");
                if (tds.size() >= 2) {
                    response.setTimeLimit(tds.get(0).text());
                    response.setMemoryLimit(tds.get(1).text());
                }
            }
            
        } catch (Exception e) {
            log.error("문제 상세 정보 크롤링 실패: {}", problemId, e);
        }
    }

    private ProblemResponseDto convertToResponseDto(SolvedAcProblemResponse response) {
        return ProblemResponseDto.builder()
                .problem_id(response.getProblemId())
                .title(response.getTitleKo())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .time_limit(response.getTimeLimit())
                .memory_limit(response.getMemoryLimit())
                .tags(response.getTags().stream()
                        .map(tag -> tag.getDisplayNames().get(0).getName())
                        .collect(Collectors.toList()))
                .build();
    }
} 