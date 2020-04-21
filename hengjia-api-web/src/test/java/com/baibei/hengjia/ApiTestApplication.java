package com.baibei.hengjia;

import com.baibei.hengjia.api.ApiApplication;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAndIntegralAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeIntegralDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawTempDto;
import com.baibei.hengjia.api.modules.cash.component.BankBackMessageAnalysis;
import com.baibei.hengjia.api.modules.cash.component.BankMessageSplice;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.IValidateService;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.Utils;
import com.baibei.hengjia.api.modules.product.service.IProductService;
import com.baibei.hengjia.api.modules.shop.service.IShpOrderService;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.sms.dao.PubSmsMapper;
import com.baibei.hengjia.api.modules.sms.service.IPubSmsService;
import com.baibei.hengjia.api.modules.sms.util.SmsUtil;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.IntegralTradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author hwq
 * @date 2019/06/04
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApiApplication.class)
@AutoConfigureMockMvc
public class ApiTestApplication {

    @Autowired
    private IPubSmsService pubSmsService;
    @Autowired
    IProductService productService;
    @Autowired
    IShpOrderService orderService;
    @Autowired
    PubSmsMapper pubSmsMapper;
    @Autowired
    BankMessageSplice bankMessageSplice;
    @Autowired
    BankBackMessageAnalysis bankBackMessageAnalysis;
    @Autowired
    PropertiesVal propertiesVal;
    @Autowired
    IOrderWithdrawService orderWithdrawService;
    @Autowired
    IValidateService validateService;
    @Value("11:00:00")
    private String withdrawTime;
    @Value("09:30:00")
    private String depositTime;
    @Autowired
    SmsUtil smsUtil;
    @Autowired
    ITradeDayService tradeDayService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private IAccountService accountService;

    @Before
    public void setUp() throws Exception{
        //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    }


    @Test
    public void Test01() {


//        boolean tradeDay = tradeDayService.isTradeDay(new Date());
//        System.out.println(tradeDay);


//        BigDecimal amount = new BigDecimal(10000);
//        String amountAndFee = Utils.getAmountAndFee(amount, propertiesVal.getRate(), propertiesVal.getFee());
//        System.out.println("amountAndFee=" + amountAndFee);


        /**
         * 查询临时出金银行信息接口
         */
        OrderWithdrawTempDto orderWithdrawTempDto = new OrderWithdrawTempDto();
        orderWithdrawTempDto.setAccountBank("招商银行");
        orderWithdrawTempDto.setBranchBank("广州支行");
        orderWithdrawTempDto.setAmount(new BigDecimal(20));
        orderWithdrawTempDto.setBankNum("11111111");
        orderWithdrawTempDto.setBankAccount("胡文卿");
        orderWithdrawTempDto.setCustomerNo("0017810783");
//        log.info("withdrawInfo={}", withdrawInfo);
//        ApiResult apiResult = tempWithdrawservice.withdrawInfo(orderWithdrawTempDto);

        /**
         * 出金接口
         */
        OrderWithdrawDto orderWithdrawDto = new OrderWithdrawDto();
//        orderWithdrawDto

//        ApiResult withdraw = orderWithdrawService.withdrawApplicationApplication(orderWithdrawDto);
//        log.info("withdraw={}", withdraw);

//        orderWithdrawService.findStatusTask();


        /**
         * 判断是否是工作日
         */
//        boolean flag = false;
//        DateFormat format1 = new SimpleDateFormat("HH:mm:ss");
//        Calendar cal = Calendar.getInstance();//使用日历类
//        cal.setTime(new Date());
//        int hour = cal.get(cal.HOUR_OF_DAY);
//        int minute = cal.get(cal.MINUTE);
//        int second = cal.get(cal.SECOND);
//        String hourStr = hour < 10 ? "0" + hour : hour + "";
//        String minuteStr = minute < 10 ? "0" + minute : minute + "";
//        String secondStr = second < 10 ? "0" + second : second + "";
//        String nowTime = hourStr + ":" + minuteStr + ":" + secondStr;
//        String depositTime1 = depositTime;
//        String withdrawTime1 = withdrawTime;
//        Date depositTime =format1.parse(depositTime1);
//        Date withdrawTime =format1.parse(withdrawTime1);
//        Date nowTime1 = format1.parse(nowTime);
//
//        log.info("nowTime={}", nowTime);//10:13:17
//        log.info("depositTime={}", depositTime);//09:30:00
//        log.info("withdrawTime={}", withdrawTime);//15:00:00
//
//
//        if (nowTime1.compareTo(depositTime) == 1 && nowTime1.compareTo(withdrawTime) == -1) {
//            log.info("是工作日");
//        } else {
//            log.info("不是工作日");
//        }


//        PageParam pageParam = new PageParam();
//        pageParam.setCurrentPage(1);
//        pageParam.setPageSize(1);
//        CustomerBaseDto customerBaseDto = new CustomerBaseDto();
//        customerBaseDto.setCustomerNo("3000000001");
//        customerBaseDto.setCustomerId(1);
//        Condition condition1 = new Condition(PubSms.class);
//        Example.Criteria criteria1 = condition1.createCriteria();
//        criteria1.andEqualTo("mobile", "13751882497");
//        List<PubSms> sms = pubSmsMapper.selectByCondition(condition1);
//        log.info("sms={}",sms);
//        if (CollectionUtils.isEmpty(sms)) {
//            log.info("11111111");
//        } else {
//            log.info("22222222");
//        }

        /**
         * 短信
         */
        String phone = "13755588979";
        //发送短信并且入库
//        String code = RandomUtils.getRandomNumber(6);
//        String code = "fox,131014241,NB0001";
//        String sms = smsUtil.operatorSms("3", "1", code, phone);
//        System.out.println(sms);
//        ApiResult apiResult = pubSmsService.getSms("13755588979", sms);
//        if(apiResult.hasSuccess()){
//            pubSmsService.writeToDb("13755588979", code);
//        }
//        ApiResult apiResult = pubSmsService.getSms(phone, 3, "1", new String[]{"fox,131000258,NB0001"});
//        ApiResult apiResult = pubSmsService.getSms("18664624999", 2, "1");
//        log.info("apiResult={}", apiResult);

//        String phone = "13755588979";
//        String code = "175482";
//        ApiResult apiResult = pubSmsService.validateCode(phone,code);
//        log.info("apiResult={}",apiResult);

        /**
         * 1318出金接口
         */
//        OrderWithdrawDto orderWithdrawDto = new OrderWithdrawDto();
//        orderWithdrawDto.setBankName("招商银行");
//        orderWithdrawDto.setOrderAmt(new BigDecimal(500));
//        orderWithdrawDto.setPassword("123456");
//        orderWithdrawDto.setReceiveAccount("6214835710575893");
//        orderWithdrawDto.setCustomerNo("6665364803");
//        orderWithdrawService.withdrawApplicationApplication(orderWithdrawDto);


//        Map<String, String> ressult = bankBackMessageAnalysis.parsingTranMessageString(message_1010);
//        ApiResult apiResult = new ApiResult();
//        String tranFunc = ressult.get("TranFunc");
//        if ("000000".equals(ressult.get("RspCode"))) {
//            String array = ressult.get("Array");
//            String[] arr = array.split("&");
//            for (int j = 0; j < arr.length; j += 11) {
//                MemberBalanceVo memberBalanceVo = new MemberBalanceVo();
//                memberBalanceVo.setCustAcctId(arr[j]);
//                memberBalanceVo.setCustFlag(arr[j + 1]);
//                memberBalanceVo.setCustType(arr[j + 2]);
//                memberBalanceVo.setCustStatus(arr[j + 3]);
//                memberBalanceVo.setThirdCustId(arr[j + 4]);
//                memberBalanceVo.setMainAcctId(arr[j + 5]);
//                memberBalanceVo.setCustName(arr[j + 6]);
//                memberBalanceVo.setTotalAmount(new BigDecimal(arr[j + 7]));
//                memberBalanceVo.setTotalBalance(new BigDecimal(arr[j + 8]));
//                memberBalanceVo.setTotalFreezeAmount(new BigDecimal(arr[j + 9]));
//                memberBalanceVo.setTranDate(arr[j + 10]);
//                apiResult.setData(memberBalanceVo);
//            }
//        }


        /**
         * 积分商城首页商品数据
         */
//        MyPageInfo<ShopHomeVO> homeData = productService.getHomeData(pageParam, customerBaseDto);
//        log.info("homeData={}",homeData);

        /**
         * 积分商城首页用户信息
         */
//        IntegralDetailVo userInfo = orderService.getUserInfo(customerBaseDto);
//        log.info("userInfo={}",userInfo);

        /**
         * 立即兑换
         */
//        ExchangePointDTO exchangePointDTO = new ExchangePointDTO();
//        exchangePointDTO.setCustomerId(1);
//        exchangePointDTO.setCustomerNo("3000000001");
//        exchangePointDTO.setSunPoint("600");
//
//        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
//        orderDetailsDTO.setNum("2");
//        orderDetailsDTO.setSellPrice("100");
//        orderDetailsDTO.setSpuNo("NB001");
//
//        OrderDetailsDTO orderDetailsDTO1 = new OrderDetailsDTO();
//        orderDetailsDTO1.setNum("2");
//        orderDetailsDTO1.setSellPrice("200");
//        orderDetailsDTO1.setSpuNo("NB002");
//
//        List<OrderDetailsDTO> list = Lists.newArrayList();
//        list.add(orderDetailsDTO);
//        list.add(orderDetailsDTO1);
//        exchangePointDTO.setOrderDetailsDTOList(list);
//
//        orderService.exchangePoint(exchangePointDTO);


        /**
         * 兑换列表
         */
//        MyPageInfo<OrderListVO> list = orderService.exchangeList(pageParam, customerBaseDto,"2019-06-03 00:00:00","2019-06-09 00:00:00");
//        log.info("list={}",list);
//        String array = "19071209471936961840&18121620919695&2&0&11.00&888800120689910&0011554708&20190712&20190712";
//        String[] arr = array.split("&");
//        String msg = "A0010301019203                0000000161000000PA001022019071209474019071209470863949695ERR020无符合条件记录                                                                                      00000             00000000000132502                20190712094740ERR020无符合条件记录                            000000039PA001190712094708639496959203&&&0&19071209470863949695&&&&0.00&&&&&&";
//        String msg1 = "A0010301019203                0000000222000000PA001022019071209474019071209471936961840000000交易成功                                                                                            00000             00000000000132502                20190712094740000000交易成功                                  000000100PA0011907120947193696184092031&1&1&1&19071209471936961840&18121620919695&2&0&11.00&888800120689910&0011554708&20190712&20190712&&";
//        BankBackMessageAnalysis bm = new BankBackMessageAnalysis();// 解析返回报文类
//        Map<String, String> retKeyDict = bm.parsingTranMessageString(msg1);
//        log.info("retKeyDict={}", JSONObject.toJSONString(retKeyDict));

        /**
         * 对比出金金额
         */
//        String customerNo = "0018936562";
//        BigDecimal amount = new BigDecimal(500);
//        boolean flag = validateService.compairAmount(customerNo, amount);
//        System.out.println("flag:" + flag);


        /**
         * 资金接口
         */
        ChangeCouponAndIntegralAmountDto changeCouponAndIntegralAmountDto = new ChangeCouponAndIntegralAmountDto();
        changeCouponAndIntegralAmountDto.setCustomerNo("0015670994");
        //积分对象
        ChangeIntegralDto changeIntegralDto = new ChangeIntegralDto();
        changeIntegralDto.setIntegralNo(101L);
        changeIntegralDto.setChangeAmount(new BigDecimal(25000));
        changeIntegralDto.setCustomerNo("0015670994");
        changeIntegralDto.setOrderNo("11111111111");
        changeIntegralDto.setReType((byte)1);
        changeIntegralDto.setTradeType((byte)IntegralTradeTypeEnum.EXCHANGE_INTEGRAL_PRODUCT.getCode());
        changeCouponAndIntegralAmountDto.setChangeIntegralDto(changeIntegralDto);
        //券对象
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCouponType("deliveryTicket");
        changeCouponAccountDto.setProductTradeNo("002");
        changeCouponAccountDto.setChangeAmount(new BigDecimal(11600));
        changeCouponAccountDto.setOrderNo("2222222222222222222");
        changeCouponAccountDto.setReType((byte)1);
        changeCouponAccountDto.setCustomerNo("0015670994");
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.OFFSET_CONSUME.getCode());
        changeCouponAndIntegralAmountDto.setChangeCouponAccountDto(changeCouponAccountDto);

        accountService.changeCouponAndIntegralAmount(changeCouponAndIntegralAmountDto);

    }

    @Test
    public void test02() throws Exception{
       String result =  mockMvc.perform
               (MockMvcRequestBuilders.post("/api/advisory/add")
                .content("{\"bannerTitle\":}")
//                .param("bannerTitle", "开始测试")
//                .param("bannerSort","1")
//                .param("bannerImage","boy.jpg")
//                .param("bannerUrlType","inner")
//                .param("bannerUrl","http://www.baidu.com")
//                .param("bannerContent","你好")
//                .param("bannerDisplay","show")
        .contentType(MediaType.APPLICATION_JSON))
//               .andExpect(content())
               .andReturn().getResponse().getContentAsString();
        System.out.println("result ：" + result);

    }

    public static Date getFetureDate() {
        try {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 14);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result = format.format(today);
            System.out.println(result);
//        long date=format.parse(result).getTime();
        Date date=format.parse(result);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2-day1));
            return day2-day1;
        }
    }

    public static int daysBetween(Date smdate,Date bdate) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        smdate=sdf.parse(sdf.format(smdate));
        bdate=sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        log.info("time1={},time2={}",time1,time2);
        long between_days=(time1-time2)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days).replace("-", ""));
    }


    public static int getDistanceTime(Date startTime, Date endTime) {
        int days = 0;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();

        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        days = (int) (diff / (24 * 60 * 60 * 1000));
        return days;
    }

    public static int getDiffDays(Date beginDate, Date endDate) {
        if(beginDate==null||endDate==null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff=(endDate.getTime()-beginDate.getTime())/(1000*60*60*24);
        int days = new Long(diff).intValue();
        return days;
    }

    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
            System.out.println(date);
            System.out.println(mydate);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }


    public static void main(String[] args) throws ParseException {

//        long fetureDate = ApiTestApplication.getFetureDate();
        Date fetureDate = ApiTestApplication.getFetureDate();
//        System.out.println("fetureDate="+fetureDate);

//        long nowTime = System.currentTimeMillis();
        Date nowTime = new Date();
//        System.out.println(nowTime);

//
//        int days = ApiTestApplication.differentDays(nowTime,fetureDate);
//        System.out.println(days);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=sdf.parse("2019-07-22 17:45:36");
        Date d2=sdf.parse("2019-08-05 13:02:34");
//        String d1 = "2019-07-23";
//        String d2 = "2019-08-05";
//        System.out.println(daysBetween(d1,d2));


        String dTime = "08:30:00";
        String wTime = "21:30:00";
        boolean flag = Utils.compareTime(dTime,wTime);
        System.out.println(flag);






//        int days = (int) ((fetureDate - nowTime) / (1000*3600*24));
//        System.out.println(days);


//        string content = "尊敬的{0}您好，您的电话是{1}，你的会员编号是{2}。";
//        string str = "fox,131000258,nb0001";
//        string[] arr = new string[]{str};
//        content = messageformat.format(content, arr);
//        system.out.println(content);

//        boolean flag = false;
//        if(!flag){
//            system.out.println("1111");
//        } else {
//            system.out.println("222");
//        }

//        calendar calendar = calendar.getinstance();
//        calendar.settime(new date());
//        calendar.set(calendar.hour_of_day, 0);
//        calendar.set(calendar.minute, 0);
//        calendar.set(calendar.second, 0);
//        date time = calendar.gettime();
//        system.out.println("time:" + time);


//        string str = "18070900000021&18031551826869&2&0&0.01&888800117510412&6660000012&20180709&20180315&18070900000018&18031551826471&2&0&11.22&888800117510402&6660000010&20180709&20180315&18070900000038&18031551842235&2&0&0.99&888800117510412&6660000012&20180709&20180315&18070900000015&18031551826467&1&0&100000.00&888800117510402&6660000010&20180709&20180315&18070900000020&18031551826863&1&0&100000.00&888800117510412&6660000012&20180709&20180315";
//        string[] arr = str.split("&");
//        for (int i = 0; i < arr.length; i+=9) {
//            system.out.println(arr[i]);
//            system.out.println(arr[i + 1]);
//            system.out.println(arr[i + 2]);
//            system.out.println(arr[i + 3]);
//            system.out.println(arr[i + 4]);
//            system.out.println(arr[i + 5]);
//            system.out.println(arr[i + 6]);
//            system.out.println(arr[i + 7]);
//            system.out.println(arr[i + 8]);
//            system.out.println("---------------------------");
//        }
    }
}
