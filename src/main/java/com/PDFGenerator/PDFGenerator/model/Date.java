package com.PDFGenerator.PDFGenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Date {
    private String dateTime;
    private String zone;
}