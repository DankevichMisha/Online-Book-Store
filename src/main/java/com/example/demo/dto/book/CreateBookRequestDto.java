package com.example.demo.dto.book;

import com.example.demo.validation.user.book.Isbn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank(message = "Add the title of the book")
    private String title;
    @NotBlank(message = "Add the author of the book")
    private String author;
    @Isbn
    private String isbn;
    @NotNull(message = "price can't be null")
    @Positive(message = "price can't be less than 1")
    private BigDecimal price;
    @Size(min = 10,
            max = 200,
            message = "Description must contain 10 symbols")
    private String description;
    private String coverImage;
    @NotEmpty
    private Set<Long> categoryIds;
}
