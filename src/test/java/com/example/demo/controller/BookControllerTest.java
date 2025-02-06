package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.service.book.BookService;
import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
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

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;
    private Pageable pageable;
    private BookSearchParameters bookSearchParameters;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto(1L, "Test Book", "Test Author",
                "1234567890123", BigDecimal.valueOf(19.99));
        createBookRequestDto = new CreateBookRequestDto("Test Book", "Test Author",
                "1234567890123", BigDecimal.valueOf(19.99));
        pageable = (Pageable) PageRequest.of(0, 10);
        bookSearchParameters = new BookSearchParameters("Test Book", "Test Author");
    }

    @Test
    void findAll_ShouldReturnPageOfBooks() throws Exception {
        // Arrange
        Page<BookDto> bookPage = new PageImpl<>(List.of(bookDto));
        when(bookService.findAll((org.springframework.data.domain.Pageable) pageable))
                .thenReturn(bookPage);

        // Act & Assert
        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.content[0].author").value(bookDto.getAuthor()));
    }

    @Test
    void getBooksByID_ShouldReturnBookDto() throws Exception {
        // Arrange
        Long bookId = 1L;
        when(bookService.findById(bookId)).thenReturn(bookDto);

        // Act & Assert
        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    }

    @Test
    void createBook_ShouldReturnCreatedBookDto() throws Exception {
        // Arrange
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Book\", \"author\":\"Test Author\""
                                + ", \"isbn\":\"1234567890123\", \"price\":19.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    }

    @Test
    void deleteById_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookService).deleteById(bookId);

        // Act & Assert
        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_ShouldReturnUpdatedBookDto() throws Exception {
        // Arrange
        Long bookId = 1L;
        when(bookService.update(eq(bookId), any(CreateBookRequestDto.class))).thenReturn(bookDto);

        // Act & Assert
        mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Book\", \"author\":\"Updated Author\""
                                + ", \"isbn\":\"1234567890123\", \"price\":25.99}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    }

    @Test
    void search_ShouldReturnListOfBooks() throws Exception {
        // Arrange
        List<BookDto> books = List.of(bookDto);
        when(bookService.search(any(BookSearchParameters.class))).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/books/search")
                        .param("title", bookSearchParameters.titlePart())
                        .param("author", bookSearchParameters.author())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$[0].author").value(bookDto.getAuthor()));
    }
}
