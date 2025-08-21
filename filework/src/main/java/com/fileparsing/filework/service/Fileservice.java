package com.fileparsing.filework.service;

import com.fileparsing.filework.model.Filework;
import com.fileparsing.filework.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class Fileservice {

    @Autowired
    FileRepository fileRepo;

    private static final String TMP_DIR = "uploads/tmp";
    private static final String FINAL_DIR = "uploads";

    private final String folder ="uploads/";

    public Filework storingData(MultipartFile file) throws IOException {
        Path path = Paths.get(folder);
        if(!Files.exists(path)){
            Files.createDirectories(path);
        }
        String filePath = folder + file.getOriginalFilename();

        Filework filework = new Filework(file.getOriginalFilename(),filePath);
        filework.setProgress(0);
        filework.setStatus("uploading");
        fileRepo.save(filework);

        long fileTotalSize = file.getSize();
        long fileUploadSize = 0;

        try (var inputStream = file.getInputStream();
             var outputStream = Files.newOutputStream(Paths.get(filePath))) {  // <-- fixed

            byte[] fileByte = new byte[4096];
            int fileRead;

            while ((fileRead = inputStream.read(fileByte)) != -1) {
                outputStream.write(fileByte, 0, fileRead);
                fileUploadSize += fileRead;

                int progress = (int) ((fileUploadSize * 100) / fileTotalSize);

                // update progress dynamically
                filework.setProgress(progress);
                if (progress == 0) {
                    filework.setStatus("uploading");
                } else if (progress < 100) {
                    filework.setStatus("processing");
                } else {
                    filework.setStatus("ready");
                }
                fileRepo.save(filework);
            }
        }
        Filework response = new Filework();
        response.setId(filework.getId());
        response.setName(filework.getName());
        response.setFilePath(filework.getFilePath());
        return response;
//        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
    }
}
