package com.btp.layout.model;

import java.util.Map;
import java.util.HashMap;

public interface EditableProperties {
    Map<String, String> getEditableProperties();
    void setEditableProperty(String key, String value);
    String getEditableProperty(String key);
}

class ChairProperties implements EditableProperties {
    private final Map<String, String> properties;

    public ChairProperties() {
        properties = new HashMap<>();
        // Default properties for chairs
        properties.put("material", "Wood");
        properties.put("cushioned", "Yes");
        properties.put("style", "Standard");
    }

    @Override
    public Map<String, String> getEditableProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public void setEditableProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getEditableProperty(String key) {
        return properties.get(key);
    }
}

class TableProperties implements EditableProperties {
    private final Map<String, String> properties;

    public TableProperties() {
        properties = new HashMap<>();
        // Default properties for tables
        properties.put("material", "Wood");
        properties.put("shape", "Rectangle");
        properties.put("seats", "4");
    }

    @Override
    public Map<String, String> getEditableProperties() {
        return new HashMap<>(properties);
    }

    @Override
    public void setEditableProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getEditableProperty(String key) {
        return properties.get(key);
    }
} 