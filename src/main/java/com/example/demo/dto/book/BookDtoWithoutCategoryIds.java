package com.example.demo.dto.book;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;

    public BookDtoWithoutCategoryIds(String s, String s1, String s2,
                                     BigDecimal bigDecimal, String s3, Object o) {
    }
}
