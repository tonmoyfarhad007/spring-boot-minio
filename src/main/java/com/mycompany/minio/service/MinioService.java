package com.mycompany.minio.service;

import com.mycompany.minio.domainmodel.Media;
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

    public List<Media> getAllMediaLinks() {
        List<Media> mediaList = new ArrayList<>();
        try {
            // List all objects in the specified bucket
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );

            // Generate a presigned URL for each object and create a Media object
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();

                String url = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName)
                                .object(objectName)
                                .expiry(7, TimeUnit.DAYS)  // Set expiration as needed
                                .build()
                );

                // Create a Media object for each item (add logic to retrieve title/description if available)
                String title = "Title for " + objectName;  // Placeholder title
                String description = "Description for " + objectName;  // Placeholder description
                Media media = new Media(title, description, url);

                mediaList.add(media);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaList;
    }
}
