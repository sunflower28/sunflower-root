package com.sunflower.framework.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public final class CustomerJsonComponent {
    private CustomerJsonComponent() {
    }

    public static class TrimmingJsonDeserializer extends JsonDeserializer<String> {
        public TrimmingJsonDeserializer() {
        }

        @Override
        public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            return parser.hasToken(JsonToken.VALUE_STRING) ? parser.getValueAsString().trim() : null;
        }
    }
}