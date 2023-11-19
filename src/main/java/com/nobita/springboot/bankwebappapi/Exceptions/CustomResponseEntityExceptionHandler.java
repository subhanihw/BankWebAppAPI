package com.nobita.springboot.bankwebappapi.Exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private final Map<Class<? extends Exception>, String> exceptionGuidMap = new HashMap<>();

    public CustomResponseEntityExceptionHandler() {
        exceptionGuidMap.put(CustomerNotFoundException.class, "d590aeff-93c8-4e34-9a2f-8b0e98f38c29");
        exceptionGuidMap.put(AccountNotFoundException.class, "23ef3a9d-8833-4ba1-bec4-1c5e06201309");
        exceptionGuidMap.put(InvalidAccountTypeException.class, "44b95e76-952f-4444-8411-1bd0e981e492");
        exceptionGuidMap.put(AccountTypeAlreadyExistsException.class,"ba409f8b-3c67-43f5-8ade-4ca0b83bdfc4");
        exceptionGuidMap.put(CustomerAlreadyExists.class,"82a6f2d8-d1b0-4688-b2e5-bb8d480aebad");
        exceptionGuidMap.put(InsufficientBalanceException.class, "3a38bc0a-5889-450e-84e9-9be5b74f46f6");
        exceptionGuidMap.put(InvalidTransactionType.class, "e97bb121-2bdd-464c-a953-9e56cef45e71");
        exceptionGuidMap.put(MinBalanceException.class, "1822eb71-74f1-48b3-8dd1-b5fdb6ba4cb4");
        exceptionGuidMap.put(NoTransactionsException.class, "c7362ac7-54dd-4db7-9306-357b4ad7a031");
        exceptionGuidMap.put(RequiresPanException.class, "83abd3cc-e326-49d2-82b3-459344bac28c");
        exceptionGuidMap.put(InvalidFieldException.class, "6f72ba58-5fcc-41b1-8a1c-5a35a5bbbabd");
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) throws Exception {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler({InvalidFieldException.class, InsufficientBalanceException.class})
//    public final ResponseEntity<Object> handleInvalidField(Exception ex, WebRequest request) throws Exception {
//        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
//        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler({AccountNotFoundException.class,
                    AccountTypeAlreadyExistsException.class,
                    InvalidAccountTypeException.class,
                    CustomerNotFoundException.class,
                    CustomerAlreadyExists.class,
                    InsufficientBalanceException.class,
                    InvalidFieldException.class,
                    InvalidTransactionType.class,
                    MinBalanceException.class,
                    NoTransactionsException.class,
                    RequiresPanException.class
                    })
    public final ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) throws Exception
    {
        String guid = getGuidForException(ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "Bad Request. Please Contact administrator. Reference ID: "+guid,
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getFieldError().getDefaultMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private String getGuidForException(Exception ex) {
        return exceptionGuidMap.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(ex))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseGet(this::generateGuid);
    }

    private String generateGuid() {
        return UUID.randomUUID().toString();
    }
}

