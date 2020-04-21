package com.baibei.hengjia.api.modules.cash.enumeration;

public enum CashFunctionType {

    SIGNING(1303, "签约"),

    DEPOSIT(1310, "入金"),

    SIGN_IN_BACK(1330, "签到签退"),

    DEPOSIT_WITHDRAW_MEMBERS_DETAIL(1006, "入金流水对账及会员开销户流水匹配"),

    VIEW_FILE(1005, "通知交易网查看文件"),

    FIND_FILE_PLANNED_SPEED(1004, "查询银行清算与对账文件的进度"),

    BEGIN_CLEAN(1003, "交易网触发银行进行清算对账");


    CashFunctionType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    // 功能标识
    public int index;

    public String name;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
