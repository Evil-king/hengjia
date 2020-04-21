package com.baibei.hengjia.api.modules.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDetailDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ThawCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountDetailVo;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountVo;
import com.baibei.hengjia.api.modules.account.dao.CouponAccountMapper;
import com.baibei.hengjia.api.modules.account.dao.RecordCouponMapper;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.api.modules.account.model.RecordCoupon;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponFrozenService;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponThawService;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/08/05 10:07:22
* @description: CouponAccount服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CouponAccountServiceImpl extends AbstractService<CouponAccount> implements ICouponAccountService {

    @Autowired
    private CouponAccountMapper tblCpCouponAccountMapper;

    @Autowired
    private RecordCouponMapper recordCouponMapper;

    @Autowired
    private IProductMarketService productMarketService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ITradeDayService tradeDayService;

    @Autowired
    private IRecordCouponFrozenService recordCouponFrozenService;

    @Autowired
    private IRecordCouponThawService recordCouponThawService;

    @Override
    public ApiResult changeAmount(ChangeCouponAccountDto changeCouponAccountDto) {
        log.info("修改券可用金额传入的值为"+JSONObject.toJSONString(changeCouponAccountDto));
        Customer customer=customerService.findByCustomerNo(changeCouponAccountDto.getCustomerNo());
        if(customer==null){
            return ApiResult.badParam("用户不存在");
        }
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        CouponAccount couponAccount=new CouponAccount();
        if (couponAccounts.size()>0){
            couponAccount=couponAccounts.get(0);
        }else {
            couponAccount.setCustomerNo(changeCouponAccountDto.getCustomerNo());
            couponAccount.setProductTradeNo(changeCouponAccountDto.getProductTradeNo());
            couponAccount.setCouponType(changeCouponAccountDto.getCouponType());
            couponAccount.setBalance(BigDecimal.ZERO);
            couponAccount.setFrozenBalance(BigDecimal.ZERO);
            couponAccount.setThawBalance(BigDecimal.ZERO);
            couponAccount.setCreateTime(new Date());
            couponAccount.setModifyTime(new Date());
            couponAccount.setFlag(new Byte("1"));
            tblCpCouponAccountMapper.insertSelective(couponAccount);
        }
        //乐观锁实现方式
        Condition condition1=new Condition(CouponAccount.class);
        Example.Criteria criteria1=condition1.createCriteria();
        criteria1.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria1.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria1.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        criteria1.andEqualTo("balance",couponAccount.getBalance());
        if(changeCouponAccountDto.getReType()==1){
            //支出
            couponAccount.setBalance(couponAccount.getBalance().subtract(changeCouponAccountDto.getChangeAmount()));
            if (checkAmountNegative(couponAccount.getBalance())) {
                //判断是否为负数，为负数则返回余额不足
                return ApiResult.error("余额不足");
            }
        }else if(changeCouponAccountDto.getReType()==2){
            //收入
            couponAccount.setBalance(couponAccount.getBalance().add(changeCouponAccountDto.getChangeAmount()));
        }else {
            return ApiResult.badParam("类型错误");
        }

        couponAccount.setModifyTime(new Date());
        if (!updateByConditionSelective(couponAccount,condition)) {
            return ApiResult.error("资金操作失败");
        }
        insertRecordCoupon(changeCouponAccountDto,couponAccount);
        return ApiResult.success();
    }



    @Override
    public ApiResult changeAmountSpecil(ChangeCouponAccountDto changeCouponAccountDto) {
        log.info("修改券可用金额传入的值为"+JSONObject.toJSONString(changeCouponAccountDto));
        Customer customer=customerService.findByCustomerNo(changeCouponAccountDto.getCustomerNo());
        if(customer==null){
            return ApiResult.badParam("用户不存在");
        }
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        CouponAccount couponAccount=new CouponAccount();
        if (couponAccounts.size()>0){
            couponAccount=couponAccounts.get(0);
        }else {
            couponAccount.setCustomerNo(changeCouponAccountDto.getCustomerNo());
            couponAccount.setProductTradeNo(changeCouponAccountDto.getProductTradeNo());
            couponAccount.setCouponType(changeCouponAccountDto.getCouponType());
            couponAccount.setBalance(BigDecimal.ZERO);
            couponAccount.setFrozenBalance(BigDecimal.ZERO);
            couponAccount.setThawBalance(BigDecimal.ZERO);
            couponAccount.setCreateTime(new Date());
            couponAccount.setModifyTime(new Date());
            couponAccount.setFlag(new Byte("1"));
            tblCpCouponAccountMapper.insertSelective(couponAccount);
        }
        //乐观锁实现方式
        Condition condition1=new Condition(CouponAccount.class);
        Example.Criteria criteria1=condition1.createCriteria();
        criteria1.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria1.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria1.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        criteria1.andEqualTo("balance",couponAccount.getBalance());
        if(changeCouponAccountDto.getReType()==1){
            //支出
            couponAccount.setBalance(couponAccount.getBalance().subtract(changeCouponAccountDto.getChangeAmount()));
        }else if(changeCouponAccountDto.getReType()==2){
            //收入
            if (checkAmountNegative(couponAccount.getBalance())) {
                //判断是否为负数，为负数则返回余额不足
                return ApiResult.error("余额不足");
            }
            couponAccount.setBalance(couponAccount.getBalance().add(changeCouponAccountDto.getChangeAmount()));
        }else {
            return ApiResult.badParam("类型错误");
        }

        couponAccount.setModifyTime(new Date());
        if (!updateByConditionSelective(couponAccount,condition)) {
            return ApiResult.error("资金操作失败");
        }
        insertRecordCoupon(changeCouponAccountDto,couponAccount);
        return ApiResult.success();
    }


    @Override
    public ApiResult changeFrozenAmount(ChangeCouponAccountDto changeCouponAccountDto) {
        log.info("修改券冻结金额传入的值为"+JSONObject.toJSONString(changeCouponAccountDto));
        Customer customer=customerService.findByCustomerNo(changeCouponAccountDto.getCustomerNo());
        if(customer==null){
            return ApiResult.badParam("用户不存在");
        }
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        CouponAccount couponAccount=new CouponAccount();
        if (couponAccounts.size()>0){
            couponAccount=couponAccounts.get(0);
        }else {
            couponAccount.setCustomerNo(changeCouponAccountDto.getCustomerNo());
            couponAccount.setProductTradeNo(changeCouponAccountDto.getProductTradeNo());
            couponAccount.setCouponType(changeCouponAccountDto.getCouponType());
            couponAccount.setBalance(BigDecimal.ZERO);
            couponAccount.setFrozenBalance(BigDecimal.ZERO);
            couponAccount.setThawBalance(BigDecimal.ZERO);
            couponAccount.setCreateTime(new Date());
            couponAccount.setModifyTime(new Date());
            couponAccount.setFlag(new Byte("1"));
            tblCpCouponAccountMapper.insertSelective(couponAccount);
        }
        //乐观锁实现方式
        Condition condition1=new Condition(CouponAccount.class);
        Example.Criteria criteria1=condition1.createCriteria();
        criteria1.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria1.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria1.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        criteria1.andEqualTo("frozenBalance",couponAccount.getFrozenBalance());
        if(changeCouponAccountDto.getReType()==1){
            //支出
            couponAccount.setFrozenBalance(couponAccount.getFrozenBalance().subtract(changeCouponAccountDto.getChangeAmount()));
        }else if(changeCouponAccountDto.getReType()==2){
            //收入
            couponAccount.setFrozenBalance(couponAccount.getFrozenBalance().add(changeCouponAccountDto.getChangeAmount()));
        }else {
            return ApiResult.badParam("类型错误");
        }
        if (checkAmountNegative(couponAccount.getFrozenBalance())) {
            //判断是否为负数，为负数则返回余额不足
            return ApiResult.error("余额不足");
        }
        couponAccount.setModifyTime(new Date());
        if (!updateByConditionSelective(couponAccount,condition)) {
            return ApiResult.error("资金操作失败");
        }
        recordCouponFrozenService.insertOne(changeCouponAccountDto,couponAccount);
        return ApiResult.success();
    }

    @Override
    public ApiResult changeThawAmount(ChangeCouponAccountDto changeCouponAccountDto) {
        log.info("修改券解冻金额传入的值为"+JSONObject.toJSONString(changeCouponAccountDto));
        Customer customer=customerService.findByCustomerNo(changeCouponAccountDto.getCustomerNo());
        if(customer==null){
            return ApiResult.badParam("用户不存在");
        }
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        CouponAccount couponAccount=new CouponAccount();
        if (couponAccounts.size()>0){
            couponAccount=couponAccounts.get(0);
        }else {
            couponAccount.setCustomerNo(changeCouponAccountDto.getCustomerNo());
            couponAccount.setProductTradeNo(changeCouponAccountDto.getProductTradeNo());
            couponAccount.setCouponType(changeCouponAccountDto.getCouponType());
            couponAccount.setBalance(BigDecimal.ZERO);
            couponAccount.setFrozenBalance(BigDecimal.ZERO);
            couponAccount.setThawBalance(BigDecimal.ZERO);
            couponAccount.setCreateTime(new Date());
            couponAccount.setModifyTime(new Date());
            couponAccount.setFlag(new Byte("1"));
            tblCpCouponAccountMapper.insertSelective(couponAccount);
        }
        //乐观锁实现方式
        Condition condition1=new Condition(CouponAccount.class);
        Example.Criteria criteria1=condition1.createCriteria();
        criteria1.andEqualTo("customerNo",changeCouponAccountDto.getCustomerNo());
        criteria1.andEqualTo("productTradeNo",changeCouponAccountDto.getProductTradeNo());
        criteria1.andEqualTo("couponType",changeCouponAccountDto.getCouponType());
        criteria1.andEqualTo("thawBalance",couponAccount.getThawBalance());
        if(changeCouponAccountDto.getReType()==1){
            //支出
            couponAccount.setThawBalance(couponAccount.getThawBalance().subtract(changeCouponAccountDto.getChangeAmount()));
        }else if(changeCouponAccountDto.getReType()==2){
            //收入
            couponAccount.setThawBalance(couponAccount.getThawBalance().add(changeCouponAccountDto.getChangeAmount()));
        }else {
            return ApiResult.badParam("类型错误");
        }
        if (checkAmountNegative(couponAccount.getThawBalance())) {
            //判断是否为负数，为负数则返回余额不足
            return ApiResult.error("余额不足");
        }
        couponAccount.setModifyTime(new Date());
        if (!updateByConditionSelective(couponAccount,condition)) {
            return ApiResult.error("资金操作失败");
        }
        recordCouponThawService.insertOne(changeCouponAccountDto,couponAccount);
        return ApiResult.success();
    }

    @Override
    public ApiResult<BigDecimal> getByCustomerNo(String customerNo,String productTradeNo,String couponType) {
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("couponType",couponType);
        if(!StringUtils.isEmpty(productTradeNo)){
            criteria.andEqualTo("productTradeNo",productTradeNo);
        }
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        BigDecimal totalBalance=BigDecimal.ZERO;
        for (int i = 0; i <couponAccounts.size() ; i++) {
            totalBalance=totalBalance.add(couponAccounts.get(i).getBalance().add(couponAccounts.get(i).getFrozenBalance()));
        }
        return ApiResult.success(totalBalance);
    }

    @Override
    public MyPageInfo<CouponAccountVo> myList(CouponAccountDto couponAccountDto) {
        PageHelper.startPage(couponAccountDto.getCurrentPage(), couponAccountDto.getPageSize());
        List<CouponAccountVo> list = tblCpCouponAccountMapper.selectCouponAccount(couponAccountDto);
        MyPageInfo<CouponAccountVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public CouponAccount getCouponAccountById(Long couponAccountId) {
        if (StringUtils.isEmpty(couponAccountId)) {
            throw new ServiceException("未指定券账户");
        }
        CouponAccount couponAccount = new CouponAccount();
        couponAccount.setId(couponAccountId);
        return tblCpCouponAccountMapper.selectByPrimaryKey(couponAccount);
    }

    @Override
    public CouponAccount getCouponAccount(String customerNo, String productTradeNo, String couponType) {
        Condition condition = new Condition(CouponAccount.class);
        Example.Criteria criteria = condition.createCriteria();
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("查询券余额失败，用户编码不能为空");
        }
        if (StringUtils.isEmpty(productTradeNo)) {
            throw new ServiceException("查询券余额失败，商品交易编码不能为空");
        }
        if (StringUtils.isEmpty(couponType)) {
            throw new ServiceException("查询券余额失败，券类型不能为空");
        }
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        criteria.andEqualTo("couponType",couponType);
        List<CouponAccount> couponAccountList = tblCpCouponAccountMapper.selectByCondition(condition);
        if (couponAccountList.size()>1) {
            throw new ServiceException("error:couponAccount should select one but more");
        }
        return couponAccountList.size()==0?null:couponAccountList.get(0);
    }

    @Override
    public ApiResult thawCouponAccount(ThawCouponAccountDto thawCouponAccountDto) {
        if(!tradeDayService.isTradeDay(new Date())){
            return ApiResult.badParam("请在交易日进行解冻操作");
        }
        if(thawCouponAccountDto.getThawAmount().compareTo(BigDecimal.ZERO)<1){
            return ApiResult.badParam("请输入大于0的值");
        }
        String[] arg=thawCouponAccountDto.getThawAmount().toString().split("\\.");
        if(arg.length>1){
            if(arg[1].length()>2){
                return ApiResult.badParam("最多只能输入2位小数");
            }
        }
        Condition condition = new Condition(CouponAccount.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo",thawCouponAccountDto.getCustomerNo());
        criteria.andEqualTo("productTradeNo",thawCouponAccountDto.getProductTradeNo());
        criteria.andEqualTo("couponType",thawCouponAccountDto.getCouponType());
        List<CouponAccount> couponAccountList = tblCpCouponAccountMapper.selectByCondition(condition);
        if (couponAccountList.size()>1) {
            throw new ServiceException("error:couponAccount should select one but more");
        }
        CouponAccount couponAccount=couponAccountList.get(0);
        if(thawCouponAccountDto.getThawAmount().compareTo(couponAccount.getFrozenBalance())==1){
            //需要解冻的金额大于冻结的金额，无法进行解冻
            return ApiResult.badParam("冻结金额不足");
        }
        if(thawCouponAccountDto.getThawAmount().compareTo(couponAccount.getThawBalance())==1){
            //需要解冻的金额大于解冻的金额，无法进行解冻
            return ApiResult.badParam("解冻金额不足");
        }
        couponAccount.setBalance(couponAccount.getBalance().add(thawCouponAccountDto.getThawAmount()));
        if (!updateByConditionSelective(couponAccount,condition)) {
            return ApiResult.error("资金操作失败");
        }
        ChangeCouponAccountDto changeCouponAccountDto=new ChangeCouponAccountDto();
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.COUPON_THAW.getCode());
        changeCouponAccountDto.setReType(new Byte("2"));
        changeCouponAccountDto.setOrderNo(IdWorker.get32UUID());
        changeCouponAccountDto.setCustomerNo(couponAccount.getCustomerNo());
        changeCouponAccountDto.setProductTradeNo(thawCouponAccountDto.getProductTradeNo());
        changeCouponAccountDto.setCouponType(thawCouponAccountDto.getCouponType());
        changeCouponAccountDto.setChangeAmount(thawCouponAccountDto.getThawAmount());
        insertRecordCoupon(changeCouponAccountDto,couponAccount);
        //修改解冻金额
        changeCouponAccountDto.setReType(new Byte("1"));
        changeCouponAccountDto.setChangeAmount(thawCouponAccountDto.getThawAmount());
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.THAWAMOUT_OUT.getCode());
        changeThawAmount(changeCouponAccountDto);
        //修改冻结金额
        changeCouponAccountDto.setReType(new Byte("1"));
        changeCouponAccountDto.setChangeAmount(thawCouponAccountDto.getThawAmount());
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.FROZENAMOUT_OUT.getCode());
        changeFrozenAmount(changeCouponAccountDto);
        return ApiResult.success();
    }

    @Override
    public CouponAccountDetailVo getCouponAccountDetail(CouponAccountDetailDto couponAccountDetailDto) {
        List<String> statusList=new ArrayList();
        statusList.add("trading");//正在交易
        statusList.add("onmarket");//已上市
        List<String> allProductTradeNo = productMarketService.getAllProductTradeNo(statusList);
        String productTradeNo;
        if(allProductTradeNo.size()>=1){
            productTradeNo=allProductTradeNo.get(0);
        }else {
            productTradeNo="error";
        }
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("couponType",couponAccountDetailDto.getCouponType());
        criteria.andEqualTo("customerNo",couponAccountDetailDto.getCustomerNo());
        List<CouponAccount> couponAccounts=tblCpCouponAccountMapper.selectByCondition(condition);
        CouponAccount couponAccount=new CouponAccount();
        if (couponAccounts.size()>0){
                couponAccount=couponAccounts.get(0);
          }else {
                couponAccount.setCustomerNo(couponAccountDetailDto.getCustomerNo());
                couponAccount.setProductTradeNo(productTradeNo);
                couponAccount.setCouponType(couponAccountDetailDto.getCouponType());
                couponAccount.setBalance(BigDecimal.ZERO);
                couponAccount.setFrozenBalance(BigDecimal.ZERO);
                couponAccount.setThawBalance(BigDecimal.ZERO);
                couponAccount.setCreateTime(new Date());
                couponAccount.setModifyTime(new Date());
                couponAccount.setFlag(new Byte("1"));
                tblCpCouponAccountMapper.insertSelective(couponAccount);
        }
        //TODO 暂时冗余查询所有商品的券额度，后续产生多商品需要操作
        CouponAccountDetailVo couponAccountDetailVo=BeanUtil.copyProperties(couponAccount,CouponAccountDetailVo.class);
        couponAccountDetailVo.setTotalCouponBalance(couponAccount.getBalance().add(couponAccount.getFrozenBalance()));
        return couponAccountDetailVo;
    }

    @Override
    public ApiResult<List<CouponAccount>> findByBalance(BigDecimal balance, String customerNo, String productTradeNo, String couponType) {
        Condition condition=new Condition(CouponAccount.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        if(!StringUtils.isEmpty(customerNo)){
            criteria.andEqualTo("customerNo",customerNo);
        }
        criteria.andEqualTo("productTradeNo",productTradeNo);
        criteria.andEqualTo("couponType",couponType);
        criteria.andGreaterThanOrEqualTo("balance",balance);
        return ApiResult.success(tblCpCouponAccountMapper.selectByCondition(condition));
    }

    /**
     * 插入一条可用资金修改流水
     * @param frozenAmountDto
     * @param account
     */
    private void insertRecordCoupon(ChangeCouponAccountDto frozenAmountDto, CouponAccount account) {
        RecordCoupon recordCoupon=new RecordCoupon();
        String recordNo=IdWorker.get32UUID();
        recordCoupon.setRecordNo(frozenAmountDto.getOrderNo());
        //交易商编码
        recordCoupon.setCustomerNo(frozenAmountDto.getCustomerNo());
        //如果
        String change=frozenAmountDto.getReType()==1? "-":"";
        //变动数额
        recordCoupon.setChangeAmount(change+frozenAmountDto.getChangeAmount().setScale(2).toString());
        //券类型
        recordCoupon.setCouponType(frozenAmountDto.getCouponType());
        //商品交易类型
        recordCoupon.setProductTradeNo(frozenAmountDto.getProductTradeNo());
        //券余额
        recordCoupon.setBalance(account.getBalance());
        //交易类型
        recordCoupon.setTradeType(frozenAmountDto.getTradeType());
        //冻结类型 1：支出，2：收入
        recordCoupon.setRetype(frozenAmountDto.getReType());
        recordCoupon.setCreateTime(new Date());
        recordCoupon.setModifyTime(new Date());
        if(CouponAccountTradeTypeEnum.DELIVERY.getCode() == frozenAmountDto.getTradeType()){
            recordCoupon.setRemark(IntegralTradeTypeEnum.DELIVERY.getMsg());
        }
        //是否有效
        recordCoupon.setFlag(new Byte("1"));
        recordCouponMapper.insertSelective(recordCoupon);
    }
    /**
     * 判断一个值是否为负数，
     *
     * @param balance 判断的金额
     * @return true：负数 false 非负数
     */
    private boolean checkAmountNegative(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) <0) {
            return true;
        } else {
            return false;
        }
    }
}
