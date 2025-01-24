package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Set<CategoryDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = findCategoryById(id);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toEntity(categoryRequestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category categoryFromDb = findCategoryById(id);
        categoryMapper.updateCategoryFromDto(categoryDto, categoryFromDb);
        return categoryMapper.toDto(categoryRepository.save(categoryFromDb));
    }

    @Override
    public void deleteById(Long id) {
        Category categoryById = findCategoryById(id);
        categoryRepository.deleteById(categoryById.getId());
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find category with id: " + id));
    }
}
