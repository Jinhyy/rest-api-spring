package me.huk.restapispring.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() {
        Event event = Event.builder()
                .name("jh-event").description("jh-des")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,44))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,3,11,16,50))
                .beginEventDateTime(LocalDateTime.of(2019,3,15,18,30))
                .endEventDateTime(LocalDateTime.of(2019,3,15,20,30))
                .basePrice(100).maxPrice(200).limitOfEnrollment(100).location("수서역")
                .build();

        try {
            mockMvc.perform(post("/api/events/")
                    // header 설정
                    .contentType(MediaType.APPLICATION_JSON_UTF8)   // 요청응답 json 으로 보내겠다.
                    .accept(MediaTypes.HAL_JSON)    // HAL_JSON 타입 응답만 허용하겠다.

                    //body 설정
                    .content(objectMapper.writeValueAsString(event))    // object Mapper로 event 요청 json 형식으로 바꾼다.
            )
                    .andDo(print())
                    .andExpect(status().isCreated())   // created : 201 상태코드
                    .andExpect(jsonPath("id").exists());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}