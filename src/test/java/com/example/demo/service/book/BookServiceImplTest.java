package com.example.demo.service.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import com.example.demo.repository.category.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    private static final Long ID = 1L;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Test
    @DisplayName("""
            This test verifies the successful saving of a book with 
            valid data (all required fields are filled) 
            into the database. 
            The method ensures that the book is correctly saved and 
            that the saved record matches the provided data.
            """)
    void saveValidBook_Success() {
        Category category = initCategory();

        CreateBookRequestDto requestDto = initRequestBookDto();

        Book book = initBook(Set.of(category));

        BookDto expected = initResponseDtoBook();

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("""
            This test verifies the retrieval of all books from the database. 
            The method ensures that a list 
            of all books is returned correctly and 
            contains all necessary fields, without any filtering.
            """)
    void getAllBooks_Success() {
        Category category = initCategory();
        Book firstBook = initBook(Set.of(category));
        Book secondBook = initBook(Set.of(category));
        secondBook.setId(2L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(firstBook, secondBook));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        BookDto firstBookResponseDto = initResponseDtoBook();
        BookDto secondBookResponseDto = initResponseDtoBook();

        List<BookDto> expected = List.of(firstBookResponseDto, secondBookResponseDto);

        when(bookMapper.toDto(firstBook)).thenReturn(firstBookResponseDto);
        when(bookMapper.toDto(secondBook)).thenReturn(secondBookResponseDto);

        List<BookDto> actual = bookService.findAll(pageable);

        assertThat(actual).isEqualTo(expected);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(firstBook);
        verify(bookMapper, times(1)).toDto(secondBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            This test verifies the successful retrieval of a book by its existing ID. 
            The method checks that the book with the provided ID is found, 
            and the returned data matches the expected values.
            """)
    void getBook_withExistingId_Success() {
        Book book = initBook(Set.of(initCategory()));

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));

        BookDto expected = initResponseDtoBook();

        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(ID);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(ID);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("""
            This test verifies the behavior when attempting to retrieve a book 
            with a non-existing ID. The method is expected to 
            throw an exception (such as EntityNotFoundException), 
            since no book with that ID exists.
            """)
    void getBook_withNonExistingId_ThrowException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(anyLong()));
    }

    @Test
    @DisplayName("""
            This test verifies the successful deletion of a\s
            book with an existing ID.\s
            The method ensures that the book is deleted\s
            from the database and that no record of it remains.
           \s""")
    void deleteBook_withExistingId_Success() {
        Category category = initCategory();
        Book book = initBook(Set.of(category));

        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(anyLong());
        bookService.deleteById(ID);

        verify(bookRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Test for deleting a book with a non-existing ID, 
            expecting an EntityNotFoundException
            """)
    void deleteBook_withNonExistingId_ThrowException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(ID));
    }

    @Test
    @DisplayName("""
            This test verifies the successful retrieval of 
            books by category ID. 
            The method checks that books belonging to 
            the specified category are correctly returned.
            """)
    void findBookByCategoryId_Success() {
        Category category = initCategory();
        category.setId(12L);

        Book firstBook = initBook(Set.of(category));

        Pageable pageable = PageRequest.of(0, 10);

        BookDtoWithoutCategoryIds
                bookWithoutCategoryIdsResponseDto = initBookWithoutCategory();

        List<Book> books = List.of(firstBook);

        List<BookDtoWithoutCategoryIds>
                expected = List.of(bookWithoutCategoryIdsResponseDto);

        when(bookRepository.findAllByCategoriesId(category.getId(), pageable))
                .thenReturn(books);

        when(bookMapper.toDtoWithoutCategories(firstBook))
                .thenReturn(bookWithoutCategoryIdsResponseDto);

        List<BookDtoWithoutCategoryIds> actual = bookService.getBooksByCategoryId(
                category.getId(), pageable);

        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findAllByCategoriesId(category.getId(), pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategories(firstBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private Book initBook(Set<Category> categories) {
        Book book = new Book();
        book.setId(ID);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setIsbn("978-0-12-345678-9");
        book.setPrice(BigDecimal.valueOf(10099.99));
        book.setDescription("test description");
        book.setCoverImage(null);
        book.setCategories(categories);
        return book;
    }

    private Category initCategory() {
        Category category = new Category();
        category.setId(ID);
        category.setName("test category");
        category.setDescription("test description");
        return category;
    }

    private BookDto initResponseDtoBook() {
        BookDto responseDto = new BookDto();
        responseDto.setTitle("test title");
        responseDto.setAuthor("test author");
        responseDto.setIsbn("978-0-12-345678-9");
        responseDto.setPrice(BigDecimal.valueOf(10099.99));
        responseDto.setDescription("test description");
        responseDto.setCoverImage(null);
        responseDto.setCategoryIds(Set.of(ID));
        return responseDto;
    }

    private BookDtoWithoutCategoryIds initBookWithoutCategory() {
        return new BookDtoWithoutCategoryIds(
                "test title",
                "test author",
                "978-0-12-345678-9",
                BigDecimal.valueOf(10099.99),
                "test description",
                null
        );
    }

    private String[] titles() {
        return new String[]{"test 1", "test 2"};
    }

    private String[] authors() {
        return new String[] {"Bob", "Alice"};
    }

    private String[] isbns() {
        return new String [] {"978-0-12-345678-9", "978-9-87-654321-0"};
    }

    private String[] prices() {
        return new String[] {"200", "500"};
    }

    private String[] descriptions() {
        return new String[] {"test desc", "test desc"};
    }

    private String[] categoriesIds() {
        return new String[] {"1", "2", "3"};
    }

    private CreateBookRequestDto initRequestBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("test title");
        requestDto.setAuthor("test author");
        requestDto.setIsbn("978-0-12-345678-9");
        requestDto.setPrice(BigDecimal.valueOf(10099.99));
        requestDto.setDescription("test description");
        requestDto.setCoverImage(null);
        requestDto.setCategoryIds(Set.of(ID));
        return requestDto;
    }
}
