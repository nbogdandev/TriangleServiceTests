package common.utils.types.network;

public enum ResponseErrors {
    NOT_FOUND("Not Found"),
    UNPROCESSABLE_ENTITY("Unprocessable Entity"),
    UNAUTHORIZED("Unauthorized");

    private String error;

    ResponseErrors(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
