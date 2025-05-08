package shop.holy.v3.ecommerce.api.filter;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import shop.holy.v3.ecommerce.api.dto.FieldErrorResponse;
import shop.holy.v3.ecommerce.api.dto.ResponseError;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.ForbiddenException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;

import java.util.Arrays;
import java.util.List;
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
    public ResponseError<List<FieldErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
//        log.error(buildErrorMessage(ex));

        List<FieldErrorResponse> errorDTOs = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorResponse(error.getField(),
                        String.valueOf(error.getRejectedValue()))
                ).toList();
        String overallReason = Arrays.stream(ex.getDetailMessageArguments())
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return new ResponseError<>(request.getContextPath(), 1, overallReason, errorDTOs);
    }

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

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError<?> handleForbiddenException(ForbiddenException ex, HttpServletRequest webRequest) {
        return new ResponseError<>(webRequest.getContextPath(), 12, ex.getMessage(), null);
    }
}
