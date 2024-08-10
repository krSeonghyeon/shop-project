package com.bcsd.shop.service;

import com.bcsd.shop.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.bcsd.shop.exception.errorcode.FileErrorCode.*;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveImage(MultipartFile imageFile) {
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        try {
            Path directory = Paths.get(uploadDir);
            Path filePath = directory.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath);
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new CustomException(FILE_SAVE_FAILED);
        }
    }

    public void deleteImage(String imagePath) {
        try {
            String fileName = Paths.get(imagePath).getFileName().toString();
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new CustomException(FILE_DELETE_FAILED);
        }
    }

    public Resource getImage(String fileName) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new CustomException(FILE_NOT_FOUND);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new CustomException(FILE_READ_FAILED);
        }
    }

    public String determineContentType(String fileName) {
        try {
            Path imagePath = Paths.get(uploadDir).resolve(fileName).normalize();
            return Files.probeContentType(imagePath);
        } catch (IOException e) {
            throw new CustomException(FILE_READ_FAILED);
        }
    }
}
