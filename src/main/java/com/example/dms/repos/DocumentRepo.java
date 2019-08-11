package com.example.dms.repos;

import com.example.dms.domain.Document;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepo extends CrudRepository<Document, Long> {

}
