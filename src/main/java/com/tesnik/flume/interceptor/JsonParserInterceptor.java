package com.tesnik.flume.interceptor;

import com.google.gson.Gson;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tiwariaa on 2/11/2016.
 */
public class JsonParserInterceptor implements Interceptor {

    private final static Logger logger = Logger.getLogger(JsonParserInterceptor.class);

    public JsonParserInterceptor() {
    }

    public void initialize() {
    }

    public Event intercept(Event event) {

        //Creating new event
        Event newEvent = null;
        // This is the event's body
        String body = new String(event.getBody());
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = (Map<String, Object>) gson.fromJson(body, map.getClass());
        try {

            event.getHeaders().put("topic", findNewTopic(gson, map));
            newEvent = EventBuilder.withBody(getMessageToPass(gson, map).getBytes(), event.getHeaders());
            // Let the enriched event go
            return newEvent;
        } catch (Exception e) {
            throw new RuntimeException("Error while parsing the json data");
        }
    }


    public List<Event> intercept(List<Event> events) {

        List<Event> interceptedEvents =
                new ArrayList<Event>(events.size());
        for (Event event : events) {
            // Intercept any event
            Event interceptedEvent = intercept(event);
            interceptedEvents.add(interceptedEvent);
        }

        return interceptedEvents;
    }

    public String findNewTopic(Gson gson, Map<String, Object> s) {
        return (String) s.get("event-topic");
    }

    public String getMessageToPass(Gson gson, Map<String, Object> map){
        Map<String, Object> nameAndValues = (Map<String, Object>) map.get("message");
        return gson.toJson(nameAndValues);
    }


    public void close() {
    }

    public static class Builder implements Interceptor.Builder {

        public void configure(Context context) {
        }

        public Interceptor build() {
            return new JsonParserInterceptor();
        }
    }
}