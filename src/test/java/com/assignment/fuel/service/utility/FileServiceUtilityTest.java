package com.assignment.fuel.service.utility;

import com.assignment.fuel.service.utility.FileServiceUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileServiceUtilityTest {

    @Test
    public void test_readJsonFile() {
        String json = FileServiceUtility.readJsonFile("IndianCities.json");
        assertNotNull(json);
    }
}
