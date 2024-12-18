package com.ImageUploader.ImageUploader.execptions;


import com.amazonaws.services.wafv2.model.CustomResponse;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


//Global Exception Handler
@ControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler
    public ResponseEntity<CustomResponse> handleUploadException(ImageUploadException imageUploadException){
        CustomResponse customResponse = new CustomResponse();
        customResponse.setResponseCode(500);
        customResponse.setCustomResponseBodyKey("ErrorKey");

        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(customResponse);


    }

}
