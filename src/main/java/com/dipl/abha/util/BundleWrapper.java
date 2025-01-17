package com.dipl.abha.util;
import org.hl7.fhir.r4.model.Bundle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

@JsonIgnoreProperties({"extension", "extensionFirstRep"}) // Add more properties if needed
@ToString
public class BundleWrapper {
    private Bundle bundle;

    public BundleWrapper(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }
}