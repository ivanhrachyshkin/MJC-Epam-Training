//package com.epam.esm.service;
//
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.ResourceBundle;
//
//public class DummyRb extends ResourceBundle {
//
//    private Map<String, Object> messages;
//
//    public void setMessage(final String key, final Object message) {
//        messages = Collections.singletonMap(key, message);
//    }
//
//    public void setMessages(final Map<String, Object> messages) {
//        this.messages = messages;
//    }
//
//    @Override
//    protected Object handleGetObject(final String key) {
//        return messages.get(key);
//    }
//
//    @Override
//    public Enumeration<String> getKeys() {
//        return null;
//    }
//}
