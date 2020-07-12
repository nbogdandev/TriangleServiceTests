package common.utils.types.network;

public enum ResponseCodes {
    OK(200),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    UNPROCESSIBLE(422);

    private Integer code;

    ResponseCodes(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
