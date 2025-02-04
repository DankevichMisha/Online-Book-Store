package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParameters;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;
    private BookSearchParameters bookSearchParameters;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Создаём объекты для тестов
        book = new Book(1L, "Test Book", "Test Author", "1234567890123", BigDecimal.valueOf(19.99), null);
        bookDto = new BookDto(1L, "Test Book", "Test Author", "1234567890123", BigDecimal.valueOf(19.99));
        createBookRequestDto = new CreateBookRequestDto("Test Book", "Test Author", "1234567890123", BigDecimal.valueOf(19.99));
        bookSearchParameters = new BookSearchParameters("Test Book", "Test Author");
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void save_ShouldReturnSavedBookDto() {
        // Arrange
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.save(createBookRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(bookDto.getId(), result.getId());
        assertEquals(bookDto.getTitle(), result.getTitle());
        verify(bookRepository).save(book);
        verify(bookMapper).toModel(createBookRequestDto);
        verify(bookMapper).toDto(book);
    }

    @Test
    void findAll_ShouldReturnPageOfBookDtos() {
        // Arrange
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(any())).thenReturn(bookDto);

        // Act
        Page<BookDto> result = bookService.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDto.getTitle(), result.getContent().get(0).getTitle());
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(any());
    }

    @Test
    void findById_WithValidId_ShouldReturnBookDto() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.findById(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookDto.getId(), result.getId());
        assertEquals(bookDto.getTitle(), result.getTitle());
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    void findById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.findById(bookId));
        assertEquals("Can't find book by id: " + bookId, exception.getMessage());
    }

    @Test
    void update_WithInvalidId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> bookService.update(bookId, createBookRequestDto));
        assertEquals("Can't find book by id " + bookId, exception.getMessage());
    }

    @Test
    void deleteById_ShouldCallDeleteOnRepository() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);

        // Act
        bookService.deleteById(bookId);

        // Assert
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void search_ShouldReturnListOfBookDtos() {
        // Arrange
        List<Book> bookList = List.of(book);
        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(Specification.where(null));  // Подразумеваем, что спецификация возвращает корректные книги
        when(bookRepository.findAll(any(Specification.class))).thenReturn(bookList);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        List<BookDto> result = bookService.search(bookSearchParameters);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDto.getTitle(), result.get(0).getTitle());
        verify(bookRepository).findAll(any(Specification.class));
        verify(bookMapper).toDto(book);
    }

    @Test
    void getBooksByCategoryId_ShouldReturnListOfBookDtosWithoutCategoryIds() {
        // Arrange
        Long categoryId = 1L;
        List<Book> bookList = List.of(book);
        when(bookRepository.findAllByCategoriesId(categoryId, pageable)).thenReturn(bookList);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(new BookDtoWithoutCategoryIds(1L, "Test Book"));

        // Act
        List<BookDtoWithoutCategoryIds> result = bookService.getBooksByCategoryId(categoryId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Book", result.get(0).title());
        verify(bookRepository).findAllByCategoriesId(categoryId, pageable);
        verify(bookMapper).toDtoWithoutCategories(book);
    }
}
