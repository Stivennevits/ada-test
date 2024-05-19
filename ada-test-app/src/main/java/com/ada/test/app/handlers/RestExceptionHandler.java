package com.ada.test.app.handlers;

import com.ada.test.common.constants.ErrorMessages;
import com.ada.test.common.ex.AdaException;
import com.ada.test.common.ex.EntityAlreadyExistException;
import com.ada.test.common.ex.UnauthorisedException;
import com.ada.test.core.components.I18NComponent;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

import static com.ada.test.app.handlers.ErrorResponseHandlerMapper.buildResponseEntity;
import static org.springframework.http.HttpStatus.*;


@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final I18NComponent i18n;

    public RestExceptionHandler(I18NComponent i18n) {
        this.i18n = i18n;
    }

    @ExceptionHandler(AdaException.class)
    protected ResponseEntity<Object> handleAdaException(@NonNull AdaException ex) {
        log.error("RestExceptionHandler::handleAdaException --Error: [{}]", ex.getMessage());
        return buildResponseEntity(ex.getMessage(), BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnknownException(@NonNull Exception ex) {
        log.error("RestExceptionHandler::handleUnknownException --Error: [{}]", ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_UNKNOWN_ERROR), ex.getMessage(), INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(EntityAlreadyExistException.class)
    protected ResponseEntity<Object> handleEntityAlreadyExist(@NonNull EntityAlreadyExistException ex, WebRequest webRequest) {
        log.error("Inside RestExceptionHandler::handleEntityAlreadyExist User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(ex.getMessage(), BAD_REQUEST);
    }



    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(@NonNull AccessDeniedException ex, WebRequest webRequest) {
        log.error("Inside RestExceptionHandler::handleAccessDeniedException User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_UNKNOWN_ERROR), ex.getMessage(), UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorisedException.class)
    protected ResponseEntity<Object> handleUnauthorisedException(@NonNull UnauthorisedException ex, WebRequest webRequest) {
        log.error("Inside RestExceptionHandler::handleUnauthorisedException User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(ex.getMessage(), UNAUTHORIZED);
    }



    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        log.error("Inside RestExceptionHandler::handleHttpMediaTypeNotAcceptable User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_UNACCEPTABLE_MEDIA_TYPE, builder.substring(0, builder.length() - 2)), ex.getMessage(), UNSUPPORTED_MEDIA_TYPE);
    }

    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Inside RestExceptionHandler::handleHttpRequestMethodNotSupported User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_UNKNOWN_RESOURCE, request.getContextPath()), "", NOT_FOUND);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = i18n.getMessage(ErrorMessages.HANDLER_MISSING_PARAMETER, ex.getParameterName());
        log.error("Inside RestExceptionHandler::handleMissingServletRequestParameter User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(error, ex.getMessage(), BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(i18n.getMessage(ErrorMessages.HANDLER_UNKNOWN_MEDIA_TYPE));
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        log.error("Inside RestExceptionHandler::handleHttpMediaTypeNotSupported User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(builder.substring(0, builder.length() - 2), ex.getMessage(), UNSUPPORTED_MEDIA_TYPE);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Inside RestExceptionHandler::handleMethodArgumentNotValid User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.error("Inside RestExceptionHandler::handleConstraintViolation User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_VALIDATION_ERROR, ex.getMessage()), ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest webRequest) {
        log.error("Inside RestExceptionHandler::handleEntityNotFound User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(ex.getMessage(), NOT_FOUND);
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        log.error("Inside RestExceptionHandler::handleHttpMessageNotReadable User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(error, ex.getMessage(), BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        log.error("Inside RestExceptionHandler::handleHttpMessageNotWritable User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(error, ex.getMessage(), UNPROCESSABLE_ENTITY);
    }

    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Inside RestExceptionHandler::handleNoHandlerFoundException User [{}] Error: [{}]", Objects.requireNonNull(request.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_NO_HANDLER_ERROR, ex.getHttpMethod(), ex.getRequestURL()), ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest webRequest) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity("Database error", ex.getMessage(), CONFLICT);
        }
        log.error("Inside RestExceptionHandler::handleDataIntegrityViolation User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(ex.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest webRequest) {
        log.error("Inside RestExceptionHandler::handleMethodArgumentTypeMismatch User [{}] Error: [{}]", Objects.requireNonNull(webRequest.getUserPrincipal()).getName(), ex.getMessage());
        return buildResponseEntity(i18n.getMessage(ErrorMessages.HANDLER_ARGUMENT_TYPE_ERROR, ex.getName(), ex.getValue(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName()), ex.getMessage(), BAD_REQUEST);
    }

}