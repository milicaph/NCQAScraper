package pages;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private
    @FindBy(css = "table.displaytable")
    WebElement table;


    public ResultsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
    }

    public void executeJS(int pageNum){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("javascript:__doPostBack('ctl00$BodyContent$gvAccountResults','Page$"+pageNum+"');");
        System.out.println(pageNum + "DA PAGE IS DIS");
    }

    public void getResultPage(String url){
        driver.get(url);
    }

    private void waitForPageToLoad(){
        wait.until(ExpectedConditions.visibilityOf(table));

    }

    private String getCellValue(WebElement element){
        String value = element.getAttribute("innerHTML")
                              .replaceAll("<[^>]*>", "")
                              .trim();
        return value;
    }

    private void setCellOutputValue(int ic, String cellValue,
                                    List<WebElement> columns,
                                    XSSFRow rowX){

        WebElement cell = columns.get(ic);
        XSSFCell cellX = rowX.createCell(ic);
        cellValue = getCellValue(cell);
        String updatedCellValue = StringUtils.normalizeSpace(cellValue);
        cellX.setCellValue(updatedCellValue);
        System.out.println("Cell: " + ic + " " + updatedCellValue);

    }

    private void writeOutput(XSSFWorkbook workbook, String filePath){
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
        } catch (IndexOutOfBoundsException | IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTableData(XSSFWorkbook workbook, XSSFSheet sheet,
                               String filePath) throws IOException {

        waitForPageToLoad();
        try{ Thread.sleep(5000); } catch (Exception ignored){}

        List<WebElement> rows = driver.findElements(By.cssSelector("tr[valign=middle]"));

        for (int i = 0; i < rows.size()-1; i++) { //first row is the col names so skip it.
            WebElement row = rows.get(i);

            List<WebElement> columns = row.findElements(By.cssSelector("td"));
            int lastRow = sheet.getLastRowNum();
            XSSFRow rowX = sheet.createRow(lastRow+1);

            String cellValue = " ";
            for(int ic = 0; ic < columns.size(); ic++){
                System.out.println("<<<<<<<<<<" + ic + ">>>>>>>>>>>>");
                setCellOutputValue(ic, cellValue, columns, rowX);

            }

            writeOutput(workbook, filePath);
        }


    }



}
