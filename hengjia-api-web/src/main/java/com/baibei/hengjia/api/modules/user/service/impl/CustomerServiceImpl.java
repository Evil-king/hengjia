package com.baibei.hengjia.api.modules.user.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import com.baibei.hengjia.api.modules.account.bean.vo.FundInformationVo;
import com.baibei.hengjia.api.modules.account.dao.CustomerIntegralMapper;
import com.baibei.hengjia.api.modules.account.dao.RecordMoneyMapper;
import com.baibei.hengjia.api.modules.account.model.CustomerIntegral;
import com.baibei.hengjia.api.modules.account.model.RecordMoney;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.model.BuymatchLog;
import com.baibei.hengjia.api.modules.match.service.IBuymatchLogService;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.dao.DealOrderMapper;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.user.bean.dto.InviteCodeDto;
import com.baibei.hengjia.api.modules.user.bean.dto.LoginDto;
import com.baibei.hengjia.api.modules.user.bean.dto.RegisterDto;
import com.baibei.hengjia.api.modules.user.bean.dto.UpdatePasswordDto;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerTokenVo;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.InvitationCodeCustomerVo;
import com.baibei.hengjia.api.modules.user.bean.vo.StatisticalCustomerVo;
import com.baibei.hengjia.api.modules.user.common.GenerateCertificateTokenUtil;
import com.baibei.hengjia.api.modules.user.dao.CustomerDetailMapper;
import com.baibei.hengjia.api.modules.user.dao.CustomerMapper;
import com.baibei.hengjia.api.modules.user.dao.CustomerRefMapper;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.model.CustomerDetail;
import com.baibei.hengjia.api.modules.user.model.CustomerRef;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import com.baibei.hengjia.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.*;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: hyc
 * @date: 2019/06/03 14:42:36
 * @description: Customer服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl extends AbstractService<Customer> implements ICustomerService {
    //OSS文件地址（测试为：hjtest,生产为：hjprod）
    @Value("${ossUrl}")
    private String ossUrl;
    //身份证图片存储地址
    @Value("${idcardSavePath}")
    private String savePath;
    //图片前缀地址
    @Value("${pictureUrl}")
    private String pictureUrl;
    //二维码图片存储地址
    @Value("${qrcodeSavePath}")
    private String QrcodeSavePath;
    //二维码中跳转的网址
    @Value("${qrCodeUrl}")
    private String qrCodeUrl;
    //冻结登录所需登录错误次数
    @Value("${errorTimes}")
    private Integer errorTimes;

    @Value("${cash.supAcctId}")
    private String supAcctId; // 资金汇总账号

    // token失效时间
    @Value("${token.expiration}")
    private Long expirationTime;

    // token 刷新时间
    @Value("${token.refresh}")
    private Long refreshExpireTime;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRefMapper customerRefMapper;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IHoldTotalService iHoldTotalService;
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICouponAccountService couponAccountService;

    @Autowired
    private RecordMoneyMapper recordMoneyMapper;

    @Autowired
    private DealOrderMapper dealOrderMapper;

    @Autowired
    private CustomerIntegralMapper customerIntegralMapper;

    @Autowired
    private IDealOrderService dealOrderService;

    @Autowired
    private IProductMarketService productMarketService;

    @Autowired
    private IBuymatchLogService buymatchLogService;
    @Autowired
    private IAutoBlacklistService autoBlacklistService;
    @Autowired
    private IAutoWhiteListService autoWhiteListService;
    @Autowired
    private ITradeWhiteListService tradeWhiteListService;


    @Override
    public ApiResult<CustomerTokenVo> register(RegisterDto registerDto, Customer customer) {
        //生成注册用户对象
        Customer registerCustomer = new Customer();
        registerCustomer.setMobile(registerDto.getMobile());
        String salt = MD5Util.getRandomSalt(10);//获取随机的盐值
        registerCustomer.setCustomerNo(registerDto.getMobile());
        registerCustomer.setSalt(salt);
        registerCustomer.setPassword(MD5Util.md5Hex(registerDto.getPassword(), salt));
        registerCustomer.setRecommenderId(customer.getCustomerNo());//上级推荐人
        registerCustomer.setMemberNo(customer.getMemberNo());
        registerCustomer.setCustomerType(new Byte("1"));
        registerCustomer.setCustomerStatus(CustomerStatusEnum.NORMAL.getCode() + "");//100正常登陆
        //registerCustomer.setQrcode("123");//二维码先暂时不处理
        registerCustomer.setCreateTime(new Date());
        registerCustomer.setModifyTime(new Date());
        registerCustomer.setFlag(new Byte("1"));
        registerCustomer.setUsername(registerDto.getUsername());
        int length = customer.getMemberNo().length();

        String customerNO = null;//生成一个交易商编码
        while (true) {
            //生成交易编码，并循环判断该编码是否重复
            customerNO = customer.getMemberNo() + NoUtil.randomNumbers(10 - length);
            Customer findCustomer = new Customer();
            findCustomer.setCustomerNo(customerNO);
            findCustomer.setFlag(new Byte("1"));
            findCustomer = customerMapper.selectOne(findCustomer);
            if (null == findCustomer) {
                break;
            }
        }
        registerCustomer.setCustomerNo(customerNO);
        customerMapper.insertSelective(registerCustomer);
        //插入一条关系记录表
        CustomerRef customerRef = new CustomerRef();
        customerRef.setMemberNo(registerCustomer.getMemberNo());
        customerRef.setStartTime(new Date());
        customerRef.setRecommenderId(registerCustomer.getRecommenderId());
        customerRef.setCreateTime(new Date());
        customerRef.setCustomerNo(registerCustomer.getCustomerNo());
        customerRef.setFlag(new Byte("1"));
        customerRef.setModifyTime(new Date());
        customerRef.setOperationType(new Byte("1"));
        customerRef.setOperator(0L);
        customerRefMapper.insertSelective(customerRef);
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setCustomerNo(customerNO);
        customerDetail.setCreateTime(new Date());
        customerDetail.setModifyTime(new Date());
        customerDetail.setFlag(new Byte("1"));
        customerDetailMapper.insertSelective(customerDetail);
        //将用户信息存入redis
        updateRedisUser(registerCustomer);
        CustomerTokenVo customerTokenVo = BeanUtil.copyProperties(registerCustomer, CustomerTokenVo.class);
        //注册成功，生成token相关信息
        CustomerTokenVo token = buildTokenToRedis(registerCustomer.getCustomerNo());
        customerTokenVo.setAccessToken(token.getAccessToken());
        customerTokenVo.setRefreshToken(token.getRefreshToken());
        return ApiResult.success(customerTokenVo);
    }

    @Override
    public ApiResult<CustomerTokenVo> login(Customer customer, LoginDto loginDto, HttpServletRequest request) {
        ApiResult apiResult = new ApiResult();
//        //判断验证码是否正确
//        HttpSession session = request.getSession();
//        if(!loginDto.getVerificationCode().equalsIgnoreCase(((String)session.getAttribute("VerificationCode")))){
//            apiResult.setCode(ResultEnum.LOGIN_VERIFICATION_CODE_ERROR.getCode());
//            apiResult.setMsg(ResultEnum.LOGIN_VERIFICATION_CODE_ERROR.getMsg());
//            return apiResult;
//        }
        //查到信息后通过MD5加盐加密处理进行密码的匹配
        String password = MD5Util.md5Hex(loginDto.getPassword(), customer.getSalt());
        if (!password.equals(customer.getPassword())) {
            //密码匹配失败，调用一次登录失败方法一次
            Integer failCount = increaseFailedLoginCounter(customer.getCustomerNo());
            if (failCount - errorTimes > 0) {
                return ApiResult.error("密码错误次数超过" + errorTimes + "次，请明天再试");
            }
            apiResult.setCode(ResultEnum.USER_PASSWORD_ERROR.getCode());
            apiResult.setMsg(ResultEnum.USER_PASSWORD_ERROR.getMsg());
            return apiResult;
        }
        //将用户信息存入redis
        updateRedisUser(customer);
        //匹配成功，则生成用户登录token以及刷新用户登录token的token，并设置失效时间，
        CustomerTokenVo customerTokenVo = BeanUtil.copyProperties(customer, CustomerTokenVo.class);
        //注册成功，生成token相关信息
        CustomerTokenVo token = buildTokenToRedis(customer.getCustomerNo());
        customerTokenVo.setAccessToken(token.getAccessToken());
        customerTokenVo.setRefreshToken(token.getRefreshToken());
        //登录成功删除用户错误登陆次数信息
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customer.getCustomerNo());
        redisUtil.set(key, "0");
        return ApiResult.success(customerTokenVo);
    }

    /**
     * 修改登录失败次数
     *
     * @param customerNo 交易商编码
     * @return 是否超过限定次数
     */
    public Boolean checkLoginFailCount(String customerNo) {
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customerNo);
        String v = redisUtil.get(key);
        if (org.springframework.util.StringUtils.isEmpty(v)) {
            return false;
        } else if (Integer.valueOf(v) - errorTimes < 0) {
            return false;
        } else {
            redisUtil.set(key, v);
            redisUtil.expireAt(key, getTomorrowZero());
            return true;
        }

    }




    /**
     * 通过手机号判断用户是否存在（已删除的不算）
     *
     * @param mobile 手机号
     * @return 是：存在 否：不存在
     */
    public boolean checkMobileRegistered(String mobile) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("mobile", mobile);
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        if (customerList.size() < 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过customerNo查询用户信息
     *
     * @param customerNo customerNo
     * @return
     */
    @Override
    public CustomerVo findUserByCustomerNo(String customerNo) {
        //先从redis里取用户的信息
//        String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customerNo);
//        Map<String, Object> map = redisUtil.hgetAll(key);
//        if (map.size() != 0) {
//            //将redis里的值转换成map
//            Customer customer = (Customer) MapUtil.mapToObject(map, Customer.class);
//            CustomerVo customerVo = BeanUtil.copyProperties(customer, CustomerVo.class);
//            buildAccountInfo(customerVo, customerNo);
//            return customerVo;
//        }
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        if (customerList.size() < 1) {
            return null;
        }
        CustomerVo customerVo = BeanUtil.copyProperties(customerList.get(0), CustomerVo.class);
        Condition condition1=new Condition(CustomerIntegral.class);
        Example.Criteria criteria1=buildValidCriteria(condition1);
        criteria1.andEqualTo("customerNo",customerNo);
        //积分余额暂时取101类型的积
        criteria1.andEqualTo("integralNo","101");
        List<CustomerIntegral> customerIntegrals = customerIntegralMapper.selectByCondition(condition1);
        if (customerIntegrals.size() < 1) {
            customerVo.setIntegralBalance(BigDecimal.ZERO);
        }else {
            customerVo.setIntegralBalance(customerIntegrals.get(0).getBalance());
        }
        buildAccountInfo(customerVo, customerNo);
        customerVo.setSupAcctId(supAcctId);
        customerVo.setAutoTrade(!autoBlacklistService.isBlackList(customerNo));
        customerVo.setWhiteListFlag(autoWhiteListService.isWhiteList(customerNo));
        customerVo.setTransferFlag("2578".equals(customerNo) ? true :
                tradeWhiteListService.isWhiteList(customerNo,Constants.TradeWhitelistType.TRANSFER_SELL));
        return customerVo;
    }

    /**
     * 获取账户信息
     *
     * @param customerVo
     * @param customerNo
     */
    private void buildAccountInfo(CustomerVo customerVo, String customerNo) {
        BigDecimal marketValue = iHoldTotalService.marketValue(customerNo).getMarketValue();
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(customerNo);
        ApiResult<FundInformationVo> apiResult = accountService.fundInformation(customerNoDto, marketValue);
        if (apiResult.hasSuccess()) {
            FundInformationVo fundInformationVo = apiResult.getData();
            customerVo.setTotalAssets(fundInformationVo.getTotalAssets());
            customerVo.setBalance(fundInformationVo.getBalance());
            customerVo.setFreezingAmount(fundInformationVo.getFreezingAmount());
            customerVo.setHoldMarketValue(fundInformationVo.getHoldMarketValue());
            customerVo.setWithdrawableCash(fundInformationVo.getWithdrawableCash());
            customerVo.setPasswordFlag(fundInformationVo.getPasswordFlag());
        }
        ApiResult<BigDecimal> couponResult=couponAccountService.getByCustomerNo(customerNo,null,Constants.CouponType.VOUCHERS);
        if(apiResult.hasSuccess()){
            customerVo.setCouponBalance(couponResult.getData());
        }
        ApiResult<BigDecimal> Result=couponAccountService.getByCustomerNo(customerNo,null,Constants.CouponType.DELIVERYTICKET);
        if(Result.hasSuccess()){
            customerVo.setDeliveryTicketBalance(Result.getData());
        }
    }

    /**
     * 重置用户密码
     *
     * @param customer 需要重置密码的用户
     * @param password 新密码
     * @return
     */
    @Override
    public ApiResult<String> resetPassword(Customer customer, String password) {
        //重新生成盐值
        String salt = MD5Util.getRandomSalt(10);
        customer.setSalt(salt);
        customer.setPassword(MD5Util.md5Hex(password, salt));
        customer.setModifyTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        //修改redis中用户的信息
        updateRedisUser(customer);
        return ApiResult.success();
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDto
     * @return
     */
    public ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto) {
        ApiResult result = new ApiResult();
        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getRepeatPassword())) {
            return ApiResult.badParam("两次输入新密码不一致");
        }
        Customer customer = findByCustomerNo(updatePasswordDto.getCustomerNo());
        if (customer == null) {
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        String password = MD5Util.md5Hex(updatePasswordDto.getOldPassword(), customer.getSalt());
        if (!password.equals(customer.getPassword())) {
            result.setCode(ResultEnum.USER_PASSWORD_ERRO.getCode());
            result.setMsg(ResultEnum.USER_PASSWORD_ERRO.getMsg());
            return result;
        }
        customer.setPassword(MD5Util.md5Hex(updatePasswordDto.getNewPassword(), customer.getSalt()));
        customer.setModifyTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        //修改redis中用户的信息
        updateRedisUser(customer);
        return ApiResult.success();
    }

    /**
     * 通过手机号查询交易商编码
     *
     * @param mobile
     * @return
     */
    @Override
    public ApiResult<String> findCustomerNoByMobile(String mobile) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("mobile", mobile);
        List<Customer> list = customerMapper.selectByCondition(condition);
        return list.size() < 1 ? ApiResult.error() : ApiResult.success(list.get(0).getCustomerNo());
    }

    /**
     * 通过用户名或者手机号查询用户
     *
     * @param username
     * @return
     */
    @Override
    public Customer checkUsername(String username) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        //是手机号，通过手机号查找交易商编码（直属人ID）
        criteria.andEqualTo("mobile", username);
        criteria.orEqualTo("username", username);
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        return customerList.size() < 1 ? null : customerList.get(0);
    }

    /**
     * 通过手机号查找用户
     *
     * @param mobile
     * @return
     */
    @Override
    public Customer findByMobile(String mobile) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("mobile", mobile);
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        return customerList.size() < 1 ? null : customerList.get(0);
    }

    /**
     * 生成二维码
     *
     * @param customerNoDto
     * @return
     */
    @Override
    public ApiResult<String> createQrCode(CustomerNoDto customerNoDto) {
        StringBuilder stringBuilder = new StringBuilder(qrCodeUrl);
        String qrURl = stringBuilder.toString();
        ApiResult result = new ApiResult();
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNoDto.getCustomerNo());
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        if (customerList.size() < 1) {
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        Customer customer = customerList.get(0);
        if (customer.getQrcode() == null) {
            qrURl = qrURl.replace("InvitationCode", customerNoDto.getCustomerNo());
            qrURl = qrURl.replace("MenberNo", customer.getMemberNo());
            String qrCodeImageUrl = QRcodeUtils.getQRCode(qrURl, QrcodeSavePath, NoUtil.randomNumbers(15), ossUrl);
            customer.setQrcode(qrCodeImageUrl);
            customer.setModifyTime(new Date());
            //修改缓存中的信息
            updateRedisUser(customer);
            customerMapper.updateByCondition(customer, condition);
            return ApiResult.success(pictureUrl + qrCodeImageUrl);
        } else {
            return ApiResult.success(pictureUrl + customer.getQrcode());
        }

    }

    /**
     * 通过邀请码查找所有用户
     *
     * @param inviteCodeDto
     * @return
     */
    @Override
    public MyPageInfo<InvitationCodeCustomerVo> findByInviteCode(InviteCodeDto inviteCodeDto) {
        PageHelper.startPage(inviteCodeDto.getCurrentPage(), inviteCodeDto.getPageSize());
        List<InvitationCodeCustomerVo> list = customerMapper.findByInviteCode(inviteCodeDto);
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
        List<String> statusList=new ArrayList();
        statusList.add("trading");//正在交易
        statusList.add("onmarket");//已上市
        for (int i = 0; i <list.size() ; i++) {
            List<String> productNos=dealOrderService.findTradeProductNo(list.get(i).getCustomerNo(),formatdate.format(new Date()));
            //交易的用户编号
            String tradeFlag="";
            for (int j = 0; j < productNos.size(); j++) {
                List<BuymatchLog> buymatchLogs=buymatchLogService.findByDateAndCustomerAndProductTradeNo(productNos.get(j),new SimpleDateFormat("yyyyMMdd").format(new Date()),list.get(i).getCustomerNo());
                if(buymatchLogs.size()>0){
                    if(j==(productNos.size()-1)){
                        tradeFlag=tradeFlag+productNos.get(j);
                    }else {
                        tradeFlag=tradeFlag+productNos.get(j)+",";
                    }
                }
            }
            list.get(i).setTradeFlag(tradeFlag);
            list.get(i).setMobile(MobileUtils.changeMobile(list.get(i).getMobile()));
        }
        MyPageInfo<InvitationCodeCustomerVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    /**
     * 实名认证
     *
     * @param
     * @return
     */
    @Override
    public ApiResult authentication(String customerNo, String realname, String idcard, MultipartFile frontFile, MultipartFile backFile) {
        ApiResult result = new ApiResult();
        Customer customer = findByCustomerNo(customerNo);
        if (customer == null) {
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        String idcardFront = null;
        String idcardBack = null;
        try {
            //为身份证正面图片
            idcardFront = ImageUtils.setImagge(frontFile.getInputStream(), savePath, customerNo + "idcardFront");
            //为身份证反面图片
            idcardBack = ImageUtils.setImagge(backFile.getInputStream(), savePath, customerNo + "idcardBack");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setCustomerNo(customerNo);
        customerDetail.setIdcard(idcard);
        customerDetail.setRealname(realname);
        customerDetail.setIdcardBack(idcardBack);
        customerDetail.setIdcardFront(idcardFront);
        customerDetail.setCreateTime(new Date());
        customerDetail.setModifyTime(new Date());
        customerDetail.setFlag(new Byte("1"));
        Integer insertResult = customerDetailMapper.insertSelective(customerDetail);
        if (insertResult == 0) {
            ApiResult.error("身份认证失败！");
        }
        return ApiResult.success();
    }

    /**
     * 退出登录
     *
     * @param customerNo
     */
    @Override
    public ApiResult<String> exitLogin(String customerNo) throws ServiceException {
        Customer customer = findByCustomerNo(customerNo);
        if (customer == null) {
            return ApiResult.error("用户不存在");
        }
        deleteToken(customerNo);
        return ApiResult.success();
    }

    /**
     * 通过openid查询用户相关信息
     *
     * @param openid
     * @return
     */
    @Override
    public CustomerVo selectByOpenid(String openid) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("openid", openid);
        List<Customer> customers = customerMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(customers)) {
            return null;
        }
        CustomerVo customerVo = BeanUtil.copyProperties(customers.get(0), CustomerVo.class);
        return customerVo;
    }

    @Override
    public StatisticalCustomerVo getCustomerList(String customerNo,String date) {
        //通过递归找到名下所有用户
        List<Customer> customers=findAllCustomer(customerNo,date);
        //传入的那个用户
        Customer customer=findByCustomerNo(customerNo);
        customers.add(customer);
        SimpleDateFormat formatdate = new SimpleDateFormat("YYYY-MM-dd");//日期算换格式
        //总充值
        BigDecimal totalRecharge=BigDecimal.ZERO;
        //当日充值
        BigDecimal todayRecharge=BigDecimal.ZERO;
        //总注册人数=总人数
        Integer totalRegist=customers.size();
        //当天注册人数
        Integer todayRegist=0;
        //未充值人数
        Integer withoutRecharge=0;
        //充值人数
        Integer rechargeAmount=0;
        //总提现
         BigDecimal totalWithdraw=BigDecimal.ZERO;
        //当日提现
         BigDecimal todayWithdraw=BigDecimal.ZERO;
         //有效人数
        Integer effectiveAmount=0;
        //总成交手数
        Integer totalDealCount=0;
        for (int i = 0; i <customers.size() ; i++) {
            //充值
            List<RecordMoney> recordMoneyList=recordMoneyMapper.selectByCustomerNoAndDate(customers.get(i).getCustomerNo(),"101",date);
            if(recordMoneyList.size()>0){
                for (int j = 0; j <recordMoneyList.size() ; j++) {
                    //changeAmount有时候可能加了-号，需要删掉
                    //总充值增加
                    totalRecharge=totalRecharge.add(new BigDecimal(recordMoneyList.get(j).getChangeAmount().replace("-","")));
                    if(formatdate.format(recordMoneyList.get(j).getModifyTime()).equals(date)){
                        //判断该时间是否为当日日期
                        //当日充值增加
                        todayRecharge=todayRecharge.add(new BigDecimal(recordMoneyList.get(j).getChangeAmount().replace("-","")));
                    }
                }
                if (recordMoneyList.size()==1&&(new BigDecimal(recordMoneyList.get(0).getChangeAmount()).compareTo(BigDecimal.ZERO)==0)){
                    //如果有充值记录并且充值记录只有一条且充值数额为0，则未充值人数+1
                    withoutRecharge++;
                }
            }else{
                //没有充值记录，未充值人数+1
                withoutRecharge++;
            }
            //提现
            List<RecordMoney> withDrawList=recordMoneyMapper.selectByCustomerNoAndDate(customers.get(i).getCustomerNo(),"102",date);
            if(withDrawList.size()>0){
                for (int j = 0; j < withDrawList.size(); j++) {
                    //changeAmount提现加了-号，需要删掉
                    //总提现增加
                    totalWithdraw=totalWithdraw.add(new BigDecimal(withDrawList.get(j).getChangeAmount().replace("-","")));
                    if(formatdate.format(withDrawList.get(j).getModifyTime()).equals(date)){
                        //判断该时间是否为当日日期
                        //当日提现增加
                        todayWithdraw=todayWithdraw.add(new BigDecimal(withDrawList.get(j).getChangeAmount().replace("-","")));
                    }
                }
            }
            //判断当日注册人数
            if(formatdate.format(customers.get(i).getCreateTime()).equals(date)){
                todayRegist++;
            }
            CustomerVo customerVo=findUserByCustomerNo(customers.get(i).getCustomerNo());
            if(customerVo.getTotalAssets().compareTo(new BigDecimal(2000))>=0){
                effectiveAmount++;
            }
            Integer count=dealOrderMapper.selectCountByDate(customers.get(i).getCustomerNo(),date);
            totalDealCount=totalDealCount+count;
        }
        rechargeAmount=totalRegist-withoutRecharge;
        StatisticalCustomerVo statisticalCustomerVo=new StatisticalCustomerVo();
        //总注册人数
        statisticalCustomerVo.setTotalRegist(totalRegist);
        //当天注册人数
        statisticalCustomerVo.setTodayRegist(todayRegist);
        //总充值
        statisticalCustomerVo.setTotalRecharge(totalRecharge);
        //当天充值
        statisticalCustomerVo.setTodayRecharge(todayRecharge);
        //总提现
        statisticalCustomerVo.setTotalWithdraw(totalWithdraw);
        //当天提现
        statisticalCustomerVo.setTodayWithdraw(todayWithdraw);
        //未充值人数
        statisticalCustomerVo.setWithoutRecharge(withoutRecharge);
        //充值人数
        statisticalCustomerVo.setRechargeAmount(rechargeAmount);
        //有效人数
        statisticalCustomerVo.setEffectiveAmount(effectiveAmount);
        statisticalCustomerVo.setTotalDealCount(totalDealCount);
        return statisticalCustomerVo;
    }

    @Override
    public String getMemberCustomerNoByProductNo(String productTradeNo) {
        return customerMapper.getMemberCustomerNoByProductNo(productTradeNo);
    }

    @Override
    public String getDistributorNo() {
        Condition condition=new Condition(Customer.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerType",3);
        List<Customer> customerList=customerMapper.selectByCondition(condition);
        if (customerList.size()==1) {
            return customerList.get(0).getCustomerNo();
        }else if(customerList.size()>1){
            throw  new ServiceException("Distributor should select one but more");
        }else {
            return null;
        }
    }

    @Override
    public int updateByCustomer(Customer customer) {
        return customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 递归算法获取名下所有用户
     * @param customerNo
     * @return
     */
    private List<Customer> findAllCustomer(String customerNo,String date) {
        List<Customer> result=new ArrayList<>();
        List<Customer> customerList=customerMapper.selectByrecommenderIdAndDate(customerNo,date);
        if(customerList.size()>0){
            for (int i = 0; i < customerList.size(); i++) {
                List<Customer> oneResult=findAllCustomer(customerList.get(i).getCustomerNo(),date);
                if(oneResult.size()>0){
                    result.addAll(oneResult);
                }
            }
            result.addAll(customerList);
        }else {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * 删除用户token信息
     *
     * @param customerNo
     */
    private void deleteToken(String customerNo) {
        String redisKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
        String userToken = (String) redisUtil.hmget(redisKey, "accessToken");
        String tokenCodeKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, userToken);
        redisUtil.delete(tokenCodeKey);
        redisUtil.delete(redisKey);
    }

    /**
     * 登录次数错误+1
     *
     * @param customerNo 交易商编号
     * @return 登录错误次数
     */
    private Integer increaseFailedLoginCounter(String customerNo) {
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customerNo);
        String v = redisUtil.get(key);
        if (org.springframework.util.StringUtils.isEmpty(v)) {
            redisUtil.set(key, "1");
            redisUtil.expireAt(key, getTomorrowZero());
            return 1;
        } else {
            v = Integer.valueOf(v) + 1 + "";
            redisUtil.set(key, v);
            return Integer.valueOf(v) + 1;
        }
    }

    /**
     * 将用户的token存入redis
     *
     * @param customerNo 交易商编码
     */
    public CustomerTokenVo buildTokenToRedis(String customerNo) {
        //登录token
        String accessToken = GenerateCertificateTokenUtil.accessToken();
        //刷新token
        String refreshToken = GenerateCertificateTokenUtil.refreshToken();
        //登录token生效时间
        /*long accessTokenExpireTime = GenerateCertificateTokenUtil.accessTokenExpireTime();*/
        long accessTokenExpireTime = GenerateCertificateTokenUtil.accessTokenExpireTime(expirationTime);
        //刷新token生效时间
        long refreshTokenExpireTime = GenerateCertificateTokenUtil.refreshTokenExpireTime(refreshExpireTime);

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        map.put("accessTokenExpireTime", String.valueOf(accessTokenExpireTime));
        map.put("refreshTokenExpireTime", String.valueOf(refreshTokenExpireTime));
        String redisKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
        redisUtil.hsetAll(redisKey, map);
        // 缓存token与customerNo的对应关系
        String tokenCodeKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, accessToken);
        redisUtil.set(tokenCodeKey, customerNo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(format.format(accessTokenExpireTime));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("时间戳转换失败");
        }
        redisUtil.expireAt(tokenCodeKey, date);
        CustomerTokenVo tokenVo = new CustomerTokenVo();
        tokenVo.setAccessToken(accessToken);
        tokenVo.setRefreshToken(refreshToken);
        return tokenVo;
    }

    /**
     * 修改存在redis中用户的信息
     *
     * @param customer 用户实体
     */
    public void updateRedisUser(Customer customer) {
        String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customer.getCustomerNo());
        Map<String, Object> map = MapUtil.objectToMap(customer);
        redisUtil.hsetAll(key, map);
    }

    /**
     * 获取明天0点
     *
     * @return
     */
    private Date getTomorrowZero() {
        long current = System.currentTimeMillis();//当前时间毫秒数
        long zero = (current + 24 * 60 * 60 * 1000) / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//明天零点零分零秒的毫秒数
        return new Date(zero);
    }

    @Override
    public boolean updateOpenid(String customerNo, String openId) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("客户编码不能为空");
        }
        Customer customer = findByCustomerNo(customerNo);
        if (customer == null) {
            throw new ServiceException("客户编码不存在");
        }
        if (StringUtils.isEmpty(customer.getOpenid()) || !openId.equals(customer.getOpenid())) {
            customer.setModifyTime(new Date());
            customer.setOpenid(openId);
            //修改缓存中的信息
            updateRedisUser(customer);
            return customerMapper.updateByPrimaryKeySelective(customer) > 0;
        }
        return true;
    }


    @Override
    public Customer findByCustomerNo(String customerNo) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("客户编码不能为空");
        }
        //先从redis里取用户的信息
//        String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customerNo);
//        Map<String, Object> map = redisUtil.hgetAll(key);
//        if (map.size() != 0) {
//            //将redis里的值转换成map
//            Customer customer = (Customer) MapUtil.mapToObject(map, Customer.class);
//            return customer;
//        }
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<Customer> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
