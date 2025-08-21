package com.fileparsing.filework.controller;

import com.fileparsing.filework.model.Filework;
import com.fileparsing.filework.repository.FileRepository;
import com.fileparsing.filework.service.FileParsing;
import com.fileparsing.filework.service.Fileservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileRepository fileRepo;

    @Autowired
    Fileservice fileService;

    @Autowired
    FileParsing fileParsing;

    // read-by-id
    @GetMapping("{id}/progress")
    public Filework getFileById(@PathVariable String id){
        return fileRepo.findById(id).orElse(null);
    }

    @GetMapping("/{id}")
    public Filework getFiledataById(@PathVariable String id) {
        return fileRepo.findById(id).orElse(null);
    }

    @PostMapping("/upload")
    public Filework uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Filework filework = fileService.storingData(file);

        new Thread(() -> fileParsing.parseFile(filework)).start();

        return filework;
    }
    @GetMapping
    public List<Filework> retrieve(){
    return fileRepo.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteFile(@PathVariable String id){
        if(!fileRepo.existsById(id)){
            throw new RuntimeException("File Not Found with File id " + id);
        }
        fileRepo.deleteById(id);
        return "File Deleted";
    }
}
