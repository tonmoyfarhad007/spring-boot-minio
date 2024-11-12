package com.mycompany.minio.service;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;


    public String uploadFile(String objectName, InputStream fileInputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(fileInputStream, size, -1)
                            .contentType(contentType)
                            .build());
            return "File uploaded successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    public InputStream downloadFile(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getAllMediaLinks() {
        List<String> mediaLinks = new ArrayList<>();
        try {
            // List all objects in the specified bucket
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());

            // Generate a presigned URL for each object
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();

                String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS)  // Set expiration as needed
                        .build());

                mediaLinks.add(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaLinks;
    }
}
