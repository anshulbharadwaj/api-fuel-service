package com.assignment.fuel.service.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

@Slf4j
@UtilityClass
public class FileServiceUtility {

    /**
     *  This is a utility method for reading input json file
     * @return given input json into a String
     */
    public static String readJsonFile(String inputFileName) {
        String json = "";
        try {
            json = StreamUtils
                    .copyToString(new ClassPathResource(inputFileName).getInputStream(), Charset.defaultCharset());
        } catch (Exception exception) {
            log.error("Error occurred while reading json file {} ", exception);
        }
        return json;
    }



    /**
     *  This method returns random integer between
     *  Minimum and Maximum Range */
    public static int getRandomInteger(int maximum, int minimum){
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }

}
