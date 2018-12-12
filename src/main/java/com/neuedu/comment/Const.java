package com.neuedu.comment;

public class Const {
    public static final Integer SUCCESS_CODE = 0;
    public static final Integer SUCCESS_ERROR = 1;


    public static final String CURRENTUSER = "CURRENTUSER";


    public static final String USERNAME="username";
    public static final String EMAIL ="email";
    public enum RoleEnum {
        ROLE_CUSTOMER(0,"普通用户"),
        ROLE_ADMIN(1,"管理员")
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
    public enum ProductStatusEnum{
        PRODUCT_ONLINE(1,"在售"),
        PRODUCT_OFFLINE(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;
        private int code;
        private String desc;
        private ProductStatusEnum(int code, String desc) {
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
    public enum CartCheckEnum{
        CART_CHECK(1,"已勾选"),
        CART_ON_CHECK(0,"未勾选")
        ;
        private int code;
        private String desc;
        private CartCheckEnum(int code, String desc) {
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
