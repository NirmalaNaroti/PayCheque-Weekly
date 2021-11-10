package com.fragma.service;

import com.fragma.dto.PayCheque;
import com.fragma.dto.MainDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class ExcelFileCreator {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelFileCreator.class);

    XSSFWorkbook workbook = new XSSFWorkbook();

    DecimalFormat df = new DecimalFormat("#,###.00");

    public void createAllSheets(String excelFileLocation, MainDto mainDto) throws Exception {

         createDataTemplateSheet(mainDto, "Pay_Cheque_Template");
         createDataExtractSheet(mainDto,"Pay_Cheque_Extract");



        FileOutputStream out = new FileOutputStream(excelFileLocation);
        this.workbook.write(out);
        out.close();
        LOG.info(" Excel file written successfully on disk at :" + excelFileLocation);
    }

    private void createDataTemplateSheet(MainDto mainDto, String sheetName) throws Exception {

        LOG.info("***** executing createDataTemplateSheet ****** ");


        Font headingFont = workbook.createFont();
        headingFont.setBold(true);

        XSSFColor orange = new XSSFColor(new java.awt.Color(182, 207, 242));

        XSSFCellStyle headingCellStyle = workbook.createCellStyle();

        headingCellStyle.setFont(headingFont);
        headingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headingCellStyle.setFillForegroundColor(orange);
        headingCellStyle.setBorderBottom(BorderStyle.THIN);
        headingCellStyle.setBorderLeft(BorderStyle.THIN);
        headingCellStyle.setBorderRight(BorderStyle.THIN);
        headingCellStyle.setBorderTop(BorderStyle.THIN);
        headingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headingCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headingCellStyle.setWrapText(true);

        XSSFColor lightOrange = new XSSFColor(new java.awt.Color(255, 216, 151));

        XSSFCellStyle MainHeadingCellStyle = workbook.createCellStyle();

        MainHeadingCellStyle.setFont(headingFont);
        MainHeadingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        MainHeadingCellStyle.setFillForegroundColor(lightOrange);
        MainHeadingCellStyle.setBorderBottom(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderLeft(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderRight(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderTop(BorderStyle.THIN);
        MainHeadingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        MainHeadingCellStyle.setAlignment(HorizontalAlignment.CENTER);
        MainHeadingCellStyle.setWrapText(true);

        CellStyle bordersOnly = workbook.createCellStyle();
        bordersOnly.setBorderBottom(BorderStyle.THIN);
        bordersOnly.setBorderLeft(BorderStyle.THIN);
        bordersOnly.setBorderRight(BorderStyle.THIN);
        bordersOnly.setBorderTop(BorderStyle.THIN);
        bordersOnly.setAlignment(HorizontalAlignment.CENTER);
        bordersOnly.setVerticalAlignment(VerticalAlignment.CENTER);


        Sheet sheet = workbook.createSheet(sheetName);

        Row headingRow1 = sheet.createRow(0);
        headingRow1.setHeight((short) 900);

        sheet.addMergedRegion(new CellRangeAddress(0,0,0,10));

        createCellAddData(headingRow1, 0, "Factoring & LRD Daily Return Cheque Report Dated: "+mainDto.getTodayDate(), MainHeadingCellStyle);

        int rowNum=1;

        Row headingRow = sheet.createRow(rowNum++);
        headingRow.setHeight((short) 900);

        int headingColmIndx = 0;

        createCellAddData(headingRow, headingColmIndx++, "SL No", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Number", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "RM Name", headingCellStyle);

        createCellAddData(headingRow, headingColmIndx++, "Account Currency", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Class", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Title", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Trans_Date", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "trn_ref", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Checker ID", headingCellStyle);

        createCellAddData(headingRow, headingColmIndx++, "Return Cheque No", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Return Cheque Amount", headingCellStyle);


        for (Map.Entry<Integer, PayCheque> tdEntry : mainDto.getMap().entrySet()) {

            Row row = sheet.createRow(rowNum++);
            int cell = 0;

            createCellAddData(row, cell++, String.valueOf(tdEntry.getKey()), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountNumber(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getRmName(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountCurrency(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountClass(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountTitle(), bordersOnly);

            createDateCellAddData(row, cell++, getFullDate(tdEntry.getValue().getTransDate()), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getTrnRefNo(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getCheckerId(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getReturnChequeNo(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getReturnChequeAmount(), bordersOnly);

        }



        for (int i = 0; i <= sheet.getRow(1).getLastCellNum(); i++) {

            sheet.autoSizeColumn(i);
            int columnWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, columnWidth + 1000);
        }
    }

    private void createDataExtractSheet(MainDto mainDto, String sheetName) throws Exception {

        LOG.info("***** executing createDataTemplateSheet ****** ");

        Font headingFont = workbook.createFont();
        headingFont.setBold(true);

        XSSFColor orange = new XSSFColor(new java.awt.Color(182, 207, 242));

        XSSFCellStyle headingCellStyle = workbook.createCellStyle();

        headingCellStyle.setFont(headingFont);
        headingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headingCellStyle.setFillForegroundColor(orange);
        headingCellStyle.setBorderBottom(BorderStyle.THIN);
        headingCellStyle.setBorderLeft(BorderStyle.THIN);
        headingCellStyle.setBorderRight(BorderStyle.THIN);
        headingCellStyle.setBorderTop(BorderStyle.THIN);
        headingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headingCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headingCellStyle.setWrapText(true);

        XSSFColor lightOrange = new XSSFColor(new java.awt.Color(255, 216, 151));

        XSSFCellStyle MainHeadingCellStyle = workbook.createCellStyle();

        MainHeadingCellStyle.setFont(headingFont);
        MainHeadingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        MainHeadingCellStyle.setFillForegroundColor(lightOrange);
        MainHeadingCellStyle.setBorderBottom(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderLeft(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderRight(BorderStyle.THIN);
        MainHeadingCellStyle.setBorderTop(BorderStyle.THIN);
        MainHeadingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        MainHeadingCellStyle.setAlignment(HorizontalAlignment.CENTER);
        MainHeadingCellStyle.setWrapText(true);

        CellStyle bordersOnly = workbook.createCellStyle();
        bordersOnly.setBorderBottom(BorderStyle.THIN);
        bordersOnly.setBorderLeft(BorderStyle.THIN);
        bordersOnly.setBorderRight(BorderStyle.THIN);
        bordersOnly.setBorderTop(BorderStyle.THIN);
        bordersOnly.setAlignment(HorizontalAlignment.CENTER);
        bordersOnly.setVerticalAlignment(VerticalAlignment.CENTER);


        Sheet sheet = workbook.createSheet(sheetName);

        Row headingRow1 = sheet.createRow(0);
        headingRow1.setHeight((short) 900);

        sheet.addMergedRegion(new CellRangeAddress(0,0,0,12));

        createCellAddData(headingRow1, 0, "Factoring & LRD Daily Return Cheque Report Dated: "+mainDto.subDate1(), MainHeadingCellStyle);

        int rowNum=1;

        Row headingRow = sheet.createRow(rowNum++);
        headingRow.setHeight((short) 900);

        int headingColmIndx = 0;

        createCellAddData(headingRow, headingColmIndx++, "SL No", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Number", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "RM Name", headingCellStyle);

        createCellAddData(headingRow, headingColmIndx++, "Account Currency", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Class", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Account Title", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Trans_Date", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "trn_ref", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, " Checker ID", headingCellStyle);

        createCellAddData(headingRow, headingColmIndx++, "Return Cheque No", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Return Cheque Amount", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Reason ", headingCellStyle);
        createCellAddData(headingRow, headingColmIndx++, "Narration ", headingCellStyle);


        for (Map.Entry<Integer, PayCheque> tdEntry : mainDto.getMap().entrySet()) {

            Row row = sheet.createRow(rowNum++);
            int cell = 0;

            createCellAddData(row, cell++, String.valueOf(tdEntry.getKey()), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountNumber(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getRmName(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountCurrency(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountClass(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getAccountTitle(), bordersOnly);

            createDateCellAddData(row, cell++, getFullDate(tdEntry.getValue().getTransDate()), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getTrnRefNo(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getCheckerId(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getReturnChequeNo(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getReturnChequeAmount(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getReturnReason(), bordersOnly);
            createCellAddData(row, cell++, tdEntry.getValue().getNarration(), bordersOnly);


        }


        for (int i = 0; i <= sheet.getRow(1).getLastCellNum(); i++) {

            sheet.autoSizeColumn(i);
            int columnWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, columnWidth + 1000);
        }
    }


    public void createCellAddData(Row row, int cellNo, String cellValue, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNo);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
    }
    public void createDateCellAddData(Row row, int cellNo, Date cellValue, CellStyle cellStyle) {

        Cell cell = row.createCell(cellNo);
        cell.setCellValue(cellValue);
        CreationHelper creationHelper = workbook.getCreationHelper();


        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
                "yyyy-MM-dd HH:mm:ss"));
        cell.setCellStyle(cellStyle);

    }
    private Date getFullDate(String strdate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= dateFormat.parse(strdate);
        System.out.println("Date:"+date);
        return date;
    }


}