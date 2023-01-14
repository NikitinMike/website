package com.example.website;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordsBookRepository extends CrudRepository<WordBookEntity, Long> {
    List<WordBookEntity> findAllByWord(String word);
}