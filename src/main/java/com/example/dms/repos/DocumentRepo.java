package com.example.dms.repos;

import com.example.dms.domain.Document;
import com.example.dms.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


public interface DocumentRepo extends CrudRepository<Document, Long> {

   Page<Document> findAll(Pageable pageable);

   Document findDocumentById(Long id);

   Page<Document> findAllByAuthor(User user, Pageable pageable);

}
