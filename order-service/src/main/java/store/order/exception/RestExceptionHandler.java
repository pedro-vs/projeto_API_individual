package store.order.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFound(OrderNotFoundException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, "Order not found", exception.getMessage(), request);
    }

    @ExceptionHandler(InvalidOrderProductException.class)
    public ProblemDetail handleInvalidOrderProduct(InvalidOrderProductException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, "Invalid order product", exception.getMessage(), request);
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ProblemDetail handleUnsupportedCurrency(UnsupportedCurrencyException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Unsupported currency", exception.getMessage(), request);
    }

    @ExceptionHandler(MissingAccountHeaderException.class)
    public ProblemDetail handleMissingAccountHeader(MissingAccountHeaderException exception, HttpServletRequest request) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Unauthorized request", exception.getMessage(), request);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ProblemDetail handleMissingRequestHeader(MissingRequestHeaderException exception, HttpServletRequest request) {
        HttpStatus status = "id-account".equals(exception.getHeaderName()) ? HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;
        String title = "id-account".equals(exception.getHeaderName()) ? "Unauthorized request" : "Missing request header";
        return buildProblemDetail(status, title, exception.getMessage(), request);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ProblemDetail handleExternalService(ExternalServiceException exception, HttpServletRequest request) {
        String title = exception.getServiceName() + " unavailable";
        return buildProblemDetail(HttpStatus.BAD_GATEWAY, title, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "request validation failed");
        problemDetail.setTitle("Invalid request body");
        problemDetail.setProperty("path", request.getRequestURI());
        problemDetail.setProperty("errors", buildFieldErrors(exception));
        return problemDetail;
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("path", request.getRequestURI());
        return problemDetail;
    }

    private Map<String, String> buildFieldErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
            .forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return errors;
    }
}
