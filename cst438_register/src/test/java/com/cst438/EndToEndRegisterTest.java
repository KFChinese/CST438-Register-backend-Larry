package com.cst438;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndRegisterTest {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/larry.chiem/chromedriver";
	public static final String URL = "http://localhost:3000";
	
	
	public static final int SLEEP_DURATION = 1000;
  public static final String TEST_USER_NAME = "Anon1";
	public static final String TEST_USER_EMAIL = "anyon1@csumb.edu";
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		
		Student student = null;
		
		do {
			student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}
		} while (student != null);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    WebDriverWait wait = new WebDriverWait(Scenario1Test.driver, 15); 

		
		try {
		
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/a"))).click();
			Thread.sleep(SLEEP_DURATION);
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/button"))).click();
      Thread.sleep(SLEEP_DURATION);
			
			
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/div/div[1]/div/div/input"))).sendKeys(TEST_USER_NAME);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/div/div[2]/div/div/input"))).sendKeys(TEST_USER_EMAIL);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/div[3]/div/div[3]/button[2]"))).click();
			Thread.sleep(SLEEP_DURATION);
			
		
			
			driver.quit();
		
	} 
}
