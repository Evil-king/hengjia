package com.baibei.hengjia.api.modules.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAndIntegralAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.bean.dto.IntegralDetailDto;
import com.baibei.hengjia.api.modules.account.bean.vo.AccountVo;
import com.baibei.hengjia.api.modules.account.bean.vo.CustomerDealOrderVo;
import com.baibei.hengjia.api.modules.account.bean.vo.FundInformationVo;
import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.account.dao.AccountMapper;
import com.baibei.hengjia.api.modules.account.dao.CustomerIntegralMapper;
import com.baibei.hengjia.api.modules.account.dao.RecordFreezingAmountMapper;
import com.baibei.hengjia.api.modules.account.dao.RecordMoneyMapper;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.model.CustomerIntegral;
import com.baibei.hengjia.api.modules.account.model.RecordFreezingAmount;
import com.baibei.hengjia.api.modules.account.model.RecordMoney;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.account.service.ICustomerIntegralService;
import com.baibei.hengjia.api.modules.settlement.bean.vo.AccountInfo;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import com.baibei.hengjia.common.tool.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 14:39:31
* @description: Account服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AccountServiceImpl extends AbstractService<Account> implements IAccountService {
    @Autowired
    private ICouponAccountService couponAccountService;

    @Autowired
    private ICustomerIntegralService customerIntegralService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private CustomerIntegralMapper customerIntegralMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RecordFreezingAmountMapper recordFreezingAmountMapper;

    @Autowired
    private RecordMoneyMapper recordMoneyMapper;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public ApiResult<String> register(String customerNo) {
        //注册需要向多个表插入数据
        //创建一条资金信息
        Account account = new Account();
        account.setCustomerNo(customerNo);
        account.setCreateTime(new Date());
        account.setModifyTime(new Date());
        account.setFlag(new Byte("1"));
        accountMapper.insertSelective(account);
        //创建一条积分信息
        CustomerIntegral customerIntegral = new CustomerIntegral();
        customerIntegral.setCustomerNo(customerNo);
        customerIntegral.setIntegralNo(101L);//积分类型ID暂时写死
        customerIntegral.setCreateTime(new Date());
        customerIntegral.setModifyTime(new Date());
        customerIntegral.setFlag(new Byte("1"));
        customerIntegralMapper.insertSelective(customerIntegral);
        return ApiResult.success("账户开户成功");
    }

    /**
     * 修改金额
     * @param accountBalanceDto
     * @return
     */
    @Override
    public void changeAccount(ChangeAmountDto accountBalanceDto) {
        log.info("修改金额传入的值为"+accountBalanceDto);
        Account account=checkAccount(accountBalanceDto.getCustomerNo());
        if(account==null){
            throw new ServiceException("帐户不存在");
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        if(accountBalanceDto.getReType()==1){
            //支出
            account.setTotalBalance(account.getTotalBalance().subtract(accountBalanceDto.getChangeAmount()));
            account.setBalance(account.getBalance().subtract(accountBalanceDto.getChangeAmount()));
        }else if(accountBalanceDto.getReType()==2){
            //收入
            account.setTotalBalance(account.getTotalBalance().add(accountBalanceDto.getChangeAmount()));
            account.setBalance(account.getBalance().add(accountBalanceDto.getChangeAmount()));
        }else {
            throw new ServiceException("金额类型错误");
        }
        if (checkAmountNegative(account.getBalance())) {
            //判断是否为负数，为负数则返回余额不足
            throw new ServiceException("余额不足");
        }
        //判断当前余额是否小于可出金余额
        //对比可提现金额与当前可用资金
        if(account.getWithdrawableCash().compareTo(account.getBalance())>0){
            //如果可提现金额大于可用资金，则当前可用资金就是可提现金额
            account.setWithdrawableCash(account.getBalance());
        }
        account.setModifyTime(new Date());
        log.info("修改后的可提现金额为"+account.getWithdrawableCash());
        //插入一条可用资金修改流水
        insertRecordMoney(accountBalanceDto,account);
        //判断是否为修改成功
        if (!updateByConditionSelective(account,condition)) {
            throw new ServiceException("资金扣除失败");
        }
    }

    /**
     * 同时修改积分和提货券（特殊情况下，允许穿仓）
     * @param changeCouponAndIntegralAmountDto
     * @return
     */
    @Override
    public void changeCouponAndIntegralAmount(ChangeCouponAndIntegralAmountDto changeCouponAndIntegralAmountDto) {
        log.info("同时修改积分和提货券入参:"+JSONObject.toJSONString(changeCouponAndIntegralAmountDto));

        Customer customerNo = customerService.findByCustomerNo(changeCouponAndIntegralAmountDto.getCustomerNo());
        if(customerNo != null && "0".equals(customerNo.getDeduction())){
            //调用扣减券和积分的方法
            ApiResult couponApiResult = couponAccountService.
                    changeAmountSpecil(changeCouponAndIntegralAmountDto.getChangeCouponAccountDto());
            ApiResult integralApiResult = customerIntegralService.
                    changeIntegralSpecil(changeCouponAndIntegralAmountDto.getChangeIntegralDto());
            if(couponApiResult.hasSuccess() && integralApiResult.hasSuccess()){
                //修改用户状态为新用户
                Customer byCustomerNo = customerService.findByCustomerNo(changeCouponAndIntegralAmountDto.getCustomerNo());
                if(byCustomerNo != null){
                    byCustomerNo.setDeduction("1");
                    byCustomerNo.setModifyTime(new Date());
                    customerService.updateByCustomer(byCustomerNo);
                }
            }
        }
    }



    public ApiResult<AccountVo> findAccount(CustomerNoDto customerNoDto) {
        Account account = new Account();
        account.setFlag(new Byte("1"));
        account.setCustomerNo(customerNoDto.getCustomerNo());
        account = accountMapper.selectOne(account);
        ApiResult result = new ApiResult();
        if (null == account) {
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
        } else {
            AccountVo accountVo = new AccountVo();
            accountVo = BeanUtil.copyProperties(account, accountVo.getClass());
            result.setData(accountVo);
            result.setCode(ResultEnum.SUCCESS.getCode());
            result.setMsg(ResultEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @Override
    public void changeFreezenAmountByType (ChangeAmountDto frozenAmountDto) {
        log.info("修改冻结金额传入的值为"+frozenAmountDto);
        Account account=checkAccount(frozenAmountDto.getCustomerNo());
        if(account==null){
            throw new ServiceException("帐户不存在");
        }
        if(2==frozenAmountDto.getReType()){
            //冻结类型为冻结
             frozenAmount(frozenAmountDto,account);
        }else if(1==frozenAmountDto.getReType()){
            //冻结类型为解冻
             thawAmount(frozenAmountDto,account);
        }else{
            throw new ServiceException("冻结参数不能为0");
        }
    }

    @Override
    public void thawAmountByTrade(ChangeAmountDto frozenAmountDto) {
        log.info("交易解冻传入的值为"+frozenAmountDto);
        Account account=checkAccount(frozenAmountDto.getCustomerNo());
        if(account==null){
            //没找到用户
            throw new ServiceException("帐户不存在");
        }
        if(1==frozenAmountDto.getReType()){
            //减少冻结资金的冻结资金数额
            BigDecimal freezingAmount = account.getFreezingAmount().subtract(frozenAmountDto.getChangeAmount());
            // 总余额减少
            BigDecimal totalBalance = account.getTotalBalance().subtract(frozenAmountDto.getChangeAmount());
            if (checkAmountNegative(freezingAmount)) {
                //判断是否为负数，为负数则返回冻结资金小于冻结数额
                throw  new ServiceException("冻结金额不足,无法进行解冻操作");
            }
            if (checkAmountNegative(totalBalance)) {
                throw  new ServiceException("总余额不足");
            }
            //乐观锁实现方式
            Condition condition=new Condition(Account.class);
            Example.Criteria criteria=condition.createCriteria();
            criteria.andEqualTo("customerNo",account.getCustomerNo());
            criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
            criteria.andEqualTo("totalBalance",account.getTotalBalance());
            Account newAccount=new Account();
            newAccount.setFreezingAmount(freezingAmount);
            newAccount.setTotalBalance(totalBalance);
            newAccount.setModifyTime(new Date());
            newAccount.setBalance(account.getBalance());
            newAccount.setWithdrawableCash(account.getWithdrawableCash());
            //插入一条冻结资金修改流水
            insertRecordFreezingAmount(frozenAmountDto, newAccount);
            if(frozenAmountDto.getTradeType()==Byte.valueOf("103")){
                //挂牌买入解冻
                frozenAmountDto.setTradeType(FundTradeTypeEnum.MAIN_BUY.getCode());
                insertRecordMoney(frozenAmountDto,newAccount);
            }else {
                throw  new ServiceException("交易成功解冻交易类型错误！");
            }

            //判断是否为修改成功
            if (!updateByConditionSelective(newAccount,condition)) {
                throw  new ServiceException("资金操作失败！");
            }
        }else{
            throw  new ServiceException("参数输入错误！");
        }

    }

    /**
     * 冻结资金
     * @param frozenAmountDto
     * @param account
     * @return
     */
    public void frozenAmount(ChangeAmountDto frozenAmountDto, Account account) {
        log.info("冻结金额传入的值为"+frozenAmountDto);
        ApiResult result = new ApiResult();

        //添加冻结资金后的冻结资金数额
        BigDecimal freezingAmount = account.getFreezingAmount().add(frozenAmountDto.getChangeAmount());
        //冻结资金后的可用资金
        BigDecimal balance = account.getBalance().subtract(frozenAmountDto.getChangeAmount());
        if (checkAmountNegative(balance)) {
            //判断是否为负数，为负数则返回余额不足
            result.setCode(ResultEnum.BALANCE_NOT_ENOUGH.getCode());
            result.setMsg(ResultEnum.BALANCE_NOT_ENOUGH.getMsg());
            throw  new ServiceException("余额不足");
        }
        //可提现金额
        BigDecimal withDrawCash=account.getWithdrawableCash();

        //对比可提现金额与当前可用资金
        if(withDrawCash.compareTo(balance)>0){
            //将操作了的可提资金放入缓存中
            String key=MessageFormat.format(RedisConstant.ACCOUNT_WITHDRAW_AMOUT,account.getCustomerNo()+frozenAmountDto.getOrderNo());
            redisUtil.set(key,withDrawCash.subtract(balance)+"");
            //如果可提现金额大于可用资金，则当前可用资金就是可提现金额
            withDrawCash=balance;

        }

        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setFreezingAmount(freezingAmount);
        newAccount.setWithdrawableCash(withDrawCash);
        newAccount.setModifyTime(new Date());
        //插入一条冻结资金修改流水
        insertRecordFreezingAmount(frozenAmountDto, newAccount);
//        //插入一条可用资金修改流水
//        insertRecordMoney(frozenAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw new ServiceException("资金扣除失败");
        }
    }

    /**
     * 插入一条可用资金修改流水
     * @param frozenAmountDto
     * @param account
     */
    private void insertRecordMoney(ChangeAmountDto frozenAmountDto, Account account) {
        RecordMoney recordMoney=new RecordMoney();
        String recordNo=IdWorker.get32UUID();
        recordMoney.setRecordNo(recordNo);
        //交易商编码
        recordMoney.setCustomerNo(frozenAmountDto.getCustomerNo());
        //如果
        String change=frozenAmountDto.getReType()==1? "-":"";
        //变动数额
        recordMoney.setChangeAmount(change+frozenAmountDto.getChangeAmount().setScale(2).toString());
        //可用资金数额
        recordMoney.setBalance(account.getBalance());
        //可提现金额数额
        recordMoney.setWithdraw(account.getWithdrawableCash());
        //交易类型
        recordMoney.setTradeType(frozenAmountDto.getTradeType());
        //订单号
        recordMoney.setOrderNo(frozenAmountDto.getOrderNo());
        //冻结类型 1：支出，2：收入
        recordMoney.setRetype(frozenAmountDto.getReType());
        recordMoney.setCreateTime(new Date());
        recordMoney.setModifyTime(new Date());
        //是否有效
        recordMoney.setFlag(new Byte("1"));
        recordMoneyMapper.insertSelective(recordMoney);
    }

    /**
     * 判断该帐户是否存在
     *
     * @param customerNo 交易商编码
     * @return
     */
    @Override
    public Account checkAccount(String customerNo) {
        Account account = new Account();
        account.setFlag(new Byte("1"));
        account.setCustomerNo(customerNo);
        //先查询账户信息存不存在
        account = accountMapper.selectOne(account);
        return account;
    }

    public void thawAmount(ChangeAmountDto frozenAmountDto, Account account) {
        log.info("挂单撤回解冻传入的值为"+frozenAmountDto);
        //减少冻结资金的冻结资金数额
        BigDecimal freezingAmount = account.getFreezingAmount().subtract(frozenAmountDto.getChangeAmount());
        //解冻资金后的可用资金
        BigDecimal balance = account.getBalance().add(frozenAmountDto.getChangeAmount());
        if (checkAmountNegative(freezingAmount)) {
            //判断是否为负数，为负数则返回冻结资金小于冻结数额
            throw  new ServiceException("冻结金额不足,无法进行解冻操作");
        }
        String key=MessageFormat.format(RedisConstant.ACCOUNT_WITHDRAW_AMOUT,account.getCustomerNo()+frozenAmountDto.getOrderNo());
        String withdraw=redisUtil.get(key);
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        if(withdraw!=null){
            log.info("上次挂单的可提金额为"+withdraw);
            if (new BigDecimal(withdraw).compareTo(frozenAmountDto.getChangeAmount())<0){
                //如果小于撤回金额
                account.setWithdrawableCash(account.getWithdrawableCash().add(new BigDecimal(withdraw)));
            }else {
                //如果大于撤回金额
                account.setWithdrawableCash(account.getWithdrawableCash().add(frozenAmountDto.getChangeAmount()));
            }
            log.info("撤回后的可提金额为"+account.getWithdrawableCash());
        }
        redisUtil.delete(key);
        //乐观锁实现方式
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setFreezingAmount(freezingAmount);
        newAccount.setModifyTime(new Date());
        newAccount.setWithdrawableCash(account.getWithdrawableCash());
        //插入一条冻结资金修改流水
        insertRecordFreezingAmount(frozenAmountDto, newAccount);
        //插入一条可用资金修改流水
//        ChangeAmountDto changeAmountDto=BeanUtil.copyProperties(frozenAmountDto,ChangeAmountDto.class);
//        changeAmountDto.setReType(new Byte("2"));
//        changeAmountDto.setTradeType(FundTradeTypeEnum.REPEAL_MONEY.getCode());
//        insertRecordMoney(frozenAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw  new ServiceException("资金操作失败！");
        }
    }

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    @Override
    public ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto) {
        ApiResult result=new ApiResult();
        if(!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入新密码不一致");
        }
        Account account=checkAccount(updatePasswordDto.getCustomerNo());
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return  result;
        }
        if(!(updatePasswordDto.getNewPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        String password=MD5Util.md5Hex(updatePasswordDto.getOldPassword(),account.getSalt());
        if(!password.equals(account.getPassword())){
            result.setCode(ResultEnum.ACCOUNT_PASSWORD_ERRO.getCode());
            result.setMsg(ResultEnum.ACCOUNT_PASSWORD_ERRO.getMsg());
            return  result;
        }
        account.setPassword(MD5Util.md5Hex(updatePasswordDto.getNewPassword(),account.getSalt()));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public ApiResult<FundInformationVo> fundInformation(CustomerNoDto customerNoDto,BigDecimal marketValue) {
        ApiResult result=new ApiResult();
        Account account=checkAccount(customerNoDto.getCustomerNo());
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        //查询积分
        IntegralDetailDto integralDetailDto=new IntegralDetailDto();
        integralDetailDto.setIntegralNo(101L);
        integralDetailDto.setCustomerNo(customerNoDto.getCustomerNo());
        IntegralDetailVo integralDetailVo=customerIntegralMapper.findIntegralDetailByCustomer(integralDetailDto);
        if(integralDetailVo==null){
            integralDetailVo=new IntegralDetailVo();
        }

        FundInformationVo fundInformationVo=BeanUtil.copyProperties(account,FundInformationVo.class);
        //持仓市值
        fundInformationVo.setHoldMarketValue(marketValue);
        //总资产 总资金+持仓市值
        fundInformationVo.setTotalAssets(account.getTotalBalance().add(marketValue).add(integralDetailVo.getBalance()==null? BigDecimal.ZERO:integralDetailVo.getBalance()));
        fundInformationVo.setPasswordFlag(StringUtil.isEmpty(account.getPassword())?0:1);
        ApiResult<BigDecimal> couponResult=couponAccountService.getByCustomerNo(customerNoDto.getCustomerNo(),null,Constants.CouponType.VOUCHERS);
        if(couponResult.hasSuccess()){
            fundInformationVo.setTotalAssets(fundInformationVo.getTotalAssets().add(couponResult.getData()));
        }
        ApiResult<BigDecimal> deliveryResult=couponAccountService.getByCustomerNo(customerNoDto.getCustomerNo(),null,Constants.CouponType.DELIVERYTICKET);
        if(deliveryResult.hasSuccess()){
            fundInformationVo.setTotalAssets(fundInformationVo.getTotalAssets().add(deliveryResult.getData()));
        }
        if(BigDecimal.ZERO.compareTo(fundInformationVo.getTotalAssets())>0){
            fundInformationVo.setTotalAssets(BigDecimal.ZERO);
        }
        return ApiResult.success(fundInformationVo);
    }

    @Override
    public ApiResult<String> forgetPassword(String customerNo, String password) {
        ApiResult result=new ApiResult();
        Account account=checkAccount(customerNo);
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        //重新生成盐值
        String salt=MD5Util.getRandomSalt(10);
        account.setSalt(salt);
        account.setPassword(MD5Util.md5Hex(password,salt));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public String updateWithDrawWithPassword(String customerNo,String password,BigDecimal amount,String orderNo,Byte tradeType,Byte reType) {
        Account account=checkAccount(customerNo);
        if(account==null){
            throw  new ServiceException("账户不存在") ;
        }

        if(!account.getPassword().equals(MD5Util.md5Hex(password,account.getSalt()))){
            throw  new ServiceException("资金密码错误") ;
        }
        //可用余额
        BigDecimal balance;
        //总资金
        BigDecimal totalBalance;
        //可提现金额
        BigDecimal withdraw;
        if(reType.equals(new Byte("1"))){
            if(account.getWithdrawableCash().compareTo(amount)<0){
                throw  new ServiceException("余额不足") ;
            }
            //为出金，需要扣
            balance=account.getBalance().subtract(amount);
            totalBalance=account.getTotalBalance().subtract(amount);
            withdraw=account.getWithdrawableCash().subtract(amount);
        }else if(reType.equals(new Byte("2"))){
            //为入金，需要加
            balance=account.getBalance().add(amount);
            totalBalance=account.getTotalBalance().add(amount);
            withdraw=account.getWithdrawableCash().add(amount);
        }else {
            throw  new ServiceException("出入金方向不明确") ;
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setTotalBalance(totalBalance);
        newAccount.setWithdrawableCash(withdraw);
        newAccount.setModifyTime(new Date());
        ChangeAmountDto changeAmountDto=new ChangeAmountDto();
        changeAmountDto.setCustomerNo(account.getCustomerNo());
        changeAmountDto.setChangeAmount(amount);
        changeAmountDto.setTradeType(tradeType);
        changeAmountDto.setOrderNo(orderNo);
        changeAmountDto.setReType(reType);
        //插入一条可用资金修改流水
        insertRecordMoney(changeAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw  new ServiceException("资金操作失败！");
        }
        return "success";
    }

    @Override
    public String updateWithDraw(String customerNo, BigDecimal amount, String orderNo, Byte tradeType, Byte reType) {
        Account account=checkAccount(customerNo);
        if(account==null){
            throw  new ServiceException("账户不存在") ;
        }

        //可用余额
        BigDecimal balance;
        //总资金
        BigDecimal totalBalance;
        //可提现金额
        BigDecimal withdraw;
        if(reType.equals(new Byte("1"))){
            if(account.getWithdrawableCash().compareTo(amount)<0){
                throw  new ServiceException("余额不足") ;
            }
            //为出金，需要扣
            balance=account.getBalance().subtract(amount);
            totalBalance=account.getTotalBalance().subtract(amount);
            withdraw=account.getWithdrawableCash().subtract(amount);
        }else if(reType.equals(new Byte("2"))){
            //为入金，需要加
            balance=account.getBalance().add(amount);
            totalBalance=account.getTotalBalance().add(amount);
            withdraw=account.getWithdrawableCash().add(amount);
        }else {
            throw  new ServiceException("出入金方向不明确") ;
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setTotalBalance(totalBalance);
        newAccount.setWithdrawableCash(withdraw);
        newAccount.setModifyTime(new Date());
        ChangeAmountDto changeAmountDto=new ChangeAmountDto();
        changeAmountDto.setCustomerNo(account.getCustomerNo());
        changeAmountDto.setChangeAmount(amount);
        changeAmountDto.setTradeType(tradeType);
        changeAmountDto.setOrderNo(orderNo);
        changeAmountDto.setReType(reType);
        //插入一条可用资金修改流水
        insertRecordMoney(changeAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw  new ServiceException("资金操作失败！");
        }else {
            return "success";
        }
    }

    /**
     * 根据用户编号查看用户可提现资金够不够
     * @param customerNo
     * @param amount
     * @throws ServiceException
     */
    @Override
    public ApiResult getWithdrawCash(String customerNo, BigDecimal amount, String password)  {
        Account account=checkAccount(customerNo);
        if(account==null){
            return ApiResult.error("账户不存在");
        }
        if(amount.compareTo(new BigDecimal(2))<1){
            return ApiResult.error("请输入正确的金额");
        }
        if(account.getWithdrawableCash().compareTo(amount)<0){
            return ApiResult.error("余额不足");
        }
        if(password!=null){
            if(!account.getPassword().equals(MD5Util.md5Hex(password,account.getSalt()))){
                return ApiResult.error("资金密码不正确");
            }
            return ApiResult.success();
        }else {
            return ApiResult.error("资金密码为空");
        }
    }

    @Override
    public ApiResult<String> insertFundPassword(String customerNo,String password) {
        Account account=checkAccount(customerNo);
        if(account==null){
            return ApiResult.badParam("交易账号为空");
        }
        //生成资金密码
        String salt=MD5Util.getRandomSalt(10);
        account.setSalt(salt);
        account.setPassword(MD5Util.md5Hex(password,salt));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public void updateWithdrawTiming(String customerNo) {
        accountMapper.updateWithdrawTiming(customerNo);
    }

    @Override
    public List<AccountInfo> allAccountList() {
        Condition condition = new Condition(Account.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        return BeanUtil.copyProperties(findByCondition(condition),AccountInfo.class);
    }

    @Override
    public List<CustomerDealOrderVo> selectByCustomerDealOrder() {
        return accountMapper.selectByCustomerDealOrder();
    }

    /**
     * 插入一条冻结资金的流水记录
     *
     * @param frozenAmountDto 冻结的资金,订单ID以及订单类型等
     * @param account        操作修改后的账户
     */
    private void insertRecordFreezingAmount(ChangeAmountDto frozenAmountDto, Account account) {
        RecordFreezingAmount recordFreezingAmount = new RecordFreezingAmount();
        String recordNo=IdWorker.get32UUID();
        recordFreezingAmount.setRecordNo(recordNo);
        //交易商编码
        recordFreezingAmount.setCustomerNo(frozenAmountDto.getCustomerNo());
        //如果
        String change=frozenAmountDto.getReType()==1? "-":"";
        //变动数额
        recordFreezingAmount.setChangeAmount(change+frozenAmountDto.getChangeAmount().setScale(2).toString());
        //冻结资金后的冻结资金数额
        recordFreezingAmount.setBalance(account.getFreezingAmount());
        //交易类型
        recordFreezingAmount.setTradeType(frozenAmountDto.getTradeType());
        //订单号
        recordFreezingAmount.setOrderNo(frozenAmountDto.getOrderNo());
        //冻结类型 1：冻结，2：解冻
        recordFreezingAmount.setRetype(frozenAmountDto.getReType());
        recordFreezingAmount.setCreateTime(new Date());
        recordFreezingAmount.setModifyTime(new Date());
        //是否有效
        recordFreezingAmount.setFlag(new Byte("1"));
        recordFreezingAmountMapper.insertSelective(recordFreezingAmount);
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
