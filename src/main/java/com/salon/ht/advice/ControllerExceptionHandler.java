package com.salon.ht.advice;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.salon.ht.entity.payload.ApiResponse;
import com.salon.ht.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse processValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<ObjectError> allErrors = result.getAllErrors();
        String data = allErrors.stream().map(this::resolveLocalizedErrorMessage).collect(Collectors.joining("\n"));
        return new ApiResponse(false, data, ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse processIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    private String resolvePathFromWebRequest(WebRequest request) {
        try {
            return ((ServletWebRequest) request).getRequest().getAttribute("javax.servlet.forward.request_uri").toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private String resolvePathFromRequest(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    /**
     * Resolve localized error message. Utility method to generate a localized error
     * message
     *
     * @param objectError the field error
     * @return the string
     */
    private String resolveLocalizedErrorMessage(ObjectError objectError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(objectError, currentLocale);
        LOGGER.info(localizedErrorMessage);
        return localizedErrorMessage;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse handleAppException(AppException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse handleResourceAlreadyInUseException(ResourceAlreadyInUseException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponse handleBadRequestException(BadRequestException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiResponse handleUserNameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return new ApiResponse(false, "Tài khoản hoặc mật khẩu không chính xác. Hãy nhập lại", ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UserRegistrationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = BookingException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleBookingException(BookingException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = InvalidTokenRequestException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse handleInvalidTokenException(InvalidTokenRequestException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UserLogoutException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ResponseBody
    public ApiResponse handleUserLogoutException(UserLogoutException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = BookingUseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse handleBookingUseException(BookingUseException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = LockedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponse handleLockedException(LockedException ex, WebRequest request) {
        return new ApiResponse(false, "Tài khoản đã bị khóa. Vui lòng liên hệ admin để mở lại", ex.getClass().getName(), resolvePathFromWebRequest(request));
    }

    @ExceptionHandler(value = UploadFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse handleUploadFileException(UploadFileException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = JWTDecodeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ApiResponse handleJWTDecodeException(JWTDecodeException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = ImportUserException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ApiResponse handleImportUserException(ImportUserException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    @ExceptionHandler(value = DepartmentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponse handleDeleteDepartmentException(DepartmentException ex, WebRequest request) {
        return new ApiResponse(false, ex.getMessage(), ex.getClass().getName(), resolvePathFromRequest(request));
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

}
