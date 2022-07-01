package com.example.capstone.constant;



public class AppConstant {
    private AppConstant() {}

    public static final String DATE_JSON_FORMAT = "dd-MM-yyyy";
    public static final String DATETIME_JSON_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String DEFAULT_SYSTEM = "SYSTEM";
    public enum ResponseCode {

        SUCCESS("SUCCESS", "Success!"),
        DATA_NOT_FOUND("DATA_NOT_FOUND", "Data not found!"),
        UNKNOWN_ERROR("UNKNOWN_ERROR", "Happened unknown error!"),
        BAD_CREDENTIALS("BAD_CREDENTIALS", "Provided Credentials is bad!"),
        NOT_LOGGED_IN("NOT_LOGGED_IN", "Login first to access this endpoint"),
        USER_EXIST("USER_EXIST","User Already Exist"),
        PASSWORD_INCORRECT("PASSWORD_INCORRECT","Password is incorrect"),
        NOT_ENROLL("NOT_ENROLL","Enroll first to access this endpoint");

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

    public enum RoleType{
        ROLE_USER,
        ROLE_ADMIN
    }

    public enum Level {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public enum Gender {
        MALE, FEMALE
    }



}
