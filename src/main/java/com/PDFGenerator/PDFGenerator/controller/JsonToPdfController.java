package com.PDFGenerator.PDFGenerator.controller;

import com.PDFGenerator.PDFGenerator.service.ConvertService;
import com.PDFGenerator.PDFGenerator.storage.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/convert")
@AllArgsConstructor
public class JsonToPdfController {
    private final ConvertService convertService;
    private final StorageService storageService;

    @GetMapping("/generateTable")
    public ResponseEntity<Resource> convertCSVtoTable(@RequestBody String json) throws IOException {
        convertService.convertJson2Csv(json);
        Resource file = storageService.loadAsResource();
        convertService.convertCSVtoTable();
        convertService.convertToDocx();
        convertService.createWordTableFromList();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
