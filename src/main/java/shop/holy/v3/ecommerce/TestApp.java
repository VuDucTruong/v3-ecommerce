package shop.holy.v3.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class TestApp {
    public static void main(String[] args) {
        Date date = new Date();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(date);
            log.info("Serialized date: {}", json);

            Date deserializedDate = objectMapper.readValue(json, Date.class);
            log.info("Deserialized date: {}", deserializedDate);
        } catch (Exception e) {
            log.error("Error during serialization/deserialization", e);
        }

    }
}
