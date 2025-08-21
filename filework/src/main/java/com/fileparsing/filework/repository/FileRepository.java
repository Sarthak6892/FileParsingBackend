package com.fileparsing.filework.repository;

import com.fileparsing.filework.model.Filework;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<Filework,String> {

}
