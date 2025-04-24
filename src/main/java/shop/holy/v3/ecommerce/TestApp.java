package shop.holy.v3.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
public class TestApp {
    public static void main(String[] args) {
//        String[] strs = {"abc,", "def,'efg'"};
        long nums[] = {1, 2, 3, 4, 5};
        String result = Arrays.stream(nums)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        log.info("----------------");
        log.info("{}", result);
        log.info("----------------");
    }
}
