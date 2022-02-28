package com.PDFGenerator.PDFGenerator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private Date date;
    private String trainee;
    private String eventType;
    private String interviewer;
    private String[] recipients;
    private String topic;
}
