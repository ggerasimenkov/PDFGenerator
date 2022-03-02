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
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlTableWriter;

import java.io.*;


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
    static int CsvStringCounter() throws FileNotFoundException{
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
    static int NumberOfCsvData(){
        return 1;
    }


    public void createWordTableFromList() throws IOException {
        //Blank Document
        XWPFDocument document = new XWPFDocument();

        //Write the Document in file system
        FileOutputStream out = new FileOutputStream("create_table.docx");

        //create table
        XWPFTable table = document.createTable();
        var numberOfCsvStrings = CsvStringCounter();
        for (var i = 0; i < numberOfCsvStrings; ++i) {
            var row = table.createRow();
            if (i == 0){
                row.createCell();
                row.getCell(0).setText("Дата");
                row.createCell();
                row.getCell(1).setText("Сотрудник");
                row.createCell();
                row.getCell(2).setText("Событие");
                row.createCell();
                row.getCell(3).setText("Наставник");
                row.createCell();
                row.getCell(4).setText("Получатели");
                row.createCell();
                row.getCell(5).setText("Тема");

            }else{
            for (var j = 0; j < 6; ++j) {
                if (j ==0){
                    row.getCell(0).setText("J is: " + 0);
                }else{
                row.createCell();
                row.getCell(j).setText("J is: " + j);
                }
            }
            }
        }
        table.removeRow(0);
        document.write(out);
        out.close();
        log.info(numberOfCsvStrings +"\nDOCX successfully written");
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
