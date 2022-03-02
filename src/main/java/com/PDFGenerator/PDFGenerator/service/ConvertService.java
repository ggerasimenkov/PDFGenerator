package com.PDFGenerator.PDFGenerator.service;


import java.io.FileNotFoundException;
import java.io.IOException;

public interface ConvertService {

    void convertJson2Csv(String json) throws IOException;
    void convertCSVtoTable() throws IOException;
    void convertToDocx() throws FileNotFoundException;
    void createWordTableFromList() throws IOException;

}