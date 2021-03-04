package com.assignment.fuel.service;

import com.assignment.fuel.service.resource.EventDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/publish")
@Slf4j
public class FuelMessageController {

    private final JmsTemplate jmsTemplate;
    @Value("${fuel.service.queue}")
    private String queueToSend;

    /**
     *  This method can be used for publishing message to the queue
     * @param eventDetail event detail.
     */

    @PostMapping("/event-detail")
    @ResponseStatus(HttpStatus.CREATED)  //TODO Implement logic for Method level security using @PreAuthorize
    public void sendMessage(@Valid @RequestBody EventDetail eventDetail) {
        eventDetail.setLocalDateTime(LocalDateTime.now());
        jmsTemplate.convertAndSend(queueToSend, eventDetail);
    }
}
