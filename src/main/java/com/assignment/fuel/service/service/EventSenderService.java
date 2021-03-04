package com.assignment.fuel.service.service;

import com.assignment.fuel.service.model.Country;
import com.assignment.fuel.service.resource.EventDetail;
import com.assignment.fuel.service.utility.FileServiceUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventSenderService {

    private final JmsTemplate jmsTemplate;
    private static final String INPUT_FILE_INDIAN_CITIES = "IndianCities.json";

    @Value("${fuel.service.queue}")
    private String queueToSend;

    /**
     * This method emits event at an interval of 2 minutes
     * with payload being city name and fuel Lid value along
     * with timestamp
     */
    @Scheduled(fixedRate = 120000)
    public void emitEvent() {
        EventDetail eventDetail = populateEventDetail();
        log.info("Event emitted is:: {},{},{}", eventDetail.getCity(), eventDetail.getLocalDateTime(),
                eventDetail.isFuelLid());
        jmsTemplate.convertAndSend(queueToSend, eventDetail);
    }

    /**
     * This method populates Event Details
     *
     * @return Event Detail
     */
    private EventDetail populateEventDetail() {
        EventDetail eventDetail = new EventDetail();
        eventDetail.setCity(processJsonToFindRandomCity());
        eventDetail.setLocalDateTime(LocalDateTime.now());
        int randomNumber = FileServiceUtility.getRandomInteger(1, 0);
        if (randomNumber == 0) {
            eventDetail.setFuelLid(Boolean.FALSE);
        } else {
            eventDetail.setFuelLid(Boolean.TRUE);
        }
        return eventDetail;
    }

    /**
     * This method returns city name randomly generated from list of cities
     * collated under json file
     *
     * @return city name
     */
    private String processJsonToFindRandomCity() {
        String cityName = "";
        try {
            JSONArray jsonArray = new JSONArray(FileServiceUtility.readJsonFile(INPUT_FILE_INDIAN_CITIES));
            ObjectMapper objectMapper = new ObjectMapper();
            Country[] countryDetail = objectMapper.readValue(jsonArray.toString(), Country[].class);
            int maxSize = countryDetail.length;
            int minSize = 1;
            int randomNumber = FileServiceUtility.getRandomInteger(maxSize, minSize);
            Optional<Country> filteredResult = Arrays.stream(countryDetail)
                    .filter(f -> f.getId() == randomNumber)
                    .findFirst();

            cityName = filteredResult.get().getName();
        } catch (JsonProcessingException jpe) {
            log.error("Error processing json {} ", jpe);
        } catch (Exception exception) {
            log.error("Error occurred while processing json {} ", exception);
        }
        return cityName;
    }

}
