package com.example.demo.dto.book;

import java.math.BigDecimal;

public record BookDtoWithoutCategoryIds(String title,
                                        String author,
                                        String isbn,
                                        BigDecimal price,
                                        String description,
                                        String coverImage) {

    // Неконструктивный конструктор должен инициализировать все поля
    public BookDtoWithoutCategoryIds(long id, String testBook) {
        this(testBook, "Unknown Author", "Unknown ISBN", BigDecimal.ZERO,
                "No Description", "No Image");
    }
}
