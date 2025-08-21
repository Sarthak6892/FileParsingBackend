package com.fileparsing.filework.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "filework")
public class Filework {
    @Id
    private String id;
    private String name;
    private String filePath;
    private String status;
    private int progress;
    private String parsedData;


    public Filework() {
    }

    public Filework(String originalFilename, String filePath) {
        this.name = originalFilename;
        this.filePath = filePath;
        this.progress = 0;
        this.status = "uploading";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getParsedData() {
        return parsedData;
    }

    public void setParsedData(String parsedData) {
        this.parsedData = parsedData;
    }

    @Override
    public String toString() {
        return "Filework{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                ", status='" + status + '\'' +
                ", progress=" + progress +
                ", parsedData='" + parsedData + '\'' +
                '}';
    }
}
