# File Parsing Backedn (Spring Boot + MongoDB)

This project allows you to upload files (CSV, Excel, PDF) and parses them into JSON format.  
It also tracks the upload status and progress for each file

 Features
- Upload file (CSV, XLSX, PDF)
- Track upload status and progress
- Parse file content into JSON
- Store everything in MongoDB
- Fetch progress and parsed data using file ID

Tech Stack
- Java 24
- Spring Boot
- MongoDB
- Maven
- Postman (for testing)

# API Endpoints
## 1. Upload File
    POST /files/upload
    Form-Data:
    file: <choose file>

## 2. Get All Files
     GET /files

## 3. Get File by ID
     GET /files/{id}

## 4. Check Progress
    GET /files/{id}/progress

## 5. Delete File
    DELETE/files/{id}   

## How to Run

1. Clone the repo
   ```bash
   git clone <repo-url>

## Testing :You can test all APIs using Postman

1.Upload File
Method: POST

URL:(http://localhost:8080/files/upload)

Body: form-data

2.Get All Files
Method: GET

URL: http://localhost:8080/files)

3.Track Upload/Processing Progress
Method: GET

URL: http://localhost:8080/files/{id}/progress)

4. Get file by id
Method: GET

URL: http://localhost:8080/files/{id})

5.Delete File
Method: DELETE

URL: http://localhost:8080/files/{id})

Run the Spring Boot app

## Postman Collections
[Link to my Postman Collection](./testAPI.postman_collection.json)
