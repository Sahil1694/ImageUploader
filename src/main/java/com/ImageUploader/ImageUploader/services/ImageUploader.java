package com.ImageUploader.ImageUploader.services;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageUploader {


      String uploadImage(MultipartFile file);

      List<String> allFiles();

      String preSignUrl(String filename);

      String getImageUrlByName(String filename);
}
