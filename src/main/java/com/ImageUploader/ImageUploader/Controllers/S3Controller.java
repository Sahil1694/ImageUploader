package com.ImageUploader.ImageUploader.Controllers;

import com.ImageUploader.ImageUploader.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private ImageUploader imageUploader;

    @PostMapping
    public ResponseEntity<?>uploadImage(@RequestParam MultipartFile file){
        return ResponseEntity.ok(imageUploader.uploadImage(file));
    }
    //Get all files
   @GetMapping
    public List<String> getAllFiles(){
        return imageUploader.allFiles();
    }

    @GetMapping("/{fileName}")
    public String getImageUrl(@PathVariable("fileName") String fileName){
        return imageUploader.getImageUrlByName(fileName);
    }


}
