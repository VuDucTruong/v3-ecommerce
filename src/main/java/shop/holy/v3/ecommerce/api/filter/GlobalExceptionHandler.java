package shop.holy.v3.ecommerce.api.filter;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import shop.holy.v3.ecommerce.api.dto.FieldErrorResponse;
import shop.holy.v3.ecommerce.api.dto.ResponseError;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.ForbiddenException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Hidden
public class GlobalExceptionHandler {


    private static final String COMMON_ERROR_MESSAGE_TEMPLATE = "Got error: [%s], with Message: [%s]";

    private String buildErrorMessage(Exception ex) {
        return String.format(COMMON_ERROR_MESSAGE_TEMPLATE, ex.getClass().getName(), ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError<?> handleUnCaughtException(Exception ex, WebRequest webRequest) {
        log.error(buildErrorMessage(ex));
        ex.printStackTrace();
        return new ResponseError<>(webRequest.getContextPath(), 100, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<List<FieldErrorResponse>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> new FieldErrorResponse(
                        e.getField(),
                        e.getRejectedValue() != null ? e.getRejectedValue().toString() : null,
                        e.getDefaultMessage()))
                .toList();

        return new ResponseError<>(request.getRequestURI(), 400, "Validation failed", errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<List<FieldErrorResponse>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {

        var errors = new ArrayList<FieldErrorResponse>();

        for (var error : ex.getAllErrors()) {
            if (error instanceof FieldError fe) {
                errors.add(new FieldErrorResponse(
                        fe.getField(),
                        fe.getRejectedValue() != null ? fe.getRejectedValue().toString() : null,
                        fe.getDefaultMessage()));
            } else {
                // handle ObjectError or other subclasses: include objectName as "field"
                errors.add(new FieldErrorResponse(
                        Arrays.stream(Objects.requireNonNull(error.getArguments())).map(e->e.getClass().getName()).collect(Collectors.joining(",\n")),
                        null,
                        error.getDefaultMessage()));
            }
        }

        return new ResponseError<>(request.getRequestURI(), 400, "Validation failed", errors);
    }



    ///  DONT CARE THESE
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError<?> handleSchedulerException(BadRequestException ex, HttpServletRequest webRequest) {
        return new ResponseError<>(webRequest.getContextPath(), 2, ex.getMessage(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError<?> handleResourceAccessException(ResourceNotFoundException ex, HttpServletRequest webRequest) {
        return new ResponseError<>(webRequest.getContextPath(), 11, ex.getMessage(), null);
    }

    @ExceptionHandler(UnAuthorisedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError<?> handleUnAuthorisedException(UnAuthorisedException ex, HttpServletRequest webRequest) {

        return new ResponseError<>(webRequest.getContextPath(), 12, ex.getMessage(), null);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseError<?> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest webRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseError<>(webRequest.getContextPath(), 13, BizErrors.AUTHORISATION_INVALID.exception().getMessage(), null);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError<?> handleForbiddenException(ForbiddenException ex, HttpServletRequest webRequest) {
        return new ResponseError<>(webRequest.getContextPath(), 12, ex.getMessage(), null);
    }
}
