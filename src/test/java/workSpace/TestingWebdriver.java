package workSpace;


import static java.lang.invoke.MethodHandles.lookup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;

import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import omf.summit.Login;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;


public class TestingWebdriver {
	String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
	static final Logger log = getLogger(lookup().lookupClass());
	String title;
	private WebDriver driver;

	//performs before all other
	@BeforeMethod
	void classSetup() {
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\X483094\\.cache\\selenium\\chromedriver\\win64\\126.0.6478.126.chromedriver.exe");
		WebDriverManager.edgedriver().create();

	 
	EdgeOptions browserOptions = new EdgeOptions();
	DesiredCapabilities cap = new DesiredCapabilities();
	//We accept any browser popup to clear the screen
	cap.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.ACCEPT);
	browserOptions.addArguments("--remote-allow-origins=*");
	browserOptions.addArguments("--disable-popup-blocking");

		driver = new EdgeDriver(browserOptions);
		driver.get(sutUrl);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		title = driver.getTitle();
		
		assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java");
		
	}
	
	@Test
	void runTest(){

		//getting the current date from the system clock
		LocalDate today = LocalDate.now();
		int currentYear = today.getYear();
		int currentDay = today.getDayOfMonth();


		//verify the title of the page
		if(assertThat(title).isEqualTo("Hands-On Selenium WebDriver with Java") != null) {

			log.debug("The title of {} is {}",sutUrl  ,title);

		}

		//click to open the calendar
		WebElement datePicker = driver.findElement(By.xpath("//input[@name='my-date']"));
		datePicker.click();

		//click
		WebElement monthElement = driver.findElement(By.xpath(String.format("//th[contains(text(),'%d')]", currentYear)));
		monthElement.click();

		//Click on the left arrow of the calendar
		WebElement arrowLeft = driver.findElement(RelativeLocator.with(By.tagName("th")).toRightOf(monthElement));
		arrowLeft.click();

		/*We will now play around with the sliders*/

		WebElement rangeSlider = driver.findElement(By.name("my-range"));

		String initValue = rangeSlider.getAttribute("value");
		log.debug("Initial value of the slider is {}", initValue );

		for(int i = 0; i < 5; i++) {

			rangeSlider.sendKeys(Keys.ARROW_RIGHT);

		}

		String endValue = rangeSlider.getAttribute("value");
		assertThat(initValue).isNotEqualTo(endValue);

		//click on the second checkbox

		WebElement checkbox2 = driver.findElement(By.id("my-check-2"));

		checkbox2.click();
		assertThat(checkbox2.isSelected()).isTrue();

		//Upload file

		WebElement inputFile = driver.findElement(By.name("my-file"));
		try {


			Path tempFile = Files.createTempFile("tempfiles", "tmp");
			String  fileName = tempFile.toAbsolutePath().toString();

			inputFile.sendKeys(fileName);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		driver.findElement(By.tagName("form")).submit();

		assertThat(driver.getCurrentUrl()).isNotEqualTo(sutUrl);


	}
	
	@AfterMethod
	void closeBrowsers() {
		driver.close();
		driver.quit();
	}

}
