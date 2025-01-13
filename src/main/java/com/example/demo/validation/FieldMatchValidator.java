package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFileName;
    private String secondFileName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFileName = constraintAnnotation.first();
        secondFileName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object field = new BeanWrapperImpl(value).getPropertyValue(this.firstFileName);
        Object fieldMatch = new BeanWrapperImpl(value).getPropertyValue(this.secondFileName);
        return Objects.equals(field, fieldMatch);
    }
}
