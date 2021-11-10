package com.fragma.dao;

import com.fragma.config.ConfigurationHelper;
import com.fragma.dto.MainDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class ReportDao {

    static Logger LOG = LoggerFactory.getLogger(ReportDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final ConfigurationHelper configurationHelper;
    int SLNo=0;

    @Autowired
    public ReportDao(@Qualifier("hiveJdbcTemplate") JdbcTemplate jdbcTemplate, ConfigurationHelper configurationHelper) {
        this.jdbcTemplate = jdbcTemplate;
        this.configurationHelper = configurationHelper;

    }

    public void getData(MainDto mainDto){
        LOG.info("***** executing getData *****");
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                LOG.info("Query = "+ ConfigurationHelper.getQuery() );
                PreparedStatement ps = connection.prepareStatement(ConfigurationHelper.getQuery());

                return ps;
            }
        },new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

                String account_number = isNullOrEmpty(resultSet.getString("account_number"));
                String rm_name = isNullOrEmpty(resultSet.getString("rm_name"));
                String account_currency = isNullOrEmpty(resultSet.getString("account_currency"));
                String account_class = isNullOrEmpty(resultSet.getString("account_class"));
                String account_title = isNullOrEmpty(resultSet.getString("account_title"));
                String trans_date = isNullOrEmpty(resultSet.getString("trans_date"));
                String trn_ref_no = isNullOrEmpty(resultSet.getString("trn_ref_no"));
                String checker_id = isNullOrEmpty(resultSet.getString("checker_id"));
                String return_cheque_no = isNullOrEmpty(resultSet.getString("return_cheque_no"));
                String return_cheque_amount = isNullOrEmpty(resultSet.getString("return_cheque_amount"));
                String return_reason = isNullOrEmpty(resultSet.getString("return_reason"));
                String narration = isNullOrEmpty(resultSet.getString("narration"));

               LOG.info("account_number:"+account_number+"rm_name:"+rm_name+"account_currency:"+account_currency+"account_class:"+account_class+"account_title:"+account_title+"trans_date:"+trans_date+"trn_ref_no"+trn_ref_no+"checker_id"+checker_id+"trn_ref_no"+trn_ref_no+"return_cheque_no"+return_cheque_no+"return_cheque_amount"+return_cheque_amount+"return_reason"+return_reason+"narration"+narration);

                mainDto.populateData(++SLNo,account_number,rm_name,account_currency,account_class,account_title,trans_date,trn_ref_no,checker_id,return_cheque_no,return_cheque_amount,return_reason,narration);
            }
        });
    }

    public void getHTMLData(MainDto mainDto){
        LOG.info("***** executing getData *****");
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                LOG.info("Query = "+ ConfigurationHelper.getHtmlquery() );
                PreparedStatement ps = connection.prepareStatement(ConfigurationHelper.getHtmlquery());

                return ps;
            }
        },new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

                String account_number = isNullOrEmpty(resultSet.getString("account_number"));
                String account_currency = isNullOrEmpty(resultSet.getString("account_currency"));
                String account_class = isNullOrEmpty(resultSet.getString("account_class"));
                String account_title = isNullOrEmpty(resultSet.getString("account_title"));

                LOG.info("account_number:"+account_number+"account_currency:"+account_currency+"account_class:"+account_class+"account_title:"+account_title);

                mainDto.addAccountNumberToSet(account_number);

                mainDto.populateHTMLData(account_number,account_currency,account_class,account_title,1);
            }
        });
    }
    public void getHTMLDataForZeroPaycheque(MainDto mainDto){
        LOG.info("***** executing getData *****");
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                LOG.info("Query = "+ ConfigurationHelper.getZeropaychequehtmlquery() );
                PreparedStatement ps = connection.prepareStatement(ConfigurationHelper.getZeropaychequehtmlquery());

                return ps;
            }
        },new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

                String account_number = isNullOrEmpty(resultSet.getString("account_number"));
                String account_currency = isNullOrEmpty(resultSet.getString("account_currency"));
                String account_class = isNullOrEmpty(resultSet.getString("account_class"));
                String account_title = isNullOrEmpty(resultSet.getString("account_title"));

                LOG.info("account_number:"+account_number+"account_currency:"+account_currency+"account_class:"+account_class+"account_title:"+account_title);

                mainDto.addAccountNumberToSet(account_number);

                mainDto.populateHTMLData(account_number,account_currency,account_class,account_title, 0);
            }
        });
    }

    public String isNullOrEmpty(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("\"\"")) {
            return " ";
        } else
            return value;
    }

}
