package common.utils.types.network;

public enum ResponseFields {
    TIMESTAMP("timestamp"),
    STATUS("status"),
    ERROR("error"),
    EXCEPTION("exception"),
    MESSAGE("message"),
    PATH("path");

    private String field;

    ResponseFields(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
