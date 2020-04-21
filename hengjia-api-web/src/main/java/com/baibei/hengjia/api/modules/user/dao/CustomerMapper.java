package com.baibei.hengjia.api.modules.user.dao;

import com.baibei.hengjia.api.modules.user.bean.dto.InviteCodeDto;
import com.baibei.hengjia.api.modules.user.bean.vo.InvitationCodeCustomerVo;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    List<InvitationCodeCustomerVo> findByInviteCode(InviteCodeDto inviteCodeDto);

    List<Customer> selectByrecommenderIdAndDate(@Param("customerNo") String customerNo,@Param("date") String date);

    String getMemberCustomerNoByProductNo(String productTradeNo);

}