package com.baibei.hengjia.api.modules.cash.withdrawProsscess;

import com.baibei.hengjia.common.tool.utils.BigDecimalUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hwq
 * @date 2019/06/13
 */
public class Utils {

    /**
     * 计算出金金额(包含手续费)
     * 公式:Max(2,0.006)
     * 资金乘以0.006的值如果比2小就取2，大的话就取本身
     *
     * @param amount
     * @return
     */
    public static String getAmountAndFee(BigDecimal amount, String rate, BigDecimal fee) {
        BigDecimal countFee = BigDecimalUtil.multiplyPercent2(rate, amount);
        System.out.println("countFee=" + countFee);
        if (countFee.compareTo(fee) > 0) {
            fee = countFee;
        }
        String result = BigDecimalUtil.add(amount.toString(), fee.toString());
        return result;
    }

    /**
     * 计算出金金额手续费
     *
     * @param amount
     * @return
     */
    public static BigDecimal getFee(BigDecimal amount, String rate, BigDecimal fee) {
        BigDecimal countFee = BigDecimalUtil.multiplyPercent2(rate, amount);
        if (countFee.compareTo(fee) > 0) {
            return fee = countFee;
        }
        return fee;
    }

    /**
     * 根据银行给过来的出金金额反推用户在页面上填写的提现金额数
     * @param bankAmount 银行给过来的出金金额
     * @param rate 手续费率
     * @param minFee 最低手续费
     * @return
     */
    public static BigDecimal getWithdrawAmount(BigDecimal bankAmount,String rate,BigDecimal minFee){
        if (BigDecimal.ONE.compareTo(new BigDecimal(rate)) < 0
                || BigDecimal.ZERO.compareTo(new BigDecimal(rate)) >= 0) {
            throw new IllegalArgumentException("decimal must in [0,1]");
        }
        //临界值(向下取两位小数)
        BigDecimal cValue = minFee.divide(new BigDecimal(rate), 2, BigDecimal.ROUND_DOWN);
        BigDecimal fee;
        BigDecimal withdrawAmount;
        if(bankAmount.add(minFee).compareTo(cValue)<=0){//按最低手续费收取
            fee = minFee;
            withdrawAmount = bankAmount.add(fee);
        }else{
            withdrawAmount=bankAmount.divide(new BigDecimal("1").subtract(new BigDecimal(rate)),2,BigDecimal.ROUND_UP);
        }
        return withdrawAmount;
    }

    /**
     * 判断是否是出金时间
     *
     * @return
     */
    public static boolean compareTime(String dTime, String wTime) {
        boolean flag = false;
        try {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            Calendar cal = Calendar.getInstance();//使用日历类
            cal.setTime(new Date());
            int hour = cal.get(cal.HOUR_OF_DAY);
            int minute = cal.get(cal.MINUTE);
            int second = cal.get(cal.SECOND);
            String hourStr = hour < 10 ? "0" + hour : hour + "";
            String minuteStr = minute < 10 ? "0" + minute : minute + "";
            String secondStr = second < 10 ? "0" + second : second + "";
            String nowTime = hourStr + ":" + minuteStr + ":" + secondStr;
            String depositTime1 = dTime;
            String withdrawTime1 = wTime;
            Date depositTime = format.parse(depositTime1);
            Date withdrawTime = format.parse(withdrawTime1);
            Date nowTime1 = format.parse(nowTime);
            if (nowTime1.compareTo(depositTime) == 1 && nowTime1.compareTo(withdrawTime) == -1) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;

    }

    public static void main(String[] args) {
        BigDecimal amount = new BigDecimal(423.36);
        String amountAndFee = Utils.getAmountAndFee(amount,"0.006",new BigDecimal(2));
        System.out.println("amountAndFee=" + amountAndFee);
    }
}
