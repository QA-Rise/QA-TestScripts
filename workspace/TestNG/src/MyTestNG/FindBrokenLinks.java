package MyTestNG;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FindBrokenLinks {

	public static void main(String[] args) {

		String bscURL = "C:\\users\\iliona.iliadhi\\Work Folders\\Documents\\Kaplan.xlsx";

		//FindBrokenLinks notFound = new FindBrokenLinks();
		Redirects urls = new Redirects();
		//notFound.findBrokenLinks(bscURL);
		urls.redirects(bscURL);

	}

	public void findBrokenLinks(String fileName) {
		WebDriver driver;
		String title;
		//System.setProperty("webdriver.gecko.driver",
				//"C:\\Users\\Iliona.Iliadhi\\Work FoldeRS\\Desktop\\geckodriver.exe");
		 System.setProperty("webdriver.chrome.driver","C:\\users\\iliona.iliadhi\\Work Folders\\Documents\\chromedriver_win32\\chromedriver.exe");
		 driver = new ChromeDriver();
		//driver = new FirefoxDriver();
		// driver.manage().window().maximize();

		InputStream XlsxFileToRead = null;
		OutputStream XlsxFileToWrite = null;

		XSSFWorkbook workbook = null;

		try {
			XlsxFileToRead = new FileInputStream(fileName);

			// Getting the workbook instance for xlsx file
			workbook = new XSSFWorkbook(XlsxFileToRead);
			// getting the first sheet from the workbook using index
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i=0; i <=sheet.getLastRowNum(); i++) {
				System.out.println(i);
				System.out.println(sheet.getLastRowNum());
				String url = sheet.getRow(i).getCell(0).getStringCellValue();
				//System.out.println(url);
				driver.get(url);
			    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
				title = driver.getTitle();
				//System.out.println(title);

				Cell resultCell = sheet.getRow(i).getCell(1);
				if (resultCell == null) {
					resultCell = sheet.getRow(i).createCell(1);
				}

				if (title.contains("404")) {
					resultCell.setCellValue("FAIL");

				} else {
					resultCell.setCellValue("PASS");

				}
			}

			XlsxFileToRead.close();
			XlsxFileToWrite = new FileOutputStream(fileName);
			workbook.write(XlsxFileToWrite);
			XlsxFileToWrite.close();
			workbook.close();

			driver.quit();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
