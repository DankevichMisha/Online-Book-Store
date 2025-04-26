package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import com.example.demo.dto.book.BookDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    public static final String DB_SCRIPT_PATH = "database/test/book_controller.sql";

    private static MockMvc mockMvc;


    //    @MockBean
    //    private BookService bookService;
    //
    //    @MockBean
    //    private JwtUtil jwtUtil;
    //
    //    private BookService bookService;
    //    private BookDto bookDto;
    //    private CreateBookRequestDto createBookRequestDto;
    //    private Pageable pageable;
    //    private BookSearchParameters bookSearchParameters;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext, @Autowired DataSource dataSource) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        executeSqlScript(dataSource, DB_SCRIPT_PATH);
    }

    private static void executeSqlScript(DataSource dataSource, String scriptPath) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(scriptPath));
        }
    }
    //    @BeforeEach
    //    void setUp() {
    //        bookDto = new BookDto(1L, "Test Book", "Test Author",
    //                "1234567890123", BigDecimal.valueOf(19.99));
    //        createBookRequestDto = new CreateBookRequestDto("Test Book", "Test Author",
    //                "1234567890123", BigDecimal.valueOf(19.99));
    //        pageable = PageRequest.of(0, 10);
    //        bookSearchParameters = new BookSearchParameters("Test Book", "Test Author");
    //
    //        // Мокируем JWT токен
    //        when(jwtUtil.validateToken(any())).thenReturn(true);
    //        when(jwtUtil.getUserName(any())).thenReturn("testuser");
    //}

    @Test
    @WithMockUser(roles = "USER") // Только авторизованные пользователи могут видеть книги
    void findAll_ShouldReturnPageOfBooks() throws Exception {
        //  when(bookService.findAll(any(Pageable.class))).thenReturn(bookPage);
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<BookDto> actual = objectMapper.readValue(
                jsonResponse,
                new TypeReference<List<BookDto>>() {}
        );

        BookDto book1 = new BookDto(1L, "Test book 1", "Test author 1",
                "978-3-16-148410-0", new BigDecimal("99.99"))
                .setDescription("test 1")
                .setCoverImage(null);

        BookDto book2 = new BookDto(2L, "Test book 2", "Test author 2",
                "978-0-12-345678-9", new BigDecimal("151.22"))
                .setDescription("test 2")
                .setCoverImage(null);

        BookDto book3 = new BookDto(3L, "Test book 3", "Test author 3",
                "978-1-23-456789-0", new BigDecimal("149.99"))
                .setDescription("test 3")
                .setCoverImage(null);

        BookDto book4 = new BookDto(4L, "Test book 4", "Test author 4",
                "978-9-87-654321-0", new BigDecimal("250.99"))
                .setDescription("test 4")
                .setCoverImage(null);
        List<BookDto> expected = List.of(book1, book2, book3, book4);
        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    //    @Test
    //    @WithMockUser(roles = "USER")
    //    void getBooksByID_ShouldReturnBookDto() throws Exception {
    //        Long bookId = 1L;
    //        when(bookService.findById(bookId)).thenReturn(bookDto);
    //
    //        mockMvc.perform(get("/books/{id}", bookId)
    //                        .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
    //                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "ADMIN") // Создание книги доступно только администратору
    //    void createBook_ShouldReturnCreatedBookDto() throws Exception {
    //        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(bookDto);
    //
    //        mockMvc.perform(post("/books")
    //                        .contentType(MediaType.APPLICATION_JSON)
    //                        .content("{\"title\":\"Test Book\", \"author\":\"Test Author\","
    //                                + "\"isbn\":\"1234567890123\", \"price\":19.99}"))
    //                .andExpect(status().isCreated())
    //                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
    //                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "ADMIN") // Удаление доступно только администратору
    //    void deleteById_ShouldReturnNoContent() throws Exception {
    //        Long bookId = 1L;
    //        doNothing().when(bookService).deleteById(bookId);
    //
    //        mockMvc.perform(delete("/books/{id}", bookId)
    //                        .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isNoContent());
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "ADMIN") // Обновление доступно только администратору
    //    void update_ShouldReturnUpdatedBookDto() throws Exception {
    //        Long bookId = 1L;
    //        when(bookService.update(eq(bookId), any(CreateBookRequestDto.class)))
    //        .thenReturn(bookDto);
    //
    //        mockMvc.perform(put("/books/{id}", bookId)
    //                        .contentType(MediaType.APPLICATION_JSON)
    //                        .content("{\"title\":\"Updated Book\", \"author\":\"Updated Author\","
    //                                + "\"isbn\":\"1234567890123\", \"price\":25.99}"))
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
    //                .andExpect(jsonPath("$.author").value(bookDto.getAuthor()));
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "USER")
    //    void search_ShouldReturnListOfBooks() throws Exception {
    //        List<BookDto> books = List.of(bookDto);
    //        when(bookService.search(any(BookSearchParameters.class))).thenReturn(books);
    //
    //        mockMvc.perform(get("/books/search")
    //                        .param("title", bookSearchParameters.titlePart())
    //                        .param("author", bookSearchParameters.author())
    //                        .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$[0].title").value(bookDto.getTitle()))
    //                .andExpect(jsonPath("$[0].author").value(bookDto.getAuthor()));
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "USER") // Проверяем доступ для пользователя (должен быть запрещен)
    //    void createBook_ShouldReturnForbiddenForUser() throws Exception {
    //        mockMvc.perform(post("/books")
    //                        .contentType(MediaType.APPLICATION_JSON)
    //                        .content("{\"title\":\"Test Book\", \"author\":\"Test Author\","
    //                                + "\"isbn\":\"1234567890123\", \"price\":19.99}"))
    //                .andExpect(status().isForbidden()); // 403 Forbidden
    //    }
    //
    //    @Test
    //    @WithMockUser(roles = "USER") // Проверяем удаление с ролью USER (должно быть запрещено)
    //    void deleteById_ShouldReturnForbiddenForUser() throws Exception {
    //        Long bookId = 1L;
    //
    //        mockMvc.perform(delete("/books/{id}", bookId)
    //                        .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isForbidden()); // 403 Forbidden
    //    }
}
