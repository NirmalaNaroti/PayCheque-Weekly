package com.fragma.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "conf")
public class ConfigurationHelper {

    private static String query;
    private static String htmlquery;
    private static  String zeropaychequehtmlquery;
    private static String excelLocation;

    public static String getQuery() {
        return query;
    }

    public static void setQuery(String query) {
        ConfigurationHelper.query = query;
    }

    public static String getHtmlquery() {
        return htmlquery;
    }

    public static void setHtmlquery(String htmlquery) {
        ConfigurationHelper.htmlquery = htmlquery;
    }

    public static String getZeropaychequehtmlquery() {
        return zeropaychequehtmlquery;
    }

    public static void setZeropaychequehtmlquery(String zeropaychequehtmlquery) {
        ConfigurationHelper.zeropaychequehtmlquery = zeropaychequehtmlquery;
    }

    public static String getExcelLocation() {
        return excelLocation;
    }

    public static void setExcelLocation(String excelLocation) {
        ConfigurationHelper.excelLocation = excelLocation;
    }
}
