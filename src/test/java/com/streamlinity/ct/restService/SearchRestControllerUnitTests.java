package com.streamlinity.ct.restService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamlinity.ct.model.Item;
import com.streamlinity.ct.restService.challenge.SearchSvcInterface;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@WebMvcTest
public class SearchRestControllerUnitTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ApplicationContext appContext;

    // See  https://github.com/Pragmatists/junitparams-spring-integration-example/blob/master/README.md
    // for an explanation on running this with JunitParams under spring

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    // Overrides real bean in test or main
    @MockBean
    private SearchSvcInterface mockSearchSvc;

    private List<Item> items;


    @Test
    public void testGetAllItems() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:3Items.json";


        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        items = mapper.readValue(jsonFile, new TypeReference<List<Item>>() {
        });

        Mockito.when(mockSearchSvc.getItems()).thenReturn(items);


        mvc.perform(get("/item")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(items)));


    }

    @Test
    public void testGetItemByShortName() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:3Items.json";
        final String classPathAdjustedC4 = "classpath:C4Items.json";

        List<Item> c4Items;
        final List<Item> emptyList = new ArrayList<>();


        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        items = mapper.readValue(jsonFile, new TypeReference<List<Item>>() {
        });

        File c4jsonFile = appContext.getResource(classPathAdjustedC4).getFile();
        c4Items = mapper.readValue(c4jsonFile, new TypeReference<List<Item>>() {
        });

        // Check C4 - should return 1 element
        Mockito.when(mockSearchSvc.getItem("C4")).thenReturn(c4Items);

        mvc.perform(get("/item/C4")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(c4Items)));

        // Check SX - should return 0 element


        Mockito.when(mockSearchSvc.getItem("SX")).thenReturn(emptyList);

        ResultActions resultActions = mvc.perform(get("/item/SX")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(emptyList)));

    }


    @Test
    public void testGetItemByCategory() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        final String classPathAdjustedDataFileName = "classpath:3Items.json";
        final String c4FileName = "classpath:C4Items.json";
        final String ssFileName = "classpath:SSCategoryItemsFrom3Items.json";

        List<Item> c4Items;
        List<Item> ssItems;
        final List<Item> emptyList = new ArrayList<>();

        File jsonFile = appContext.getResource(classPathAdjustedDataFileName).getFile();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        items = mapper.readValue(jsonFile, new TypeReference<List<Item>>() {
        });

        File c4jsonFile = appContext.getResource(c4FileName).getFile();
        c4Items = mapper.readValue(c4jsonFile, new TypeReference<List<Item>>() {
        });

        File ssJsonFile = appContext.getResource(ssFileName).getFile();
        ssItems = mapper.readValue(ssJsonFile, new TypeReference<List<Item>>() {
        });

        // Check C4 - should return 1 element
        Mockito.when(mockSearchSvc.getItems("C4")).thenReturn(c4Items);

        mvc.perform(get("/item?category=C4")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(c4Items)));

        // Check SX - should return 0 element

        Mockito.when(mockSearchSvc.getItems("SX")).thenReturn(emptyList);

        ResultActions resultActions = mvc.perform(get("/item?category=SX")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(emptyList)));

        // Check S - should return 0 element

        Mockito.when(mockSearchSvc.getItems("S")).thenReturn(emptyList);

        resultActions = mvc.perform(get("/item?category=S")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(emptyList)));


        // Check SS - should return 2 element

        Mockito.when(mockSearchSvc.getItems("SS")).thenReturn(ssItems);

        resultActions = mvc.perform(get("/item?category=SS")).
                andExpect(status().isOk()).
                andExpect(content().json(mapper.writeValueAsString(ssItems)));


    }


}
