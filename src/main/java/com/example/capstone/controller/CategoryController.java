package com.example.capstone.controller;

import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@AllArgsConstructor
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping(
            path = "/admin/category",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createNewCategory(@ModelAttribute CategoryDto request,
                                                    @RequestPart(value = "image")MultipartFile file){
        return categoryService.createNewCategory(request,file);
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getAllCategory(@RequestParam ("page") int page,
                                                 @RequestParam ("size") int size) {
        return categoryService.getAllCategory(page,size);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getOneCategory(@RequestParam(value = "id") Long id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping(value = "/category/{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Object> showImage(@PathVariable Long id){
        return categoryService.showImage(id);
    }

    @PutMapping( path = "/admin/category",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateCategory(@RequestParam(value = "id") Long id, @ModelAttribute CategoryDto request,
                                                 @RequestParam("file") MultipartFile file){
        return categoryService.updateById(id,request,file);
    }

    @DeleteMapping("/admin/category")
    public ResponseEntity<Object> deleteCategory(@RequestParam(value = "id") Long id){
        return categoryService.deleteById(id);
    }
}
