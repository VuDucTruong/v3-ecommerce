package shop.holy.v3.ecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
public class TestApp {
    public static void main(String[] args) throws JsonProcessingException {
//        String[] strs = {"abc,", "def,'efg'"};
        long nums[] = {1, 2, 3, 4, 5};
        System.out.println(nums.toString());
        String result = Arrays.stream(nums)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        log.info("----------------");
        log.info("{}", result);
        log.info("----------------");
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        LocalDate localDate = LocalDate.now();
        String str = localDate.toString();
        LocalDate test = LocalDate.parse(str);
        log.info("{}",test);
    }
}
