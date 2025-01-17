package com.dipl.abha.util;
import java.io.IOException;

import org.hl7.fhir.r4.model.Bundle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BundleSerializer extends JsonSerializer<Bundle> {
    @Override
    public void serialize(Bundle bundle, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        // Write only the necessary fields to avoid recursion
        gen.writeStringField("id", bundle.getId());
        // Add other fields you need to serialize here
        gen.writeEndObject();
    }
}