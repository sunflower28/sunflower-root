package com.sunflower.framework.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

class MyNullIntegerJsonSerializer extends JsonSerializer<Object> {
    MyNullIntegerJsonSerializer() {
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (value == null) {
            jgen.writeNumber(0);
        }

    }
}
