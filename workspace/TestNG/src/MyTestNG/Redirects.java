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
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Redirects {

	public void redirects(String fileName) {
		WebDriver driver;

		//System.setProperty("webdriver.chrome.driver",
				//"C:\\users\\iliona.iliadhi\\Work Folders\\Documents\\chromedriver_win32\\chromedriver.exe");
		System.setProperty("webdriver.gecko.driver","C:\\Users\\Iliona.Iliadhi\\Work FoldeRS\\Desktop\\geckodriver.exe");

		//driver = new ChromeDriver();
		driver = new FirefoxDriver();

		// driver.manage().window().maximize();
		XSSFWorkbook workbook = null;

		InputStream XlsxFileToRead = null;
		OutputStream XlsxFileToWrite = null;

		try {
			XlsxFileToRead = new FileInputStream(fileName);
			workbook = new XSSFWorkbook(XlsxFileToRead);

			// Getting the workbook instance for xlsx file

			// getting the first sheet from the workbook using sheet name.
			// We can also pass the index of the sheet which starts from '0'.
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				System.out.println(sheet.getLastRowNum());
				String originalKaplanUrl = sheet.getRow(i).getCell(0).getStringCellValue();
				String redirectKaplanUrl = sheet.getRow(i).getCell(1).getStringCellValue();
				System.out.println(originalKaplanUrl);
				driver.get(originalKaplanUrl);
				try{
					   //Wait 10 seconds till alert is present
					   WebDriverWait wait = new WebDriverWait(driver, 10);
					   Alert alert = wait.until(ExpectedConditions.alertIsPresent());
					   
					   //Accepting alert.
					   alert.accept();
					   System.out.println("Accepted the alert successfully.");
					}catch(Throwable e){
					   System.err.println("Error came while waiting for the alert popup. "+e.getMessage());
					}

				// driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
				String liveKaplanUrl = driver.getCurrentUrl();

				Cell resultCell = sheet.getRow(i).getCell(2);
				if (resultCell == null) {
					resultCell = sheet.getRow(i).createCell(2);
				}

				if (liveKaplanUrl.equals(redirectKaplanUrl)) {
					resultCell.setCellValue("PASS");
					System.out.println("Redirect is working" + liveKaplanUrl);
				} else {
					resultCell.setCellValue("FAIL");
					System.out.println("Expected URL was  " + redirectKaplanUrl);
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
