package com.example.BackendWebbanhang.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${test.upload-file.base-uri}")
    private String baseURI;

    public void createDirectory(String folder) {
        // Sử dụng Paths.get trực tiếp với String path
        Path path = Paths.get(folder);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectories(path); // Dùng createDirectories (có chữ s) để tạo cả các folder cha nếu cần
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws IOException {
        // Xử lý tên file: Thay thế khoảng trắng bằng dấu gạch dưới để tránh lỗi URL
        String originalName = file.getOriginalFilename();
        if (originalName != null) {
            originalName = originalName.replace(" ", "_");
        }

        // Tạo tên file duy nhất
        String finalName = System.currentTimeMillis() + "-" + originalName;

        // Dùng Paths.get(base, sub, filename) để tự động nối các dấu gạch chéo chuẩn
        // xác
        Path path = Paths.get(baseURI, folder, finalName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    public long getFileLength(String fileName, String folder) {
        Path path = Paths.get(baseURI, folder, fileName);
        File tmpDir = new File(path.toString());

        if (!tmpDir.exists() || tmpDir.isDirectory())
            return 0;
        return tmpDir.length();
    }

    public InputStreamResource getResource(String fileName, String folder)
            throws FileNotFoundException {
        Path path = Paths.get(baseURI, folder, fileName);
        File file = new File(path.toString());
        return new InputStreamResource(new FileInputStream(file));
    }
}