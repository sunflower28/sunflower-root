package com.sunflower.framework.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class MyNullArrayJsonSerializer extends JsonSerializer<Object> {
    MyNullArrayJsonSerializer() {
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeStartArray();
            jgen.writeEndArray();
        } else {
            jgen.writeObject(value);
        }

    }
}
