package com.official.pium.controller;

import static com.official.pium.fixture.PetPlantFixture.REQUEST.피우미_등록_요청;
import static com.official.pium.fixture.PetPlantFixture.REQUEST.피우미_수정_요청;
import static com.official.pium.fixture.PetPlantFixture.RESPONSE;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.official.pium.UITest;
import com.official.pium.service.PetPlantService;
import com.official.pium.service.dto.PetPlantResponse;
import com.official.pium.service.dto.PetPlantUpdateRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(controllers = PetPlantController.class)
class PetPlantControllerTest extends UITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetPlantService petPlantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class 반려_식물_ {

        @Test
        void 등록하면_201을_반환() throws Exception {
            PetPlantResponse response = RESPONSE.피우미_응답;
            given(petPlantService.create(any(), any()))
                    .willReturn(response);

            mockMvc.perform(post("/pet-plants")
                            .content(objectMapper.writeValueAsString(피우미_등록_요청))
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl("/pet-plants/" + response.getId()))
                    .andDo(print());
        }

        @Test
        void 조회하면_200을_반환() throws Exception {
            given(petPlantService.read(anyLong()))
                    .willReturn(RESPONSE.피우미_응답);

            mockMvc.perform(get("/pet-plants/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void 잘못된_ID로_조회하면_400을_반환() throws Exception {
            mockMvc.perform(get("/pet-plants/{id}", -1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("반려 식물 ID는 1이상의 값이어야 합니다.")))
                    .andDo(print());
        }

        @Test
        void 전체_조회하면_200을_반환() throws Exception {
            given(petPlantService.readAll(any()))
                    .willReturn(RESPONSE.식물_전체조회_응답);

            mockMvc.perform(get("/pet-plants"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void 잘못된_ID로_수정하면_400을_반환() throws Exception {
            Long wrongId = -1L;

            mockMvc.perform(patch("/pet-plants/{id}", wrongId)
                            .content(objectMapper.writeValueAsString(피우미_수정_요청))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("반려 식물 ID는 1이상의 값이어야 합니다.")))
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void 닉네임_없이_수정하면_400을_반환(String nickname) throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname(nickname)
                    .location("침대 옆")
                    .flowerpot("유리병")
                    .waterCycle(10)
                    .light("빛 많이 필요함")
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("반려 식물 닉네임은 필수 값입니다.")))
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void 화분_정보_없이_수정하면_400을_반환(String flowerpot) throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot(flowerpot)
                    .location("침대 옆")
                    .waterCycle(10)
                    .light("빛 많이 필요함")
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("화분 정보는 필수 값입니다.")))
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void 화분_위치_없이_수정하면_400을_반환(String location) throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("유리병")
                    .location(location)
                    .waterCycle(10)
                    .light("빛 많이 필요함")
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("화분 위치는 필수 값입니다.")))
                    .andDo(print());
        }

        @Test
        void 물주기_주기_없이_수정하면_400을_반환() throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(null)
                    .light("빛 많이 필요함")
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("물주기 주기 값은 필수 값입니다.")))
                    .andDo(print());
        }

        @Test
        void 물주기_주기_음수로_수정하면_400을_반환() throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(-10)
                    .light("빛 많이 필요함")
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("물주기 주기 값은 양수만 가능합니다.")))
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void 조도_정보_없이_수정하면_400을_반환(String light) throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(10)
                    .light(light)
                    .wind("바람이 잘 통하는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("조도 정보는 필수 값입니다.")))
                    .andDo(print());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void 통풍_정보_없이_수정하면_400을_반환(String wind) throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(10)
                    .light("밝은 곳")
                    .wind(wind)
                    .birthDate(LocalDate.now())
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("통풍 정보는 필수 값입니다.")))
                    .andDo(print());
        }

        @Test
        void 입양일_없이_수정하면_400을_반환() throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(10)
                    .light("밝은 곳")
                    .wind("바람이 불어오는 곳")
                    .birthDate(null)
                    .lastWaterDate(LocalDate.now())
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("반려 식물 입양일은 필수 값입니다.")))
                    .andDo(print());
        }

        @Test
        void 마지막_물주기_날짜_없이_수정하면_400을_반환() throws Exception {
            PetPlantUpdateRequest updateRequest = PetPlantUpdateRequest.builder()
                    .nickname("피우미2")
                    .flowerpot("플라스틱 병")
                    .location("침대 옆")
                    .waterCycle(10)
                    .light("밝은 곳")
                    .wind("바람이 불어오는 곳")
                    .birthDate(LocalDate.now())
                    .lastWaterDate(null)
                    .build();

            mockMvc.perform(patch("/pet-plants/{id}", 1L)
                            .content(objectMapper.writeValueAsString(updateRequest))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(containsString("마지막 물주기 날짜는 필수 값입니다.")))
                    .andDo(print());
        }
    }
}
