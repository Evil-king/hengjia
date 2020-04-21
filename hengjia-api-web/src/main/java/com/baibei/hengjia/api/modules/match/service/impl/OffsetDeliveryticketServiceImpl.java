package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.bean.dto.ComsumeAuthorityDto;
import com.baibei.hengjia.api.modules.match.dao.OffsetDeliveryticketMapper;
import com.baibei.hengjia.api.modules.match.dao.OffsetDeliveryticketRecordMapper;
import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.api.modules.match.model.OffsetDeliveryticket;
import com.baibei.hengjia.api.modules.match.model.OffsetDeliveryticketRecord;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.api.modules.match.service.IOffsetDeliveryticketRecordService;
import com.baibei.hengjia.api.modules.match.service.IOffsetDeliveryticketService;
import com.baibei.hengjia.api.modules.settlement.model.CleanHelp;
import com.baibei.hengjia.api.modules.settlement.service.ICleanHelpService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import com.baibei.hengjia.common.tool.utils.NoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/10/10 09:47:51
* @description: OffsetDeliveryticket服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OffsetDeliveryticketServiceImpl extends AbstractService<OffsetDeliveryticket> implements IOffsetDeliveryticketService {

    @Autowired
    private IOffsetDeliveryticketRecordService offsetDeliveryticketRecordService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private ICouponAccountService couponAccountService;
    @Autowired
    private OffsetDeliveryticketMapper offsetDeliveryticketMapper;
    @Autowired
    private OffsetDeliveryticketRecordMapper offsetDeliveryticketRecordMapper;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IMatchAuthorityService matchAuthorityService;
    @Autowired
    private ICleanHelpService cleanHelpService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Value("${trade.coupon.amount}")
    private String tradeCouponAmount;

    @Override
    public List<OffsetDeliveryticket> getInfosWithAuthority() {
        Condition condition = new Condition(OffsetDeliveryticket.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andGreaterThan("remainAuthority",0);
        List<OffsetDeliveryticket> offsetDeliverytickets = offsetDeliveryticketMapper.selectByCondition(condition);
        return offsetDeliverytickets;
    }

    @Override
    public ApiResult comsumeAuthority(ComsumeAuthorityDto comsumeAuthorityDto) {
        if (StringUtils.isEmpty(comsumeAuthorityDto.getCustomerNo())) {
            return ApiResult.error("用户编码不能为空");
        }
        if (StringUtils.isEmpty(comsumeAuthorityDto.getProductTradeNo())) {
            return ApiResult.error("商品交易编码不能为空");
        }
        if (StringUtils.isEmpty(comsumeAuthorityDto.getChangeAmount())||comsumeAuthorityDto.getChangeAmount()==0) {
            return ApiResult.error("消费数量不能为空");
        }
        if (StringUtils.isEmpty(comsumeAuthorityDto.getOrderNo())) {
            return ApiResult.error("订单号不能为空");
        }
        //查看用户提货券补扣信息
        OffsetDeliveryticket offsetDeliveryticket = this.getOne(comsumeAuthorityDto.getCustomerNo(), comsumeAuthorityDto.getProductTradeNo());
        if(StringUtils.isEmpty(offsetDeliveryticket)){
            return ApiResult.error("为找到指定用户的配货权,用户编码为："+comsumeAuthorityDto.getCustomerNo());
        }
        if (offsetDeliveryticket.getRemainAuthority()==0) {
            return ApiResult.error("用户可消费配货权为0，无需再消费，用户编码为："+comsumeAuthorityDto.getCustomerNo());
        }

        //修改配货权余额
        Condition condition = new Condition(OffsetDeliveryticket.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("id",offsetDeliveryticket.getId());
        criteria.andEqualTo("orignAuthority",offsetDeliveryticket.getOrignAuthority());
        criteria.andEqualTo("remainAuthority",offsetDeliveryticket.getRemainAuthority());
        criteria.andEqualTo("comsumedAuthority",offsetDeliveryticket.getComsumedAuthority());
        OffsetDeliveryticket odt = new OffsetDeliveryticket();
        odt.setRemainAuthority(offsetDeliveryticket.getRemainAuthority()-comsumeAuthorityDto.getChangeAmount());
        odt.setComsumedAuthority(offsetDeliveryticket.getComsumedAuthority()+comsumeAuthorityDto.getChangeAmount());
        odt.setModifyTime(new Date());
        int i = offsetDeliveryticketMapper.updateByConditionSelective(odt, condition);
        if(i==0){
            return ApiResult.error("消费配货权失败，乐观锁问题，用户编码为："+comsumeAuthorityDto.getCustomerNo());
        }
        //记录流水
        OffsetDeliveryticketRecord offsetDeliveryticketRecord = new OffsetDeliveryticketRecord();
        offsetDeliveryticketRecord.setRecordNo(IdWorker.get32UUID());
        offsetDeliveryticketRecord.setOrderNo(comsumeAuthorityDto.getOrderNo());
        offsetDeliveryticketRecord.setCustomerNo(comsumeAuthorityDto.getCustomerNo());
        offsetDeliveryticketRecord.setProductTradeNo(comsumeAuthorityDto.getProductTradeNo());
        offsetDeliveryticketRecord.setAuthority(offsetDeliveryticket.getRemainAuthority());
        offsetDeliveryticketRecord.setChangeAmount(comsumeAuthorityDto.getChangeAmount());
        offsetDeliveryticketRecord.setRemainAmount(offsetDeliveryticket.getRemainAuthority()
                - comsumeAuthorityDto.getChangeAmount());
        if (!StringUtils.isEmpty(comsumeAuthorityDto.getRemark())) {
            offsetDeliveryticketRecord.setRemark(comsumeAuthorityDto.getRemark());
        }
        offsetDeliveryticketRecord.setCreateTime(new Date());
        offsetDeliveryticketRecord.setModifyTime(new Date());
        offsetDeliveryticketRecordMapper.insertSelective(offsetDeliveryticketRecord);
        return ApiResult.success();
    }

    @Override
    public OffsetDeliveryticket getOne(String customerNo, String productTradeNo) {
        Condition condition = new Condition(OffsetDeliveryticket.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        List<OffsetDeliveryticket> offsetDeliverytickets = offsetDeliveryticketMapper.selectByCondition(condition);
        if(offsetDeliverytickets.size()>1){
            throw new ServiceException("OffsetDeliveryticket should select one but more");
        }
        return offsetDeliverytickets.size()>0?offsetDeliverytickets.get(0):null;
    }

    /**
     * 提货券补扣,主要流程：扣减用户余额-->增加挂牌商余额--->增加用户提货券余额-->消费用户配货权
     * @param customerNo
     * @param productTradeNo
     * @return
     */
    @Override
    public ApiResult offset(String customerNo, String productTradeNo,String validTradeDayFlag) {
        log.info("开始补扣，用户编码为："+customerNo);
        if (Constants.ValidTradeFlag.VALID.equals(validTradeDayFlag)) {
            boolean tradeDay = tradeDayService.isTradeDay(new Date());
            if(!tradeDay){
                return ApiResult.error("非交易日，不能补扣");
            }
        }
        if (StringUtils.isEmpty(customerNo)) {
            return ApiResult.error("未指定用户");
        }
        if (StringUtils.isEmpty(productTradeNo)) {
            return ApiResult.error("未指定商品交易编码");
        }
        String orderNo = NoUtil.getBuyMatchNo();
        //查看用户提货券补扣信息
        OffsetDeliveryticket offsetDeliveryticket = this.getOne(customerNo, productTradeNo);
        if(StringUtils.isEmpty(offsetDeliveryticket)){
            return ApiResult.error("为找到指定用户的配货权,用户编码为："+customerNo);
        }
        if (offsetDeliveryticket.getRemainAuthority()==0) {
            return ApiResult.error("用户可消费配货权为0，无需再消费，用户编码为："+customerNo);
        }
        //查看用户余额
        Account account = accountService.checkAccount(customerNo);
        if (StringUtils.isEmpty(account)) {
            return ApiResult.error("查询不到用户账户信息，用户编码为："+customerNo);
        }
        if(account.getBalance().compareTo(new BigDecimal(tradeCouponAmount))<0){
            return ApiResult.error("用户余额不足，用户编码为："+customerNo);
        }
        //计算用户目前的资金余额可消费多少个配货权
        int canComsumeAuthority = account.getBalance().divide(new BigDecimal(tradeCouponAmount),2,BigDecimal.ROUND_DOWN).intValue();
        if(canComsumeAuthority>=offsetDeliveryticket.getRemainAuthority()){
            canComsumeAuthority=offsetDeliveryticket.getRemainAuthority();
        }
        log.info("原配货权数："+offsetDeliveryticket.getRemainAuthority()+"，应消费的配货权数为："+canComsumeAuthority);
        //扣减用户余额
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(customerNo);
        BigDecimal changeAmount = new BigDecimal(canComsumeAuthority).multiply(new BigDecimal(tradeCouponAmount));
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setOrderNo(orderNo);
        changeAmountDto.setTradeType(FundTradeTypeEnum.OFFSET_DELIVERYTICKET_OUT.getCode());
        changeAmountDto.setReType(new Byte("1"));
        accountService.changeAccount(changeAmountDto);

        //查询挂牌商编码
        String memberCustomerNo = customerService.getMemberCustomerNoByProductNo(productTradeNo);
        if (StringUtils.isEmpty(memberCustomerNo)) {
            return ApiResult.error("查询不到指定挂牌商，商品交易编码为："+productTradeNo);
        }
        //增加挂牌商余额
        ChangeAmountDto memberChangeAmountDto = new ChangeAmountDto();
        memberChangeAmountDto.setCustomerNo(memberCustomerNo);
        BigDecimal memberChangeAmount = new BigDecimal(canComsumeAuthority).multiply(new BigDecimal(tradeCouponAmount));
        memberChangeAmountDto.setChangeAmount(changeAmount);
        memberChangeAmountDto.setOrderNo(orderNo);
        memberChangeAmountDto.setTradeType(FundTradeTypeEnum.OFFSET_DELIVERYTICKET_IN.getCode());
        memberChangeAmountDto.setReType(new Byte("2"));
        accountService.changeAccount(memberChangeAmountDto);

        //增加用户提货券余额
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCustomerNo(customerNo);
        changeCouponAccountDto.setChangeAmount(new BigDecimal(canComsumeAuthority)
                .multiply(new BigDecimal(tradeCouponAmount)));
        changeCouponAccountDto.setOrderNo(orderNo);
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.DELIVERY_TICKET_GET.getCode());
        changeCouponAccountDto.setReType(new Byte("2"));
        changeCouponAccountDto.setProductTradeNo(productTradeNo);
        changeCouponAccountDto.setCouponType(Constants.CouponType.DELIVERYTICKET);
        ApiResult apiResult = couponAccountService.changeAmount(changeCouponAccountDto);
        if (apiResult.getCode()!=200) {
            log.info("增加用户提货券余额失败，"+apiResult.getMsg());
            return ApiResult.error(apiResult.getMsg());
        }
        //消费用户配货权
        ComsumeAuthorityDto comsumeAuthorityDto = new ComsumeAuthorityDto();
        comsumeAuthorityDto.setCustomerNo(customerNo);
        comsumeAuthorityDto.setProductTradeNo(productTradeNo);
        comsumeAuthorityDto.setChangeAmount(canComsumeAuthority);
        comsumeAuthorityDto.setOrderNo(orderNo);
        ApiResult apr = this.comsumeAuthority(comsumeAuthorityDto);
        if(apr.getCode()!=200){
            log.info("消费用户配货权失败，"+apiResult.getMsg());
            return ApiResult.error(apr.getMsg());
        }
        //清算规则外的辅助清算表
        CleanHelp cleanHelp = new CleanHelp();
        cleanHelp.setProfitCustomerNo(memberCustomerNo);
        cleanHelp.setLossCustomerNo(customerNo);
        cleanHelp.setAmount(changeAmount);
        cleanHelp.setHelpType("transfer");
        cleanHelp.setBusinessType(Constants.BusinessType.DELIVERY_TICKET_OFFSET);
        cleanHelp.setCreateTime(new Date());
        cleanHelp.setModifyTime(new Date());
        cleanHelp.setFlag(new Byte(Constants.Flag.VALID));
        cleanHelpService.save(cleanHelp);
        return ApiResult.success();
    }

    @Override
    public void initOffsetData() {
        //查看库内是否已有名单，若有，则不再初始化
        Condition condition = buildValidCondition(OffsetDeliveryticket.class);
        int count = offsetDeliveryticketMapper.selectCountByCondition(condition);
        if(count!=0){
            throw new ServiceException("已初始化过，不可再次初始化");
        }
        Date currentDate = new Date();
        //查询配货权非0的用户集合
        List<MatchAuthority> listWithAuthority = matchAuthorityService.getListWithAuthority();
        List<OffsetDeliveryticket> offsetDeliveryticketList = new ArrayList<>();
        for (MatchAuthority matchAuthority : listWithAuthority) {
            int authority = 25-matchAuthority.getMatchAuthority();
            if(authority>0){
                OffsetDeliveryticket offsetDeliveryticket = new OffsetDeliveryticket();
                offsetDeliveryticket.setCustomerNo(matchAuthority.getCustomerNo());
                offsetDeliveryticket.setProductTradeNo(matchAuthority.getProductTradeNo());
                offsetDeliveryticket.setOrignAuthority(25-matchAuthority.getMatchAuthority());
                offsetDeliveryticket.setRemainAuthority(25-matchAuthority.getMatchAuthority());
                offsetDeliveryticket.setComsumedAuthority(0);
                offsetDeliveryticket.setCreateTime(currentDate);
                offsetDeliveryticket.setModifyTime(currentDate);
                offsetDeliveryticket.setFlag(new Byte(Constants.Flag.VALID));
                offsetDeliveryticketList.add(offsetDeliveryticket);
            }
        }
        this.save(offsetDeliveryticketList);
    }
}
