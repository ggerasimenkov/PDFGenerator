package com.PDFGenerator.PDFGenerator.service;

import com.PDFGenerator.PDFGenerator.model.Event;
import com.PDFGenerator.PDFGenerator.model.ExcelObject;
import com.PDFGenerator.PDFGenerator.model.WrapperObject;
import com.PDFGenerator.PDFGenerator.utils.CONST;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.java.Log;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTPageSzImpl;
import org.springframework.stereotype.Service;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlTableWriter;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



@Service
@Log
public class ConvertServiceImpl implements ConvertService {

    @Override
    public void convertJson2Csv(String json) throws IOException {

        convertObjectsToCsvFile(convertJsonToExcelObjects(json));

    }

    @Override
    public void convertCSVtoTable() throws IOException {
        String csvFilePath = "src/main/resources/orderLines.csv";
        Table table = Table.read().csv(csvFilePath);
        table.printHtml();
        var htmlSource = new File("src/main/resources/generatedTable.html");
        try (FileWriter writer = new FileWriter(htmlSource)) {
            writer.write(CONST.TABLE_STYLE);
            writer.write(table.printHtml());
            writer.write("</table>");
        }
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter("new-pdf.pdf"));
        pdfDocument.setDefaultPageSize(new PageSize(1600,800));
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDocument);
        HtmlTableWriter.write(table);
        System.out.println(table.fullCopy());
    }

    @Override
    public void convertToDocx() {

    }
    static int CsvStringCounter() {
        int counter =1;
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/orderLines.csv"));
            while (br.readLine() != null){
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return counter;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(CONST.COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }



    public void createWordTableFromList() throws IOException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream("create_table.docx");




        //create table
        XWPFTable table = document.createTable(0,0);
        CTBody body = document.getDocument().getBody();
        if(!body.isSetSectPr()){
            body.addNewSectPr();
        }

        CTSectPr section = body.getSectPr();
        if(!section.isSetPgSz()){
            section.addNewPgSz();
        }

        CTPageSz pageSize = section.getPgSz();
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
//A4 = 595x842 / multiply 20 since BigInteger represents 1/20 Point
        pageSize.setW(BigInteger.valueOf(16840));
        pageSize.setH(BigInteger.valueOf(11900));
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTString styleStr = tblPr.addNewTblStyle();
        styleStr.setVal("StyledTable");

        var numberOfCsvStrings = CsvStringCounter();
        for (var i = 0; i < numberOfCsvStrings - 1; ++i) {

            List<List<String>> records = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/orderLines.csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(CONST.COMMA_DELIMITER);
                    records.add(List.of(values));
                }
            }
            var data = getRecordFromLine(String.valueOf(records.get(i)));

            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                CTTrPr trPr = row.getCtRow().addNewTrPr();
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf(360));}

            XWPFTableRow row = table.createRow();
            if (i == 0){
                row.getCell(0).setText(data.get(0).replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                row.createCell();
                row.getCell(1).setText(data.get(1)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                row.createCell();
                row.getCell(2).setText(data.get(2)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                row.createCell();
                row.getCell(3).setText(data.get(3)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                row.createCell();
                row.getCell(4).setText(data.get(4)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                row.createCell();
                row.getCell(5).setText(data.get(5)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));

            }else{
            for (var j = 0; j < 6; ++j) {
                if (j ==0){
                    row.getCell(0).setText(data.get(j)
                            .replace('[', ' ')
                            .replace('"', ' '));
                }else{
                row.createCell();
                row.getCell(j).setText(data.get(j)
                        .replaceAll("[^A-Za-zА-Яа-я0-9]", " "));
                }

            }
            }
        }
        table.removeRow(0);
        document.write(out);
        out.close();
        log.info(numberOfCsvStrings +" of rows DOCX table successfully written");
    }



    private ExcelObject[] convertJsonToExcelObjects(String json)
    {
        WrapperObject targetObject = new Gson().fromJson(json, WrapperObject.class);

        Event[] listEvents = targetObject.getEvents();
        ExcelObject[] listExcelObject = new ExcelObject[listEvents.length];
        for (int i = 0; i < listEvents.length; i++)
        {
            listExcelObject[i] = ExcelObject.ExcelObjectFrom(listEvents[i]);
        }

        return listExcelObject;
    }

    private void convertObjectsToCsvFile(ExcelObject[] objects) throws IOException {
        JsonNode jsonTree = new ObjectMapper().readTree(new Gson().toJson(objects));

        CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(csvSchemaBuilder::addColumn);
        CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File(CONST.PATH_TO_FILE), jsonTree);
    }

}
