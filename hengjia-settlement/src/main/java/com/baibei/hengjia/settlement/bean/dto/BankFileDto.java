package com.baibei.hengjia.settlement.bean.dto;

import lombok.Data;

/**
 * @Classname BankFileDto
 * @Description 银行文件处理dto
 * @Date 2019/6/25 11:44
 * @Created by Longer
 */
@Data
public class BankFileDto {

    /**
     * 功能标识：
     * 1：清算失败文件
     * 2：会员余额文件
     * 3：出入金流水文件
     * 4：会员开销户文件
     * 5：对账不平记录文件
     */
    private String FuncFlag;

    /**
     * 资金汇总账号
     */
    private String SupAcctId;

    /**
     * 文件名
     */
    private String FileName;

    /**
     * 文件密码
     */
    private String Reserve;

}
