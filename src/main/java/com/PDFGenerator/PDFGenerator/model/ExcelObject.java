package com.PDFGenerator.PDFGenerator.model;

import com.PDFGenerator.PDFGenerator.utils.DataFormatter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExcelObject {

    @SerializedName(value = "Дата")
    private String date;
    @SerializedName(value = "Сотрудник")
    private String trainee;
    @SerializedName(value = "Событие")
    private String eventType;
    @SerializedName(value = "Наставник")
    private String interviewer;
    @SerializedName(value = "Получатели")
    private String[] recipients;
    @SerializedName(value = "Тема")
    private String topic;

    public static ExcelObject ExcelObjectFrom(Event event)
    {
        ExcelObject excelObject = new ExcelObject();
        excelObject.setDate(DataFormatter.format(event.getDate().getDateTime()));
        excelObject.setTrainee(event.getTrainee());
        excelObject.setEventType(event.getEventType());
        excelObject.setInterviewer(event.getInterviewer());
        excelObject.setRecipients(event.getRecipients());
        if (event.getTopic() == null) excelObject.setTopic(" ");
        else excelObject.setTopic(event.getTopic());
        return excelObject;
    }
}
