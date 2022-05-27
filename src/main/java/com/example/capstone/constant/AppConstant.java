package com.example.capstone.constant;

public class AppConstant {
    private AppConstant() {}

    public static final String DEFAULT_SYSTEM = "SYSTEM";
    public enum ResponseCode {

        SUCCESS("SUCCESS", "Success!"),
        DATA_NOT_FOUND("DATA_NOT_FOUND", "Data not found!"),
        UNKNOWN_ERROR("UNKNOWN_ERROR", "Happened unknown error!"),
        BAD_CREDENTIALS("BAD_CREDENTIALS", "Provided Credentials is bad!");

        private final String code;
        private final String message;

        private ResponseCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return this.code;
        }

        public String getMessage() {
            return this.message;
        }

    }
}
