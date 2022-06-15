package com.xmut.shoppingcenter.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class OrderUtil {
    public static String getStateName(Integer in){
        final int client = 0;
         final int store = 1;
         final int admin = 2;
        String out;
        switch (in){
            case 0:
                out = "全部";
                break;
            case 1:
                out = "待付款";
                break;
            case 2:
                out = "待发货";
                break;
            case 3:
                out = "待收货";
                break;
            case 4:
                out =  "待评价";
                break;
            case 5:
                out =  "已评价";
                break;
            case 6:
                out = "退款中";  //待发货退款
                break;
            case 7:
                out = "退款中";  //待收货退款
                break;
            case 8:
                out = "退款中"; //已收货退款
                break;
            case 9:
                out = "退款中"; //已收货并评价退款
                break;
            case 10:
                out = "退款成功";
                break;
            case 11:
                out = "禁止退款";
                break;
            default:
                out =  "出错";
                break;
        }
        return out;
    }
    // allclick, waitpay, waitsend, waitreceive, success;
    public static int canceled(){
        return 0;
    }
    public static int waitpay(){
        return 1;
    }
    public static int waitsend(){
        return 2;
    }
    public static int waitreceive(){
        return 3;
    }
    public static int success(){
        return 4;
    }
    public static int successcomment(){
        return 5;
    }
    public static int refundwaitsend(){ return 6;}
    public static int refundwaitreceive(){ return 7;}
    public static int refundafterreceived(){ return 8;}
    public static int refundafterreceivedandcommented(){ return 9;}
    public static int refundsuccess(){ return 10;}

    public static int all() {
        return 0;
    }
    public static String PriceFix(double temp){
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(temp);
    }
    public static double PriceFloorFix(double temp){
        DecimalFormat df = new DecimalFormat("#0.00");

        temp = (double) Math.floor(temp * 100) / 100;
        return temp;
    }
    public static double doubleAdd(double a1,double a2){
        BigDecimal b1 = new BigDecimal(Double.toString(a1));

        return b1.add(new BigDecimal(Double.toString(a2))).doubleValue();
    }
    public static double doubleDivide(double a1,double a2){
        BigDecimal b1 = new BigDecimal(Double.toString(a1));

        return b1.divide(new BigDecimal(Double.toString(a2)),2,BigDecimal.ROUND_FLOOR).doubleValue();
    }
    public static double doublesubtract(double a1,double a2){
        BigDecimal b1 = new BigDecimal(Double.toString(a1));

        return b1.subtract(new BigDecimal(Double.toString(a2))).doubleValue();
    }
    public static double doubleMutiply(double a1,double a2){
        BigDecimal b1 = new BigDecimal(Double.toString(a1));

        return b1.multiply(new BigDecimal(Double.toString(a2))).doubleValue();
    }

}
