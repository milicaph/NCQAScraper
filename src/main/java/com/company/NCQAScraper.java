package com.company;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.ResultsPage;
import parsing.HTMLParser;
import settings.InitDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NCQAScraper {

    public static void main(String[] args) throws IOException {
        InitDriver initiateDriver = new InitDriver();

        WebDriver driver = initiateDriver.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        driver.manage().window().maximize();

        ResultsPage resultsPage = new ResultsPage(driver, wait);

        String urlPrivate = "https://healthinsuranceratings.ncqa.org/2019/search";
        String urlMedicaid = urlPrivate.concat("/Medicaid");
        String urlMedicare = urlPrivate.concat("/Medicare");

        String[] urls = {urlPrivate, urlMedicaid, urlMedicare};
        HashMap<String, Integer> totalPages = new HashMap<String, Integer>();
        totalPages.put(urlMedicare, 27);
        totalPages.put(urlMedicaid, 14);
        totalPages.put(urlPrivate, 28);

        String filePath = "src/main/resources/Rezultati.xlsx";
        File testFile = new File(filePath);
        FileInputStream fis = new FileInputStream(testFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        for(String url : urls){
            driver.get(url);
            System.out.println("URL SCRAPING: " + url);

            for(int i = 1; i <= totalPages.get(url); i++) {

                resultsPage.writeTableData(workbook, sheet, filePath);
                int pageNum = i+1;
                resultsPage.executeJS(pageNum);

            }


        }

        driver.quit();

    }
}
