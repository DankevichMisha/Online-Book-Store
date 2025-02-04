package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryRequestDto categoryRequestDto;

    @BeforeEach
    void setUp() {
        // Создаём объекты для тестов
        category = new Category(1L, "Test Category");
        categoryDto = new CategoryDto(1L, "Test Category");
        categoryRequestDto = new CreateCategoryRequestDto("Test Category");
    }

    @Test
    void getAll_ShouldReturnPageOfCategoryDtos() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(any())).thenReturn(categoryDto);

        // Act
        Page<CategoryDto> result = categoryService.getAll(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(categoryDto.getName(), result.getContent().get(0).getName());
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(any());
    }

    @Test
    void getById_WithValidId_ShouldReturnCategoryDto() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.getById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void getById_WithInvalidId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> categoryService.getById(categoryId));
        assertEquals("Can't find category by id " + categoryId, exception.getMessage());
    }

    @Test
    void save_ShouldReturnSavedCategoryDto() {
        // Arrange
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.save(categoryRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        verify(categoryRepository).save(category);
        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryMapper).toDto(category);
    }

    @Test
    void update_ShouldReturnUpdatedCategoryDto() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // Act
        CategoryDto result = categoryService.update(categoryId, categoryRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(categoryMapper).updateCategoryFromDto(categoryRequestDto, category);
    }

    @Test
    void update_WithInvalidId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> categoryService.update(categoryId, categoryRequestDto));
        assertEquals("Can't find category by id " + categoryId, exception.getMessage());
    }

    @Test
    void deleteById_ShouldCallDeleteOnRepository() {
        // Arrange
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteById(categoryId);

        // Act
        categoryService.deleteById(categoryId);

        // Assert
        verify(categoryRepository).deleteById(categoryId);
    }
}
