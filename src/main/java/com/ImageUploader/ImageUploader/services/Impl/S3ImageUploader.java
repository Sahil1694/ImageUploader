package com.ImageUploader.ImageUploader.services.Impl;

import com.ImageUploader.ImageUploader.execptions.ImageUploadException;
import com.ImageUploader.ImageUploader.services.ImageUploader;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


//S3 Image Uploader Whole Logic.
@Service
public class S3ImageUploader implements ImageUploader {

    @Autowired
    private AmazonS3 client;

    @Value("${aws.s3.bucket}")
    private  String bucketName;

    @Override
    public String uploadImage(MultipartFile image) {

        if(image == null){
            throw new ImageUploadException("Image is null");
        }
       String actualFileName = image.getOriginalFilename();
       String fileName =  UUID.randomUUID().toString()+actualFileName.substring(actualFileName.lastIndexOf("."));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName , fileName, image.getInputStream(), metadata));
            return this.preSignUrl(fileName);
        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image " + e.getMessage());
        }
    }


    @Override
    public List<String> allFiles() {
        ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName);

        ListObjectsV2Result listObjectsV2Result =  client.listObjectsV2(listObjectsV2Request);

        List<S3ObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();

        List<String> listofFilesUrls = objectSummaries.stream().map(item-> this.preSignUrl(item.getKey())).collect(Collectors.toList());

        return listofFilesUrls;
    }

    @Override
    public String preSignUrl(String filename) {

        Date expirationDate = new Date();
        long time = expirationDate.getTime();
        int hours = 2;
        time = time + hours * 3600 * 1000;
        expirationDate.setTime(time);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filename)
                .withMethod(HttpMethod.GET)
                .withExpiration(expirationDate);
        URL url =  client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    @Override
    public String getImageUrlByName(String filename) {
        S3Object object = client.getObject(bucketName, filename);
        String key = object.getKey();
         String url =   preSignUrl(key);
         return url;
    }
}
