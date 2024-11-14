package com.mycompany.minio.controller;

import com.mycompany.minio.domainmodel.Media;
import com.mycompany.minio.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            return minioService.uploadFile(file.getOriginalFilename(), inputStream, file.getSize(), file.getContentType());
        } catch (Exception e) {
            return "Error occurred: " + e.getMessage();
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) {
        InputStream fileStream = minioService.downloadFile(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream));
    }

    @GetMapping("/all-media-links")
    public List<Media> getAllMediaLinks() {
        return minioService.getAllMediaLinks();
    }
}
