package com.baibei.hengjia.api.modules.account.service;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDetailDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ThawCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountDetailVo;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/08/05 10:07:22
* @description: CouponAccount服务接口
*/
public interface ICouponAccountService extends Service<CouponAccount> {
    /**
     * 修改券可用余额
     * @param changeCouponAccountDto
     * @return
     */
    ApiResult changeAmount(ChangeCouponAccountDto changeCouponAccountDto);


    /**
     * 修改券可用余额（扣减允许穿仓）
     * @param changeCouponAccountDto
     * @return
     */
    ApiResult changeAmountSpecil(ChangeCouponAccountDto changeCouponAccountDto);


    /**
     * 修改券冻结余额
     * @param changeCouponAccountDto
     * @return
     */
    ApiResult changeFrozenAmount(ChangeCouponAccountDto changeCouponAccountDto);
    /**
     * 修改券解冻余额
     * @param changeCouponAccountDto
     * @return
     */
    ApiResult changeThawAmount(ChangeCouponAccountDto changeCouponAccountDto);
    /**
     * 通过用户编号获取券的券总额
     * @param customerNo
     * @return
     */
    ApiResult<BigDecimal> getByCustomerNo(String customerNo,String productTradeNo,String couponType);

//    /**
//     *
//     */
//    ApiResult<CouponAccount> getByCustomerNoAndProductNo(String );

    /**
     * 查询用户券余额
     * @param couponAccountDto
     * @return
     */
    MyPageInfo<CouponAccountVo> myList(CouponAccountDto couponAccountDto);

    CouponAccount getCouponAccountById(Long couponAccountId);

    /**
     * 获取指定某个券余额
     * @param customerNo
     * @param productTradeNo
     * @param couponType 券类型
     * @return
     */
    CouponAccount getCouponAccount(String customerNo,String productTradeNo,String couponType);

    /**
     *
     * @param thawCouponAccountDto
     * @return
     */
    ApiResult thawCouponAccount(ThawCouponAccountDto thawCouponAccountDto);

    /**
     * 根据券类型查询券的各个额度
     * @param couponAccountDetailDto
     * @return
     */
    CouponAccountDetailVo getCouponAccountDetail(CouponAccountDetailDto couponAccountDetailDto);

    /**
     * 根据用户编号、商品交易编号、券类型找到所有余额大于特定值的用户
     * @param balance 特定值
     * @param customerNo 用户编号
     * @param productTradeNo 商品交易编号
     * @param couponType 券类型（vouchers:兑换券 deliveryTicket:提货券）
     * @return
     */
    ApiResult<List<CouponAccount>> findByBalance(BigDecimal balance,String customerNo,String productTradeNo,String couponType);
}
