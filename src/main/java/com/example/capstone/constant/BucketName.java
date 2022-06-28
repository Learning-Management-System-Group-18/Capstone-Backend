package com.example.capstone.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    CATEGORY_IMAGE("capstone-lms-storage");
    private final String bucketName;
}
