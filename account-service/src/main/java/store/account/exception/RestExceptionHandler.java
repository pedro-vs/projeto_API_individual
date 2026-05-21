package store.account.exception;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFound(AccountNotFoundException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, "Account not found", exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicateAccountEmailException.class)
    public ProblemDetail handleDuplicateEmail(DuplicateAccountEmailException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.CONFLICT, "Account email already exists", exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Invalid credentials", exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail detail = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Invalid request body",
            "request validation failed",
            request
        );

        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult()
            .getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        detail.setProperty("errors", errors);
        return detail;
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setProperty("path", request.getRequestURI());
        return problemDetail;
    }
}
