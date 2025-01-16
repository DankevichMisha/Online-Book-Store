package com.example.demo.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.ISBN;

@Data
@Accessors(chain = true)
public class CreateBookRequestDto {
    @NotBlank(message = "add the title of the book")
    private String title;
    @NotBlank(message = "add the author of the book")
    private String author;
    @ISBN
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
    private List<Long> categoriesId;
}
