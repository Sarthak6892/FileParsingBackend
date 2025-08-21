package com.fileparsing.filework.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fileparsing.filework.model.Filework;
import com.fileparsing.filework.repository.FileRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Service
public class FileParsing {
    @Autowired
    private FileRepository fileRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void parseFile(Filework filework) {
        try {
            filework.setStatus("processing");
            fileRepo.save(filework);

            // simulate progress updates
            for (int i = 0; i <= 80; i += 20) {
                filework.setProgress(i);
                fileRepo.save(filework);
                Thread.sleep(2000); // sleep for demo
            }

            File file = new File(filework.getFilePath());
            String parsedContent = null;

            if (file.getName().endsWith(".csv")) {
                parsedContent = parseCsv(file);
            } else if (file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                parsedContent = parseExcel(file);
            } else if (file.getName().endsWith(".pdf")) {
                parsedContent = parsePdf(file);
            } else {
                filework.setStatus("failed");
                fileRepo.save(filework);
                return;
            }

            filework.setParsedData(parsedContent);
            filework.setStatus("ready");
            filework.setProgress(100);
            fileRepo.save(filework);

        } catch (Exception e) {
            filework.setStatus("failed");
            fileRepo.save(filework);
        }
    }

    private String parseCsv(File file) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            String[] headers = {};
            if (scanner.hasNextLine()) {
                headers = scanner.nextLine().split(",");
            }
            while (scanner.hasNextLine()) {
                String[] values = scanner.nextLine().split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], i < values.length ? values[i] : "");
                }
                records.add(row);
            }
        }
        return objectMapper.writeValueAsString(records);
    }

    private String parseExcel(File file) throws Exception {
        List<Map<String, String>> records = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.toString());
            }
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Map<String, String> record = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    record.put(headers.get(j), cell != null ? cell.toString() : "");
                }
                records.add(record);
            }
        }
        return objectMapper.writeValueAsString(records);
    }

    private String parsePdf(File file) throws Exception {
        List<Map<String, Object>> pages = new ArrayList<>();
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            int totalPages = document.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                String text = stripper.getText(document);
                Map<String, Object> page = new HashMap<>();
                page.put("page", i);
                page.put("text", text.trim());
                pages.add(page);
            }
        }
        return objectMapper.writeValueAsString(pages);
    }
}
