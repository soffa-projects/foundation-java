package io.soffa.foundation.errors;

import com.mgnt.utils.TextUtils;
import io.soffa.foundation.commons.HttpStatus;
import io.soffa.foundation.commons.TextUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ErrorUtil {

    private static final String ALL_PACKAGES = "*";
    private static String defaultErrorPackage = "*";
    private static final Map<Class<?>, Integer> MAPPED_STATUS = new LinkedHashMap<>();

    private ErrorUtil() {
    }

    public static void setRelevantPackage(String pkg) {
        defaultErrorPackage = pkg;
        if (!ALL_PACKAGES.equals(pkg)) {
            TextUtils.setRelevantPackage(pkg);
        }
    }

    public static String getStacktrace(Throwable e) {
        if ("*".equals(defaultErrorPackage)) {
            return TextUtils.getStacktrace(e, true);
        }
        return TextUtils.getStacktrace(e, true, defaultErrorPackage);
    }

    public static Throwable unwrap(Throwable error) {
        if (error instanceof InvocationTargetException && error.getCause() != null) {
            return unwrap(error.getCause());
        }
        if (error instanceof UndeclaredThrowableException && error.getCause() != null) {
            return unwrap(error.getCause());
        }
        if (error instanceof RuntimeException && error.getCause() != null) {
            return unwrap(error.getCause());
        }
        return error;
    }

    public static String loookupOriginalMessage(Throwable error) {
        return loookupOriginalMessage(error, null);
    }

    public static String loookupOriginalMessage(Throwable error, Class<?> lookup) {
        if (error == null) {
            return "Unknown error";
        }
        if (lookup!=null && lookup.isInstance(error)) {
            if (TextUtil.isEmpty(error.getMessage())) {
                return loookupOriginalMessage(error.getCause(), lookup);
            }
            return error.getMessage();
        }
        if (error.getCause() != null) {
            return loookupOriginalMessage(error.getCause(), lookup);
        }
        if (TextUtil.isEmpty(error.getMessage())) {
            return loookupOriginalMessage(error.getCause(), lookup);
        }
        return error.getMessage();
    }

    public static Exception getException(int errorCode, String message) {
        switch (errorCode) {
            case HttpStatus.BAD_REQUEST:
                return new FunctionalException(message);
            case HttpStatus.CONFLICT:
                return new ConflictException(message);
            case HttpStatus.FORBIDDEN:
                return new ForbiddenException(message);
            case HttpStatus.UNAUTHORIZED:
                return new UnauthorizedException(message);
            case HttpStatus.REQUEST_TIMEOUT:
                return new TimeoutException(message);
            default:
                return new TechnicalException(message);
        }
    }

    public static int resolveErrorCode(Throwable e) {
        if (e == null) {
            return -1;
        }
        for (Map.Entry<Class<?>, Integer> entry : MAPPED_STATUS.entrySet()) {
            if (entry.getKey().isAssignableFrom(e.getClass())) {
                return entry.getValue();
            }
        }
        return -1;
    }

    static {
        MAPPED_STATUS.put(DatabaseException.class, HttpStatus.SERVER_ERROR);
        MAPPED_STATUS.put(ConfigurationException.class, HttpStatus.SERVER_ERROR);
        MAPPED_STATUS.put(RequirementException.class, HttpStatus.EXPECTATION_FAILED);
        MAPPED_STATUS.put(NotImplementedException.class, HttpStatus.NOT_IMLEMENTED);
        MAPPED_STATUS.put(InvalidTenantException.class, HttpStatus.BAD_REQUEST);
        MAPPED_STATUS.put(ValidationException.class, HttpStatus.BAD_REQUEST);
        MAPPED_STATUS.put(ConflictException.class, HttpStatus.CONFLICT);
        MAPPED_STATUS.put(ForbiddenException.class, HttpStatus.FORBIDDEN);
        MAPPED_STATUS.put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
        MAPPED_STATUS.put(InvalidTokenException.class, HttpStatus.UNAUTHORIZED);
        MAPPED_STATUS.put(InvalidAuthException.class, HttpStatus.UNAUTHORIZED);
        MAPPED_STATUS.put(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
        MAPPED_STATUS.put(NoContentException.class, HttpStatus.NO_CONTENT);
        MAPPED_STATUS.put(TodoException.class, HttpStatus.NOT_IMLEMENTED);
        MAPPED_STATUS.put(SocketException.class, HttpStatus.REQUEST_TIMEOUT );
        MAPPED_STATUS.put(TimeoutException.class, HttpStatus.REQUEST_TIMEOUT);
        MAPPED_STATUS.put(TechnicalException.class, HttpStatus.SERVER_ERROR);
        MAPPED_STATUS.put(FunctionalException.class, HttpStatus.BAD_REQUEST);
    }
}
