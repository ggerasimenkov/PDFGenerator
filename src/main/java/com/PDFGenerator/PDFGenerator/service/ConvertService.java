package com.PDFGenerator.PDFGenerator.service;


import java.io.IOException;

public interface ConvertService {

    void convertJson2Csv(String json) throws IOException;
    void convertCSVtoTable() throws IOException;

}