package com.baibei.hengjia.common.tool.constants;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/8 10:20 AM
 * @description:
 */
public final class Constants {
    private Constants() {
    }

    /**
     * 是否有效
     */
    public interface Flag {
        // 有效
        String VALID = "1";
        // 无效
        String UNVALID = "0";
    }

    /**
     * 设备
     */
    public interface Platform {
        String Android = "Android";
        String IOS = "IOS";
        String H5 = "H5";
    }

    /**
     * 常用日期类型
     */
    public interface DATE {
        String YYYYMMDD = "yyyyMMdd";
        String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
        String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD = "yyyy-MM-dd";
    }

    /**
     * 挂摘牌交易方向
     */
    public interface TradeDirection {
        // 买入
        String BUY = "buy";
        // 卖出
        String SELL = "sell";
        //兑换
        String EXCHANGE = "exchange";
        // 退市置换
        String TRANSFER = "transfer";
        //抵扣消费
        String OFFSET = "offset";
    }

    /**
     * 委托单结果
     */
    public interface EntrustOrderResult {
        // 待成交
        String WAIT_DEAL = "wait_deal";
        // 全部成交
        String ALL_DEAL = "all_deal";
        // 部分成交
        String SOME_DEAL = "some_deal";
        // 已撤销
        String REVOKE = "revoke";
    }

    /**
     * 委托单状态
     */
    public interface EntrustOrderStatus {
        // 初始化
        String INIT = "init";
        // 委托成功
        String OK = "ok";
        // 委托失败
        String FAIL = "fail";
    }

    /**
     * 上市商品交易状态
     * 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；trading=交易中；stop=停盘；exit=退市
     */
    public interface ProductMarketTradeStatus {
        String SUBMIT = "submit";
        String WAIT = "wait";
        String TRADING = "trading";
        String STOP = "stop";
        String EXIT = "exit";
    }

    /**
     * 开启关闭状态
     */
    public interface OpenStatus {
        String OPEN = "open";
        String CLOSE = "close";
    }

    /**
     * 持仓商品类型,main=本票,match=配票;send=赠送
     */
    public interface HoldType {
        String MAIN = "main";
        String MATCH = "match";
        String SEND = "send";
    }

    /**
     * 身份 personal=个人 enterprise=企业
     */
    public interface Identity {
        String PERSONAL = "personal";
        String ENTERPRISE = "enterprise";
    }

    /**
     * 用户类型（1：普通用户 2：挂牌商 3：经销商）
     */
    public interface CustomerType {
        String CUSTOMER = "1";
        String MEMBER = "2";
        String DEALERS = "3";
    }

    /**
     * 商品持仓来源 OFFSET=抵扣消费
     */
    public interface HoldResource {
        String DEAL = "deal";
        String PLAN = "plan";
        String EXCHANGE = "exchange";
        // 退市置换
        String TRANSFER = "transfer";
        String OFFSET = "offset";
        String SEND = "send";
    }

    /**
     * 商品是否已扫描（1：已扫描（可卖）；0：未扫描（不可卖））
     */
    public interface HoldScaner {
        String SCANED = "1";
        String UNSCAN = "0";
    }

    /**
     * 提货订单状态(10:待审核(审核中)；20:待发货(审核通过)；30:已发货；40:已收货；50:驳回)
     */
    public interface DeliveryStatus {
        String AUDITING = "10";
        String UNSEND = "20";
        String SENT = "30";
        String RECEIVED = "40";
        String REJECT = "50";
    }

    /**
     * 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    public interface MatchStatus {
        String SUCCESS = "SUCCESS";
        String FAIL = "FAIL";
        String HALF_SUCCESS = "HALF_SUCCESS";
    }


    public interface Status {
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 配票开关(on:开启；off:关闭)
     */
    public interface MatchSwitch {
        String ON = "on";
        String OFF = "off";
    }

    /**
     * 开关类型（tradeMatch:交易配票；deliveryMatch:提货配票；buyMatch:买入配货）
     */
    public interface SwitchType {
        String TRADEMATCH = "tradeMatch";
        String DELIVERYMATCH = "deliveryMatch";
        String BUYMATCH = "buyMatch";
    }

    /**
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:赠送)
     */
    public interface MatchType {
        String BUY_MATCH = "BUY_MATCH";
        String ASSEMBLE_MATCH = "ASSEMBLE_MATCH";
        String GROUP_MATCH = "GROUP_MATCH";
        String SEND = "SEND";
    }

    /**
     * 类型(ADD_MONEY:加钱；DEDUCTING_MONEY:扣钱)
     */
    public interface WithdrawType {
        String ADD_MONEY = "ADD_MONEY";
        String DEDUCTING_MONEY = "DEDUCTING_MONEY";
    }

    /**
     * 签约状态
     */
    public interface SigningStatus {
        String SIGNING_CREATE = "1"; //创建
        String SIGNING_UPDATE = "2"; // 修改
        String SIGNING_DELETE = "3"; //删除
    }

    /**
     * 是否需要校验 交易日  check=校验 ； uncheck="不校验"
     */
    public interface CheckTradeDay {
        String CHECK = "check";
        String UNCHECK = "uncheck";
    }

    /**
     * 签到签退
     */
    public interface SigningInBack {
        String SIGNING_IN = "1";
        String SIGNING_BACK = "2";
    }

    /**
     * 清算日志状态
     */
    public interface CleanLogStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 交易网发起出入金流水对账及会员开销户流水匹配
     */
    public interface PAB1006 {
        String WITHDRAW_DEPOSIT = "1"; //出入金对账文件请求
        String MEMBER = "2"; //开销户流水对账文件
    }

    /**
     * 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致,amount_status_diff=金额和状态都不一致
     */
    public interface DiffType {
        String LONG_DIFF = "long_diff";
        String SHORT_DIFF = "short_diff";
        String AMOUNT_DIFF = "amount_diff";
        String STATUS_DIFF = "status_diff";
        String AMOUNT_STATUS_DIFF = "amount_status_diff";
    }

    /**
     * 差异流水状态，wait=待处理，deal=已处理
     */
    public interface DiffStatus {
        String WAIT = "wait";
        String DEAL = "deal";
    }

    /**
     * 出金状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败 6、提现审核失败)
     */
    public interface WithDrawStatus {
        String APPLYING = "1";
        String APPLY_PASS = "2";
        String DEALING = "3";
        String SUCCESS = "4";
        String FAIL = "5";
        String APPLY_FAIL = "6";

    }

    /**
     * 手续费积分返还办理状态
     */
    public interface AmountReturnStatus {
        String WAIT = "wait";
        String COMPLETED = "completed";
    }

    /**
     * 手续费积分返还办理类型
     */
    public interface AmountReturnType {
        String FEE = "fee"; // 手续费
        String INTEGRAL = "integral";   // 积分
        String CREDIT = "credit";   // 提货款
    }

    /**
     * 清算进度
     * 1:正取批量文件; 2:取批量文件失败  3:正在读取文件; 4:读取文件失败 5：正在处理中; 6：处理完成,但部分成功 7：处理全部失败, 8:处理完全成功 9:处理完成,但生成处理结果文件失败）
     */
    public interface CleanProcessResult {
        String FETCHING = "1";
        String FETCH_FAIL = "2";
        String READING = "3";
        String READ_FAIL = "4";
        String DOING = "5";
        String SOME_SUCCESS = "6";
        String ALL_FAIL = "7";
        String ALL_SUCCESS = "8";
        String ALL_SUCCESS_FILE = "9";
    }

    /**
     * 清算状态
     */
    public interface CleanStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 执行内容编码，sign_in=签到，sign_back=签退，
     * accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
     * clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     */
    public interface CleanFlowPathCode {
        String SIGN_IN = "sign_in";
        String SIGN_BACK = "sign_back";
        String ACCOUNTCHECK = "accountcheck";
        String ACCOUNTCHECK_FILE = "accountcheck_file";
        String AMOUNT_RETURN = "amount_return";
        String CLEAN_FILE = "clean_file";
        String LAUNCH_CLEAN = "launch_clean";
        String CLEAN_PROCESS = "clean_process";
        String CLEAN_RESULT = "clean_result";
    }

    /**
     * 清算流程状态
     */
    public interface CleanFlowPathStatus {
        String WAIT = "wait";
        String COMPLETED = "completed";
    }

    /**
     * 买入配货失败流水状态（deal:已执行；wait:待执行；destory:已失效）
     */
    public interface MatchFailLogStatus {
        String WAIT = "wait";
        String DEAL = "deal";
        String DESTORY = "destory";
    }

    /**
     * 买入配货流水失败类型（balanceLimit：余额不足；authorityLimit：配货权不足；deliveryFail=提货失败）
     */
    public interface MatchFailLogType {
        String BALANCE_LIMIT = "balanceLimit";
        String AUTHORITY_LIMIT = "authorityLimit";
        String DELIVERY_FAIL = "deliveryFail";
    }

    /**
     * 买入配货流水类型：类型（common=正常；offset=补货<前几次配货失败>）
     */
    public interface BuyMatchLogType {
        String COMMON = "common";
        String OFFSET = "offset";
    }

    /**
     * 配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     */
    public interface MatchRule {
        String DELIVERY_MATCH = "deliveryMatch";
        String BUY_MATCH = "buyMatch";
    }

    /**
     * 配货权状态（usable=可用，disabled=不可用）
     */
    public interface MatchAuthorityStatus {
        String USABLE = "usable";
        String DISABLED = "disabled";
    }

    /**
     * 是否执行定时任务标识。on=执行；off=不执行
     */
    public interface ScheduleSwitch {
        String ON = "on";
        String OFF = "off";
    }

    /**
     * (配票规则：match:配票 vouchers:配代金券)
     */
    public interface ticketRule {
        String MATCH = "match";
        String VOUCHERS = "vouchers";
    }

    /**
     * 买入配货用户执行状态（run=已执行过；unrun=未执行过）
     */
    public interface BuyMatchUserStatus {
        String RUN = "run";
        String UNRUN = "unrun";
    }

    public interface CouponPlanDetailStatus {
        String WAIT = "wait";
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    /**
     * 券类型 vouchers=兑换券，deliveryTicket:提货券
     */
    public interface CouponType {
        String VOUCHERS = "vouchers";
        String DELIVERYTICKET = "deliveryTicket";
    }

    /**
     * 失败类型（balanceLimit=余额不足，couponBalanceLimit=券余额不足,dataError=数据异常）
     */
    public interface OffsetFailType {
        String BALANCE_LIMIT = "balanceLimit";
        String COUPONBALANCE_LIMIT = "couponBalanceLimit";
        String DATA_ERROR = "dataError";
    }

    /**
     * 是否执行补货逻辑。offset=执行；unoffset=不执行
     */
    public interface OffsetFlag {
        String OFFSET = "offset";
        String UNOFFSET = "unoffset";
    }


    /**
     * 买入配货执行类型（send=送货；plan=配货）
     */
    public interface BuyMatchOperateType {
        String SEND = "send";
        String PLAN = "plan";
    }

    /**
     * 生效、失效状态
     */
    public interface ValidStatus {
        String VALID = "valid";
        String UNVALID = "unvalid";
    }

    /**
     * 公告类型
     * 类型(mall:商场公告，trade:交易公告)
     */
    public interface PublicNoticeType {
        String MAIL = "mail";
        String TRADE = "trade";
    }

    /**
     * 提货来源。common=用户手动提货,deliveryTicket=提货券，planFirstDelivery=新用户首提（系统自动提），sys=系统自动提货
     */
    public interface DeliverySource {
        String COMMON = "common";
        String DELIVERYTICKET = "deliveryTicket";
        String PLAN_FIRSTDELIVERY = "planFirstDelivery";
        String SYS = "sys";
    }


    /**
     * 是否判断交易日标识。valid=判断；unvalid=不判断
     */
    public interface ValidTradeFlag {
        String VALID = "valid";
        String UNVALID = "unvalid";
    }

    /**
     * 交易卖出持有类型
     */
    public interface SellHoldType {
        String MAIN = "main";
        String MATCH = "match";
        String EXCHANGE = "exchagne";
    }

    /**
     * 交易白名单类型
     */
    public interface TradeWhitelistType {
        String TODAY_SELL = "today_sell";
        String TRANSFER_SELL = "transfer_sell";
    }

    /**
     * 清算规则外的辅助清算表 业务类型。delivery_ticket_offset=提货券补扣
     */
    public interface BusinessType {
        String DELIVERY_TICKET_OFFSET = "delivery_ticket_offset";
    }
}

