package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Section;
import com.example.capstone.domain.dao.Tool;
import com.example.capstone.domain.dto.SectionDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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

}