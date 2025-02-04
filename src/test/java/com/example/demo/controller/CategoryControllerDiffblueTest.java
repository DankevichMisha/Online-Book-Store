package com.example.demo.controller;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.service.book.BookService;
import com.example.demo.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.awt.print.Pageable;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookService bookService;

    private CategoryDto categoryDto;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private Pageable pageable;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto(1L, "Fiction");
        createCategoryRequestDto = new CreateCategoryRequestDto("Fiction");
        pageable = (Pageable) PageRequest.of(0, 10);
        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(1L, "Test Book");
    }

    @Test
    void getAll_ShouldReturnPageOfCategories() throws Exception {
        // Arrange
        Page<CategoryDto> categoryPage = new PageImpl<>(List.of(categoryDto));
        when(categoryService.getAll((org.springframework.data.domain.Pageable) pageable))
                .thenReturn(categoryPage);

        // Act & Assert
        ResultActions resultActions = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(categoryDto.getName()));
    }

    @Test
    void getCategoryById_ShouldReturnCategoryDto() throws Exception {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.getById(categoryId)).thenReturn(categoryDto);

        // Act & Assert
        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDto.getName()));
    }

    @Test
    void createCategory_ShouldReturnCreatedCategoryDto() throws Exception {
        // Arrange
        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(categoryDto);

        // Act & Assert
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Fiction\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryDto.getName()));
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategoryDto() throws Exception {
        // Arrange
        Long categoryId = 1L;
        when(categoryService.update(eq(categoryId), any(CreateCategoryRequestDto.class)))
                .thenReturn(categoryDto);

        // Act & Assert
        mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Fiction\"}"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name").value(categoryDto.getName()));
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteById(categoryId);

        // Act & Assert
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBooksByCategoryId_ShouldReturnListOfBooks() throws Exception {
        // Arrange
        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> books = List.of(bookDtoWithoutCategoryIds);
        when(bookService.getBooksByCategoryId(categoryId, (org.springframework.data.domain.Pageable) pageable)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(bookDtoWithoutCategoryIds.title()));
    }
}
