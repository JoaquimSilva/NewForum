package com.silva.joaquim.forum.config.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class Validations {

    @Autowired
    private MessageSource msgSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrosFormularioDto> handle(MethodArgumentNotValidException exception){

        List<ErrosFormularioDto> dtos = new ArrayList<>();
        List<FieldError> fieldErrorList =  exception.getBindingResult().getFieldErrors();

        fieldErrorList.forEach(fieldError ->
        {String msg = msgSource.getMessage(fieldError, LocaleContextHolder.getLocale());
        ErrosFormularioDto erro =  new ErrosFormularioDto(fieldError.getField(),msg);
        dtos.add(erro);
        });

        return dtos;
    }

}
