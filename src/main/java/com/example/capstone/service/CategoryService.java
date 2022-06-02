package com.example.capstone.service;

import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    public ResponseEntity<Object> addNew(CategoryDto request) {
        log.info("Executing add new category");
        try {
            Category category = mapper.map(request, Category.class);
            categoryRepository.save(category);

            log.info("Successfully added new category");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(category, CategoryDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new facility type. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
