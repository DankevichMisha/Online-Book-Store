package com.example.demo.service.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
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
public class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Should save a new category successfully")
    void saveCategory_Success() {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto("test", "test");

        Category category = initCategory();
        category.setId(null);

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto expected = new CategoryDto(1L, "test", "test");
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.save(categoryRequestDto);

        assertThat(actual).isEqualTo(expected);
        verify(categoryMapper, times(1)).toEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should return all categories successfully")
    void findAllCategories_Success() {
        Category firstCategory = initCategory();
        Category secondCategory = initCategory();
        secondCategory.setId(2L);
        Category thirdCategory = initCategory();
        thirdCategory.setId(3L);

        List<Category> categories = List.of(firstCategory, secondCategory, thirdCategory);

        CategoryDto firstCategoryResponseDto = new CategoryDto(
                1L, "test", "test");
        CategoryDto secondCategoryResponseDto = new CategoryDto(
                2L, "test", "test");
        CategoryDto thirdCategoryResponseDto = new CategoryDto(
                3L, "test", "test");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(firstCategory)).thenReturn(firstCategoryResponseDto);
        when(categoryMapper.toDto(secondCategory)).thenReturn(secondCategoryResponseDto);
        when(categoryMapper.toDto(thirdCategory)).thenReturn(thirdCategoryResponseDto);

        Set<CategoryDto> expectedCategoryResponseDtos = Set.of(firstCategoryResponseDto,
                secondCategoryResponseDto, thirdCategoryResponseDto);

        Set<CategoryDto> actual = categoryService.getAll(pageable);

        assertThat(actual).isEqualTo(expectedCategoryResponseDtos);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(firstCategory);
        verify(categoryMapper, times(1)).toDto(secondCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Should find category by existing ID successfully")
    void findCategory_withExistingId_Success() {
        Category category = initCategory();
        category.setId(2L);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        CategoryDto expected = new CategoryDto(2L, "test", "test");

        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.getById(category.getId());

        assertThat(actual).isEqualTo(expected);

        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Should throw exception when category with non-existing ID is not found")
    void findCategory_withNonExistingId_ThrowException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                categoryService.getById(anyLong()));
        verify(categoryRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when updating category with non-existing ID")
    void updateCategory_withNonExistingId_ThrowException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("new name",
                "new description");
        Assertions.assertThrows(EntityNotFoundException.class, ()
                -> categoryService.update(anyLong(), requestDto));
    }

    private Category initCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("test");
        category.setDescription("test");
        return category;
    }
}
