package com.assignment.fuel.service.controller;

import com.assignment.fuel.service.FuelMessageController;
import com.assignment.fuel.service.resource.EventDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FuelMessageController.class)
public class FuelMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    JmsTemplate jmsTemplate;

    @Test
    @SneakyThrows
    public void test_publish_event_details_is_created_successfully() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/publish/event-detail")
                .content(asJsonString(populateEventDetail()))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private EventDetail populateEventDetail() {
        EventDetail eventDetail = new EventDetail();
        eventDetail.setFuelLid(Boolean.FALSE);
        eventDetail.setCity("Delhi");
        return eventDetail;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
