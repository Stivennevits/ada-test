package com.ada.test.common.constants;

public class ErrorMessages {

    private ErrorMessages(){throw new IllegalStateException("ErrorMessages");}

    public static final String HANDLER_UNKNOWN_ERROR = "error.unknown";
    public static final String HANDLER_UNAUTHORIZED_ERROR = "error.unauthorized";
    public static final String HANDLER_UNKNOWN_RESOURCE = "error.unknown.resource";
    public static final String HANDLER_NO_HANDLER_ERROR = "error.unknown.handler";
    public static final String HANDLER_UNKNOWN_MEDIA_TYPE = "error.unknown.media-type";
    public static final String HANDLER_MISSING_PARAMETER = "error.parameter.missing";
    public static final String HANDLER_VALIDATION_ERROR = "error.validation.fields";
    public static final String HANDLER_ARGUMENT_TYPE_ERROR = "error.argument.type";
    public static final String HANDLER_UNACCEPTABLE_MEDIA_TYPE = "error.unacceptable.media.type";
    public static final String COMPANY_NOT_FOUND_BY_ID = "error.company.not.found.by.id";
    public static final String COMPANY_NOT_FOUND_BY_CODE = "error.company.not.found.by.code";
    public static final String COMPANY_NOT_HAVE_ENTITIES = "error.company.not.have.entities";

    public static final String COMPANY_ALREADY_EXISTS = "error.company.already.exists";
    public static final String INVALID_SIZE_CODE = "error.invalid.size.code";
    public static final String INVALID_SIZE_NAME = "error.invalid.size.name";
    public static final String INVALID_SIZE_DESCRIPTION = "error.invalid.size.description";
    public static final String INVALID_SIZE_CREATED_BY = "error.invalid.size.created.by";
    public static final String COMPANY_IN_USE = "error.company.in.use";

    public static final String APPLICATION_NOT_FOUND_BY_ID = "error.application.not.found.by.id";
    public static final String APPLICATION_ALREADY_EXISTS = "error.application.already.exists";
    public static final String APPLICATION_IN_USE = "error.application.in.use";

    public static final String VERSION_NOT_FOUND_BY_ID = "error.version.not.found.by.id";
    public static final String VERSION_ALREADY_EXISTS = "error.version.already.exists";
    public static final String VERSION_IN_USE = "error.version.in.use";

    public static final String CV_NOT_FOUND_BY_ID = "error.cv.not.found.by.id";
    public static final String CV_ALREADY_EXISTS = "error.cv.already.exists";
    public static final String FILE_IS_EMPTY = "error.file.is.empty";
    public static final String ERROR_INVALID_FILE= "error.invalid.file";

    public static final String IMPOSSIBLE_DIVIDE_BY_ZERO= "error.impossible.divide.by.zero";
    public static final String EXPONENT_NEED_BE_POSITIVE= "error.exponent.need.be.positive";
    public static final String IMPOSSIBLE_CALCULATE_NEGATIVE_SQUARE_ROOT= "error.divide.calculate.negative.root";





}
