package com.example.capstone.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.capstone.constant.AppConstant;
import com.example.capstone.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import java.util.Optional;


@Service
@Slf4j
public class UploadFileService {
    @Autowired
    private AmazonS3 amazonS3;

    public void upload(String path,
                       String fileName,
                       Optional<Map<String, String>> optionalMetaData,
                       InputStream inputStream){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        optionalMetaData.ifPresent(map -> {
            if(!map.isEmpty()){
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e){
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }


    public ResponseEntity<Object> download(String path, String filename){
        log.info("Sending file with name {}",filename);
        try {
            S3Object object = amazonS3.getObject(path, filename);
            S3ObjectInputStream objectContent = object.getObjectContent();
            byte[] imageByte = IOUtils.toByteArray(objectContent);
            return ResponseEntity.ok().body(imageByte);

        } catch (AmazonServiceException | IOException e) {
            log.error("Error occurred while trying to create resource URL. Error : {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
        }
    }

    public void delete(String path, String fileName){
        log.info("Executing delete directory image");
        amazonS3.deleteObject(path,fileName);
    }
}
