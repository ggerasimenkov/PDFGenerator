package com.PDFGenerator.PDFGenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrapperObject {
    private String _id;
    private String weekNumber;
    private String department;
    private Event[] events;
    private String _class;
}