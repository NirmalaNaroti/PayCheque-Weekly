package com.fragma.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainDto {

    static Logger LOG = LoggerFactory.getLogger(MainDto.class);

    public Map<Integer, PayCheque> map = new HashMap<>();
    public Map<String, SummaryHtml> summaryMap = new HashMap<>();

    public Set<String> maptoset = new LinkedHashSet<>();

    public LocalDate todayDate;
   public LocalDate lastWeek;

    public LocalDate getTodayDate() {
        return todayDate;
    }

    public String subDate(){
        LocalDate yesturday=todayDate;
        String date = yesturday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate lastWeekDay=lastWeek;
        String lastWeekdate = lastWeekDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return lastWeekdate+ " and "+date;

    }
    public String subDate1(){
        LocalDate yesturday=todayDate;
        String date = yesturday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate lastWeekDay=lastWeek;
        String lastWeekdate = lastWeekDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return date+ " and "+lastWeekdate;

    }

    public void setTodayDate(LocalDate todayDate) {
        this.todayDate = todayDate;
         this.lastWeek = todayDate.minusDays(6);
    }
    public void setTodayDate1(LocalDate todayDate) {

        this.todayDate = todayDate.minusMonths(1).withDayOfMonth(1);
        this.lastWeek = todayDate.minusMonths(1).withDayOfMonth(todayDate.minusMonths(1).lengthOfMonth());
    }

    public Map<Integer, PayCheque> getMap() {
        return map;
    }

    public void addAccountNumberToSet(String accountNumber) {
        System.out.println("AccountNumber:"+accountNumber);
        maptoset.add(accountNumber);
    }

    public Set<String> getMaptoset() {
        return maptoset;
    }

    public SummaryHtml getPayCheque(String accountNumber) {
        if (summaryMap.get(accountNumber) != null) {
            return summaryMap.get(accountNumber);
        }
        else

            return new SummaryHtml();
    }


    public String doubleToString(double amount)
    {
        if(Double.compare(amount,0.0D)==0)
        {
            return "0.00";
        }
        else {
            DecimalFormat df = new DecimalFormat("#,###.00"); // or pattern "###,###.##$"
            return df.format(amount);
        }
    }


    public String isNullOrEmpty(String  value)
    {
        if(value == null || value.isEmpty() || value.equals("\"\""))
        {
            return "-";
        }
        else
            return  value;
    }



    public void populateData(int SLNo,String account_number,String rm_name,String account_currency,String account_class,String account_title, String trans_date,String trn_ref_no,String checker_id,String return_cheque_no,String return_cheque_amount,String return_reason,String narration) {

        PayCheque payCheque = map.get(SLNo);

        if (payCheque == null) {
            payCheque = new PayCheque();
        }

       payCheque.setAccountNumber(account_number);
        payCheque.setRmName(rm_name);
        payCheque.setAccountCurrency(account_currency);
        payCheque.setAccountClass(account_class);
        payCheque.setAccountTitle(account_title);
        payCheque.setTransDate(trans_date);
        payCheque.setTrnRefNo(trn_ref_no);
        payCheque.setCheckerId(checker_id);
        payCheque.setReturnChequeNo(return_cheque_no);
        payCheque.setReturnChequeAmount(return_cheque_amount);
        payCheque.setReturnReason(return_reason);
        payCheque.setNarration(narration);

        map.put(SLNo, payCheque);






    }

    public void getMapData(){

        for (Map.Entry<String, SummaryHtml> mainMap : summaryMap.entrySet()) {

            LOG.info("Printing Map Data");

            LOG.info("Count:"+mainMap.getValue().getCount());


        }

    }

    public String intToString(int count)
    {
        if(count==0)
        {
            return "0";
        }
        else {
            return String.valueOf(count);

        }
    }

    int sr = 1;


    public int serialNum() {
        return sr++;
    }


    public void populateHTMLData(String account_number, String account_currency, String account_class, String account_title, int value) {
        SummaryHtml summaryHtml=summaryMap.get(account_number);



        if(!summaryMap.containsKey(account_number)) {


            summaryHtml = new SummaryHtml();

            summaryHtml.setAccountNumber(account_number);
            summaryHtml.setAccountCurrency(account_currency);
            summaryHtml.setAccountClass(account_class);
            summaryHtml.setAccountTitle(account_title);

            summaryHtml.setCount(value);

        }
        else {

            summaryHtml.setCount(summaryHtml.getCount()+1);

        }


        summaryMap.put(account_number,summaryHtml);
    }
}
