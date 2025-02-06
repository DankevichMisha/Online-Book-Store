package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters params);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);
}
