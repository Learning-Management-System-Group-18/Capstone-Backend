package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
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


    public ResponseEntity<Object> getAllCategory(int page, int size) {
        log.info("Executing get all category");
        try {
            Pageable pageable = PageRequest.of(page-1,size);

            Page<Category> categoryList = categoryRepository.findAll(pageable);

            List<CategoryDto> request = new ArrayList<>();

            for (Category category: categoryList){
                Integer countCourse = courseRepository.countCourseByCategoryId(category.getId());
                CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
                categoryDto.setCountCourse(countCourse);
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
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file");
            }

            //check if the file is an image
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_BMP.getMimeType(),
                    IMAGE_GIF.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
                throw new IllegalStateException("FIle uploaded is not an image");
            }

            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "category-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
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

    public ResponseEntity<Object> showImage(Long id){
        log.info("Getting category image");
        try {
            Category category = categoryRepository.findById(id).get();
            return uploadFileService.download(category.getUrlBucket(), category.getImageFileName());
        }catch (Exception e) {
            return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,"Image Not Found",HttpStatus.BAD_REQUEST);
        }
        }


    public ResponseEntity<Object> updateById(Long id, CategoryDto request,MultipartFile file){
        log.info("Executing update existing category");
        if (categoryRepository.existsByTitle(request.getTitle())) {
            log.info("Category with name : {} already exist", request.getTitle());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Category with name already exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()){
                log.info("Category with Id [{}] not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }


            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "category-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlImage = String.format("%s/%s/%s/%s",pattern,"category-images",uuid,fileName);
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

            log.info("Succesfully updated category with Id : [{}]",id);
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












