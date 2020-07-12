package common.utils.types.network;

public enum ResponseExceptions {
    NOT_FOUND("com.natera.test.triangle.exception.NotFounException"), //TODO: Revise 'NotFound' typo after fix
    UNPROCESSABLE_DATA("com.natera.test.triangle.exception.UnprocessableDataException");

    private String exception;

    ResponseExceptions(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }
}
