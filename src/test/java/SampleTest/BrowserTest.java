package SampleTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class BrowserTest {

	Properties prop = new Properties();
	WebDriver driver;

	@Before
	public void beforeMethod() {
		String configPath = System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties";
		try {
			prop.load(new FileInputStream(configPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}


	@Test
	public void getData()
	{
		System.out.println("Hello GuyZ, CHANEL to MY SubScribe");
		System.out.println("In this test case, validate search on Bing, handle multiple windows and validate the title on new window");
		driver.get("https://bing.com");
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//div[contains(@class,'mic_cont icon')]"))));
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//textarea[@inputmode='search']"))));
			driver.findElement(By.xpath("//textarea[@inputmode='search']")).sendKeys(prop.get("SEARCH_KEY").toString()+Keys.RETURN);
			List<WebElement> results=driver.findElements(By.xpath("//h2/a"));
			String firstResultText = results.get(0).getText();
			Assert.assertTrue(firstResultText.equalsIgnoreCase(prop.get("EXPECTED_RESULT").toString()));
			results.get(0).click();
			Object[] windowHandles=driver.getWindowHandles().toArray();
			driver.switchTo().window((String) windowHandles[1]);
			Assert.assertTrue(driver.getTitle().equalsIgnoreCase(prop.get("EXPECTED_TITLE").toString()));
		}catch(NoSuchElementException e){
			System.out.println("Failed due to "+e.getMessage());
			driver.quit();
			Assert.fail("Element not found");
		}
	}

	@After
	public void afterMethod(){
		driver.quit();
	}
}