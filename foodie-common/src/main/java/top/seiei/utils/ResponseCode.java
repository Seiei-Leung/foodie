package top.seiei.utils;

public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    NEED_LOGIN(10, "NEED_LOGIN");

    public int getCode() {
        return code;
    }
    public String getDec() {
        return dec;
    }
    ResponseCode(int code, String dec) {
        this.code = code;
        this.dec = dec;
    }

    private final int code;
    private final String dec;
}
