package com.algoanalyzer.controller;

import com.algoanalyzer.dto.ProblemRequestDto;
import com.algoanalyzer.service.ProblemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getProblem_ValidProblemId_ReturnsOk() throws Exception {
        // given
        Long problemId = 1000L;

        // when & then
        mockMvc.perform(get("/api/problems/{problemId}", problemId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.problemId").value(problemId))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.level").exists())
                .andExpect(jsonPath("$.tags").exists());
    }

    @Test
    void searchProblem_ValidRequest_ReturnsOk() throws Exception {
        // given
        ProblemRequestDto request = new ProblemRequestDto();
        request.setProblemId(1000L);

        // when & then
        mockMvc.perform(post("/api/problems/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.problemId").value(1000))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.level").exists())
                .andExpect(jsonPath("$.tags").exists());
    }
} 