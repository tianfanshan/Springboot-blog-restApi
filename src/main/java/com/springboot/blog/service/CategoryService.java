package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.payload.CategoryResponse;


public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(Long id);

    CategoryResponse getAllCategories(int pageNo, int pageSize,String sortBy,String sortDir);

    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    String deleteCategoryById(Long categoryId);
}
