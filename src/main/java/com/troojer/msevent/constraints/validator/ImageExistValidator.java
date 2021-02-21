package com.troojer.msevent.constraints.validator;

import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.constraints.ImageExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageExistValidator
        implements ConstraintValidator<ImageExist, String> {

    private final ImageClient imageClient;

    public ImageExistValidator(ImageClient imageClient) {
        this.imageClient = imageClient;
    }

    @Override
    public boolean isValid(String imageId, ConstraintValidatorContext context) {
        return (!imageId.isBlank() && imageClient.isImageExist(imageId));
    }
}
