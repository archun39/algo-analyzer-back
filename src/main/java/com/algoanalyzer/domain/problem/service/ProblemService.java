package com.algoanalyzer.domain.problem.service;

import java.util.stream.Collectors;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/problem/show?problemId=";
    private static final String BOJ_PROBLEM_URL = "https://www.acmicpc.net/problem/";
    private final RestTemplate restTemplate;

    public ProblemResponseDto getProblem(int problemId) {
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

    private void fetchProblemDetails(int problemId, SolvedAcProblemResponse response) {
        try {
            String bojUrl = BOJ_PROBLEM_URL + problemId;
            Document doc = Jsoup.connect(bojUrl)
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36")
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                                .header("Connection", "keep-alive")
                                .timeout(5000)
                                .get();

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

            // 시간 제한과 메모리 제한
            Elements limitElements = doc.select("table#problem-info tbody tr");
            if (limitElements.size() >= 2) { // 최소 두 줄이 있는지 확인
                for (int i = 0; i < limitElements.size(); i++) {
                    Elements ths = limitElements.get(i).select("th");
                    Elements tds = limitElements.get(i).select("td");
                    if (ths.size() > 0 && tds.size() > 0) {
                        String label = ths.get(0).text();
                        String value = tds.get(0).text();
                        if (label.contains("시간 제한")) {
                            response.setTimeLimit(value);
                        } else if (label.contains("메모리 제한")) {
                            response.setMemoryLimit(value);
                        }
                    }
                }
            } else {
                log.warn("시간 제한과 메모리 제한을 찾을 수 없습니다: {}", problemId);
            }

        } catch (Exception e) {
            log.error("문제 상세 정보 크롤링 실패: {}", problemId, e);
        }
    }

    private ProblemResponseDto convertToResponseDto(SolvedAcProblemResponse response) {
        return ProblemResponseDto.builder()
                .problemId(response.getProblemId())
                .title(response.getTitleKo())
                .description(response.getDescription())
                .input(response.getInput())
                .output(response.getOutput())
                .timeLimit(response.getTimeLimit())
                .memoryLimit(response.getMemoryLimit())
                .tags(response.getTags().stream()
                        .map(tag -> {
                            if (tag.getDisplayNames() != null && !tag.getDisplayNames().isEmpty()) {
                                return tag.getDisplayNames().get(0).getName();
                            } else {
                                return "Unknown";
                            }
                        })
                        .collect(Collectors.toList()))
                .build();
    }
} 