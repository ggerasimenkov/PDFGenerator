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
import org.springframework.stereotype.Service;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.html.HtmlTableWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

@Service
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
        pdfDocument.setDefaultPageSize(new PageSize(1600,1080));
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDocument);
        HtmlTableWriter.write(table);
        System.out.println(table.fullCopy());
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
