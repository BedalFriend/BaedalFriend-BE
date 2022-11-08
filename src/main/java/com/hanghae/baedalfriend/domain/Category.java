package com.hanghae.baedalfriend.domain;

public enum Category {

    치킨(1),

    피자(2),

    햄버거(3),

    디저트(4);
    private int num;

    Category(int num) {
        this.num = num;
    }

    public static Category partsValue(int num) {
        switch (num) {
            case 1:
                return 치킨;
            case 2:
                return 피자;
            case 3:
                return 햄버거;
            case 4:
                return 디저트;
        }
        return null;
    }
}
