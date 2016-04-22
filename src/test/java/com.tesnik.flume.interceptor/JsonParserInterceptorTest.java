package com.tesnik.flume.interceptor;


import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import static junit.framework.Assert.*;

public class JsonParserInterceptorTest{

    private Event event;
    private String inputMessageString;
    private JsonParserInterceptor ji;

    @Before
    public void prepare(){
        inputMessageString = "{\"event-topic\":\"test-topic\", \"message\":{\"key1\":\"value1\", \"key2\":\"value2\"}}";
        event = EventBuilder.withBody(inputMessageString.getBytes(), new HashMap<String, String>());
        ji = new JsonParserInterceptor();
        ji.initialize();
    }

    @Test
    public void testInterception(){
        Event interceptedEvent = ji.intercept(event);
        assertEquals("event should have found topic as test-topic ", event.getHeaders().get("topic"), "test-topic");
        assertNotNull("message should be not null", event.getBody());
    }

}

