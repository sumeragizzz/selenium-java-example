package dev.sumeragizzz.seleniumjavaexample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Driver;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception {
		// ヘッドレスモード
		ChromeOptions options = new ChromeOptions();
		options.setHeadless(true);

		// WebDriver
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
		WebDriver driver = new ChromeDriver(options);
		try {
			// 暗黙的な待機
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			// サイトへ移動
			String url = System.getProperty("url");
			driver.get(url);

			// ID入力
			String id = System.getProperty("id");
			WebElement obcId = driver.findElement(By.id("OBCID"));
			obcId.sendKeys(id);
			WebElement checkAuthPolisyBtn = driver.findElement(By.id("checkAuthPolisyBtn"));
			checkAuthPolisyBtn.click();

			// パスワード入力 →　ログイン
			String pass = System.getProperty("pass");
			WebElement password = driver.findElement(By.id("Password"));
			password.sendKeys(pass);
			WebElement login = driver.findElement(By.id("login"));
			login.click();

			// 勤務実績照会
			WebElement btn00 = driver.findElement(By.id("btn00"));
			btn00.click();

			// 初期表示時の年月を取得
			String currentMonthText = driver.findElement(By.id("js-attendanceResultTime__yearMonth")).getText();
			System.out.println(currentMonthText);

			// TODO 対象の年月までループして＜ボタンをクリック → 暫定で前月を対象とする
			WebElement before = driver.findElement(By.id("js-beforeMonthBtn"));
			before.click();

			// 年月要素は最初からあるので、暗黙的な待機では待機されない。
			// 値が変化したことを条件に待機する。
			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("js-attendanceResultTime__yearMonth"), currentMonthText)));

			// 年月チェック
			String beforeMonthText = driver.findElement(By.id("js-attendanceResultTime__yearMonth")).getText();
			System.out.println(beforeMonthText);

			// JSoup
			Document document = Jsoup.parse(driver.getPageSource());

			// 日数ループ
			int rowSize = YearMonth.parse(beforeMonthText, DateTimeFormatter.ofPattern("yyyy年 M月")).lengthOfMonth();
			for (int i = 1; i <= rowSize; i++) {
				// 申請 - 日付 - 曜日
				String dateText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=1]", i)).text();

				// 事由 〜
				String startTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=4]", i)).text();
				String endTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=5]", i)).text();
				String activeTimeText = document.select(String.format("tr[data-rowindex=%d] td[data-columnindex=6]", i)).text();

				System.out.format("%d : %s : %s : %s : %s%n", i, dateText, startTimeText, endTimeText, activeTimeText);
			}

		} finally {
			driver.quit();
		}
	}

}
