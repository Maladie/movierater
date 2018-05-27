package com.ex.movierater.examples;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;

@Component
public class JsonExamples {

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public String getRequestBodyExample(String filename) {
        JSONParser parser = new JSONParser();
        final ClassPathResource resource = new ClassPathResource(filename);

        try {
            FileReader filereader = new FileReader(resource.getFile());
            Object parse = parser.parse(filereader);
            JSONObject jsonObject = (JSONObject) parse;
            return jsonObject.toJSONString();
        } catch (Exception e) {
            log.error("Exception during json parse");
            return "";
        }
    }

}
