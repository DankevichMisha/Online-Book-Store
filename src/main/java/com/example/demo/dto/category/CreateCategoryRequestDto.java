package com.example.demo.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank(message = "add the name of category")
        @Size(min = 10, max = 255,
                message = "Name must contain between 10 and 255 symbols")
        String name,
        @NotBlank
        @Size(min = 10, max = 1024,
                message = "description must contain between 10 and 1024 symbols")
        String description) {
}
