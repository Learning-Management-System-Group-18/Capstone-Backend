package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CategoryService.class)
class CategoryServiceTest {

    @MockBean
    private  UploadFileService uploadFileService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    void getCategory_Success() {
        List<Category> categoryList = new ArrayList<>();
        Category category = Category.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        categoryList.add(category);
        Integer countUser = 1;
        Integer countCourse = 2;

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        Pageable pageable = PageRequest.of(0,1);
        Page<Category> categoryPage = new PageImpl<Category>(categoryList.subList(0,1), pageable, categoryList.size());

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(orderRepository.countOrderByCourse_CategoryIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(courseRepository.countCourseByCategoryId(anyLong())).thenReturn(countCourse);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.getCategory(0, 1);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        List<CategoryDto> result = ((List<CategoryDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("category", result.get(0).getTitle());
        assertEquals("description", result.get(0).getDescription());



    }

    @Test
    void getCategory_Error() {
        when(categoryRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.getCategory(0, 1);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void getAllCategory_Success() {
        List<Category> categoryList = new ArrayList<>();
        Category category = Category.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        categoryList.add(category);
        Integer countUser = 1;
        Integer countCourse = 2;

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(orderRepository.countOrderByCourse_CategoryIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(courseRepository.countCourseByCategoryId(anyLong())).thenReturn(countCourse);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.getAllCategory();
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        List<CategoryDto> result = ((List<CategoryDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("category", result.get(0).getTitle());
        assertEquals("description", result.get(0).getDescription());


    }

    @Test
    void getAllCategory_Error() {
        when(categoryRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.getAllCategory();
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void getCategoryById_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        Integer countCourse = 2;

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("category")
                .description("description")
                .urlImage("image")
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(courseRepository.countCourseByCategoryId(anyLong())).thenReturn(countCourse);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.getCategoryById(1L);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("category", ((CategoryDto) apiResponse.getData()).getTitle());
    }

    @Test
    void getCategoryById_categoryEmpty() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = categoryService.getCategoryById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getCategoryById_Error() {
        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.getCategoryById(1L);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void createNewCategory_Success() throws IOException {
        Map<String, String> metadata = new HashMap<>();
        Category category = Category.builder()
                .id(1L)
                .title("title")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        Boolean exist = false;

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());
        when(categoryRepository.existsByTitle(anyString())).thenReturn(exist);
        when(mapper.map(any(),eq(Category.class))).thenReturn(category);
        when(categoryRepository.save(any())).thenReturn(category);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.createNewCategory(categoryDto,file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(uploadFileService,times(1)).upload(any(),any(),any(),any());


    }

    @Test
    void createNewCategory_existName() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();
        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        Boolean exist = true;
        when(categoryRepository.existsByTitle(anyString())).thenReturn(exist);

        ResponseEntity<Object> responseEntity = categoryService.createNewCategory(categoryDto,file);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }


    @Test
    void createNewCategory_Error() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());
        when(categoryRepository.save(any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.createNewCategory(categoryDto,file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void updateByIdWithImage_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("title")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.updateById(1L,categoryDto,file);

        verify(uploadFileService,times(1)).delete(any(),any());
        verify(uploadFileService,times(1)).upload(any(),any(),any(),any());

    }

    @Test
    void updateByIdWithoutImage_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("title")
                .build();

        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        MockMultipartFile file = null;

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);
        when(mapper.map(any(),eq(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<Object> responseEntity = categoryService.updateById(1L,categoryDto,file);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    void updateById_IdNotFound() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = categoryService.updateById(1L,categoryDto,file);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateById_Error() {
        CategoryDto categoryDto = CategoryDto.builder()
                .id(1L)
                .title("title")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());
        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.updateById(1L,categoryDto,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void deleteById_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("title")
                .build();


        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        ResponseEntity<Object> responseEntity = categoryService.deleteById(anyLong());

        verify(uploadFileService, times(1)).delete(any(),any());
        verify(categoryRepository, times(1)).delete(category);

    }

    @Test
    void deleteById_IdNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = categoryService.deleteById(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_Error() {
        when(categoryRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = categoryService.deleteById(anyLong());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}