package com.example.infomanagesystem;

import com.example.infomanagesystem.utils.LoginNumRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InfoManageSystemApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private LoginNumRecord loginNumRecord;

    @Test
    void makeId(){
        loginNumRecord.addNum();
    }
    @Test
    void getId(){
        System.out.println(loginNumRecord.getPastSevenDays());
    }

}
