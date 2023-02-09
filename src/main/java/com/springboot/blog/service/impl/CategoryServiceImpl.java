package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.CategoryResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category category = mapToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return mapToDto(savedCategory);
    }

    @Override
    public CategoryDto getCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id.toString()));
        return mapToDto(category);
    }

    @Override
    public CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);

        Page<Category> postsPageable = categoryRepository.findAll(pageable);

        // get content for page object
        List<Category> categories = postsPageable.getContent();

        List<CategoryDto> content = categories.stream().map(category -> mapToDto(category)).collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(content);
        categoryResponse.setLast(postsPageable.isLast());
        categoryResponse.setPageNo(postsPageable.getNumber());
        categoryResponse.setPageSize(postsPageable.getSize());
        categoryResponse.setTotalPages(postsPageable.getTotalPages());
        categoryResponse.setTotalElements(postsPageable.getTotalElements());

        return categoryResponse;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId.toString()));

        category.setId(categoryId);
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return mapToDto(updatedCategory);
    }

    @Override
    public String deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId.toString()));
        categoryRepository.delete(category);
        return "Category with id" + categoryId + " deleted successfully";
    }

    private CategoryDto mapToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        return modelMapper.map(categoryDto, Category.class);
    }
}
