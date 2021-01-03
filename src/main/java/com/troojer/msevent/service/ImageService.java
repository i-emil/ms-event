package com.troojer.msevent.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String getImageUrl();

    void addImage(MultipartFile file);

    void removeImage();

}
