package dev.sumeragizzz.seleniumjavaexample;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {

	public static void main(String[] args) throws Exception {
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
//		options.setHeadless(true);
		options.setHeadless(false);

		WebDriver driver = new ChromeDriver(options);
		try {
			driver.get("https://www.google.co.jp/");

			System.out.println(driver.getCurrentUrl());
			System.out.println(driver.getTitle());

			WebElement hplogo = driver.findElement(By.id("hplogo"));
			System.out.println(hplogo.getAttribute("src"));

			Dimension size = driver.manage().window().getSize();
			System.out.format("%d : %d%n", size.getHeight(), size.getWidth());

			WebElement q = driver.findElement(By.name("q"));
			q.sendKeys("Selenium");

			WebElement btnK = driver.findElement(By.name("btnK"));
			btnK.submit();

			Thread.sleep(3000);

		} finally {
			driver.quit();
		}
	}

}
