package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Tool;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.ToolDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ToolService.class)
class ToolServiceTest {
    @MockBean
    private  UploadFileService uploadFileService;

    @MockBean
    private ToolRepository toolRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ToolService toolService;

    @Test
    void getAllToolByCourseId_Success() {
        List<Tool> tools = new ArrayList<>();
        List<ToolDto> toolDtos = new ArrayList<>();
        Tool tool = Tool.builder()
                .id(1L)
                .name("tool")
                .build();

        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .build();

        tools.add(tool);
        toolDtos.add(toolDto);


        when(toolRepository.findAllByCourseId(anyLong())).thenReturn(tools);
        when(mapper.map(any(),eq(ToolDto.class))).thenReturn(toolDto);

        ResponseEntity<Object> responseEntity = toolService.getAllToolByCourseId(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<ToolDto> result = (List<ToolDto>) apiResponse.getData();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("tool", result.get(0).getName());
    }

    @Test
    void getAllToolByCourseId_Error() {
        when(toolRepository.findAllByCourseId(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = toolService.getAllToolByCourseId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void newTool_Success() {

        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Tool tool = Tool.builder()
                .id(1L)
                .name("tool")
                .course(course)
                .build();

        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .course(courseDto)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());



        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(mapper.map(any(), eq(Tool.class))).thenReturn(tool);
        when(toolRepository.save(any())).thenReturn(tool);
        when(mapper.map(any(), eq(ToolDto.class))).thenReturn(toolDto);

        ResponseEntity<Object> responseEntity = toolService.createNewTool(1L,toolDto,file);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("tool", ((ToolDto) apiResponse.getData()).getName());

        verify(uploadFileService, times(1)).upload(any(),any(),any(),any());

    }

    @Test
    void newTool_Error() {
        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.createNewTool(1L,toolDto,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }


    @Test
    void updateById_Success() {
        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Tool tool = Tool.builder()
                .id(1L)
                .name("tool")
                .course(course)
                .build();

        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .course(courseDto)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(toolRepository.findById(anyLong())).thenReturn(Optional.of(tool));
        when(toolRepository.save(any())).thenReturn(tool);
        when(mapper.map(any(), eq(ToolDto.class))).thenReturn(toolDto);

        ResponseEntity<Object> responseEntity = toolService.updateById(1L,toolDto,file);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("tool", ((ToolDto) apiResponse.getData()).getName());

        verify(uploadFileService,times(1)).delete(any(),any());
        verify(uploadFileService,times(1)).upload(any(),any(),any(),any());
    }

    @Test
    void updateById_NotFound() {
        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(toolRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = toolService.updateById(1L,toolDto,file);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void updateById_ImageEmpty_Success() {
        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Tool tool = Tool.builder()
                .id(1L)
                .name("tool")
                .course(course)
                .build();

        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")
                .course(courseDto)
                .build();


        when(toolRepository.findById(anyLong())).thenReturn(Optional.of(tool));
        when(toolRepository.save(any())).thenReturn(tool);
        when(mapper.map(any(), eq(ToolDto.class))).thenReturn(toolDto);

        ResponseEntity<Object> responseEntity = toolService.updateById(1L,toolDto,null);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("tool", ((ToolDto) apiResponse.getData()).getName());


    }

    @Test
    void updateById_Error() {
        ToolDto toolDto = ToolDto.builder()
                .id(1L)
                .name("tool")

                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(toolRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = toolService.updateById(1L,toolDto,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

}