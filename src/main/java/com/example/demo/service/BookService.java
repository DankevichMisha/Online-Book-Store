package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);
}
