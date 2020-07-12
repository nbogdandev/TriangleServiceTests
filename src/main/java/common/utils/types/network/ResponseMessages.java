package common.utils.types.network;

public enum ResponseMessages {
    LIMIT_EXCEEDED("Limit exceeded"),
    CANNOT_PROCESS_INPUT("Cannot process input"),
    NOT_FOUND("Not Found"),
    NO_MESSAGE_AVAILABLE("No message available");

    private String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
