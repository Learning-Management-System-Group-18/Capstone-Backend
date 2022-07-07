package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Tool;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.OrderRepository;
import com.example.capstone.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.capstone.constant.BucketName;


import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private  UploadFileService uploadFileService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<Object> getCategory(Integer page, Integer size) {
        log.info("Executing get all category with pagination");
        try {
            Pageable pageable = PageRequest.of(page,size);

            Page<Category> categoryList = categoryRepository.findAll(pageable);

            List<CategoryDto> request = new ArrayList<>();

            for (Category category: categoryList){
                Integer countUser = orderRepository.countOrderByCourse_CategoryId(category.getId());
                Integer countCourse = courseRepository.countCourseByCategoryId(category.getId());
                CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
                categoryDto.setCountCourse(countCourse);
                categoryDto.setCountUser(countUser);

                request.add(categoryDto);
            }

            log.info("Successfully retrieved all Category with pagination");
            return ResponseUtil.build(ResponseCode.SUCCESS, request ,HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all Category. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllCategory() {
        log.info("Executing get all category");
        try {
            List<Category> categoryList = categoryRepository.findAll();
            List<CategoryDto> request = new ArrayList<>();

            for (Category category: categoryList){
                Integer countUser = orderRepository.countOrderByCourse_CategoryId(category.getId());
                Integer countCourse = courseRepository.countCourseByCategoryId(category.getId());
                CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
                categoryDto.setCountCourse(countCourse);
                categoryDto.setCountUser(countUser);
                request.add(categoryDto);
            }

            log.info("Successfully retrieved all Category");
            return ResponseUtil.build(ResponseCode.SUCCESS, request ,HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all Category. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCategoryById(Long id) {
        log.info("Executing get Category with ID : {}", id);
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isEmpty()) {
                log.info("category with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Integer countCourse = courseRepository.countCourseByCategoryId(id);
            CategoryDto request = mapper.map(category, CategoryDto.class);
            request.setCountCourse(countCourse);

            log.info("Successfully retrieved Category with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
        } catch (Exception e ) {
            log.error("An error occurred while trying to get category with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> createNewCategory(CategoryDto request, MultipartFile file) {
        log.info("Executing add new category");
        if (categoryRepository.existsByTitle(request.getTitle())) {
            log.info("Category with name : {} already exist", request.getTitle());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Category with name already exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        try {

            //check if the file is empty
            if (file.isEmpty()) {
                log.info("Cannot upload empty file");
                return ResponseUtil.build(ResponseCode.IMAGE_EMPTY,null,HttpStatus.BAD_REQUEST);
            }

            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "category-images", uuid);
            String urlImage = String.format("%s/%s/%s/%s",pattern,"category-images",uuid,fileName);
            uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

            //and then save category in database
            Category category = mapper.map(request, Category.class);
            category.setImageFileName(fileName);
            category.setUrlBucket(urlBucket);
            category.setUrlImage(urlImage);
            categoryRepository.save(category);


            log.info("Successfully added new category");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(category, CategoryDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new category. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateById(Long id, CategoryDto request,MultipartFile file){
        log.info("Executing update existing category with Id [{}]", id);
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()){
                log.info("Category with Id [{}] not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            if(file == null) {
                log.info("File is null, update category without file");
                optionalCategory.ifPresent(category -> {
                    category.setId(id);
                    category.setTitle(request.getTitle());
                    category.setDescription(request.getDescription());
                    categoryRepository.save(category);
                });

                log.info("Successfully updated category without image with Id : [{}]",id);
            } else {
                log.info("File is not null, updated course with file");

                // delete path old
                Category oldCategory = categoryRepository.findById(id).get();
                uploadFileService.delete(oldCategory.getUrlBucket(),oldCategory.getImageFileName());

                //get file metadata
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));

                //save image in s3
                String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
                String uuid = UUID.randomUUID().toString().replace("-", "");
                String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "category-images", uuid);
                String fileName = String.format("%s", file.getOriginalFilename());
                String urlImage = String.format("%s/%s/%s/%s", pattern, "category-images", uuid, fileName);
                uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

                optionalCategory.ifPresent(category -> {
                    category.setId(id);
                    category.setTitle(request.getTitle());
                    category.setDescription(request.getDescription());
                    category.setUrlBucket(urlBucket);
                    category.setUrlImage(urlImage);
                    category.setImageFileName(fileName);
                    categoryRepository.save(category);
                });

                log.info("Successfully updated category with image with Id : [{}]",id);
            }
            return ResponseUtil.build(ResponseCode.SUCCESS,mapper.map(optionalCategory.get(),CategoryDto.class),HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to update existing category. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing category");
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()){
                log.info("Category with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Category category = categoryRepository.findById(id).get();
            uploadFileService.delete(category.getUrlBucket(),category.getImageFileName());
            categoryRepository.delete(optionalCategory.get());
            log.info("Successfully delete category with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,null,HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing category. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}












