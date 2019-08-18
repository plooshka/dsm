package com.example.dms.repos;

import com.example.dms.domain.Document;
import com.example.dms.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DocumentRepo extends CrudRepository<Document, Long> {


   Document findDocumentById(Long id);

   Iterable<Document> findAllByAuthor(User user);
}
