package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("404 {}", e.getMessage());
        return new ErrorResponse("404 NotFoundException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {
        log.error("400 {}", e.getMessage());
        return new ErrorResponse("400 MethodArgumentNotValidException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExist(final AlreadyExistException e) {
        log.error("409 {}", e.getMessage());
        return new ErrorResponse("409 AlreadyExistException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExist(final EmailConflictException e) {
        log.error("409 {}", e.getMessage());
        return new ErrorResponse("409 EmailConflictException", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleExceptions (final InternalError e) {
        log.info("500 {}", e.getMessage());
        return new ErrorResponse("500 InternalError", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherExceptions(final Throwable throwable) {
        log.info("500 {}", throwable.getMessage());
        return new ErrorResponse("500 Error", throwable.getMessage());
    }
}
