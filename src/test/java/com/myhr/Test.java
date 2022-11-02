package com.myhr;

import com.myhr.utils.DateUtils;
import com.myhr.utils.EncodesUtils;
import com.myhr.utils.JsonUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description
 * @Author yyf
 * @Date 2022-09-28 14:21
 */

@SpringBootTest
public class Test {
    @org.junit.jupiter.api.Test
    public void  test() {

        String aa = "这是一个测试";

        String ss = EncodesUtils.encodeBase64(aa.getBytes());

        String tt = Base64.getEncoder().encodeToString(aa.getBytes());

        System.out.println(ss);
        System.out.println(tt);

//        String expiredTime = "2022-09-09T21:20:20.123";
//        Date time = DateUtils.parseDate(expiredTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
//
//        DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
//        LocalDateTime dateTime = LocalDateTime.parse(expiredTime, pattern1);
//        Date date = Date.from( dateTime.atZone( ZoneId.systemDefault()).toInstant());
//
//
//        Date nowDate = new Date();
//
//        System.out.println(time);
//        System.out.println(date);
//        System.out.println(nowDate);



//        LocalDate start = LocalDate.parse("2022-09-03");
//
//        LocalDate end = LocalDate.parse("2022-09-04");
//        end = end.minusDays(1);
//        String date = end.toString();
//
//        System.out.println(date);




//
//        List<String> ids = new ArrayList<>();
//        ids.add("123");
//        ids.add("111");
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("ids", ids);
//
//        System.out.println(map);
    }
}
