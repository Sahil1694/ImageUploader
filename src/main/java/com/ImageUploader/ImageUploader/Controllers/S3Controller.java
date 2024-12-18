package com.ImageUploader.ImageUploader.Controllers;

import com.ImageUploader.ImageUploader.services.ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//S3Controller For S3
@RestController
@RequestMapping("/api/v1/s3")
public class S3Controller {

    @Autowired
    private ImageUploader imageUploader;
    //Upload Image
    @PostMapping
    public ResponseEntity<?>uploadImage(@RequestParam MultipartFile file){
        return ResponseEntity.ok(imageUploader.uploadImage(file));
    }
    //Get all files
    @GetMapping
    public List<String> getAllFiles(){
        return imageUploader.allFiles();
    }
    //Get Image Url
    @GetMapping("/{fileName}")
    public String getImageUrl(@PathVariable("fileName") String fileName){
        return imageUploader.getImageUrlByName(fileName);
    }


}
