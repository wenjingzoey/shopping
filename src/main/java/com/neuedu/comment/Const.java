package com.neuedu.comment;

public class Const {
    public static final Integer SUCCESS_CODE = 0;
    public static final Integer SUCCESS_ERROR = 1;


    public static final String CURRENTUSER = "CURRENTUSER";


    public static final String USERNAME="username";
    public static final String EMAIL ="email";
    public enum RoleEnum {
        ROLE_ADMIN(0,"普通用户"),
        ROLE_CUSTOMER(1,"管理员")
        ;
        private int code;
        private String desc;

       private RoleEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
