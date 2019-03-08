package me.huk.restapispring.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() {
        try {
            mockMvc.perform(post("api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)   // 요청응답 json 으로 보내겠다.
                    .accept(MediaTypes.HAL_JSON)    // HAL_JSON 타입 응답만 허용하겠다.
            )
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}