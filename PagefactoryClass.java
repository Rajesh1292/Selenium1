package submission;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PagefactoryClass {
	
	WebDriver driver;

	public PagefactoryClass(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		}
	
	
	@FindBy(xpath = "//h2[text()='Data cannot be saved']/following::button[text()='Close']")
	WebElement Save_notsaved_button_click;
	@FindBy(xpath = "//h1[text()='System Error']")
	WebElement Login_Systemerror;
	@FindBy(xpath = "//h1[text()[contains(.,'Additional Verification Required')]]")
	WebElement additionalInformation_Title;
	@FindBy(xpath = "//span[@id='ctl00_PlaceHolderMain_lblQuestionNote']//following::label[1]")
	WebElement additionalInformation_getQuestion;
	@FindBy(xpath = "//input[@id='txtAnswer']")
	WebElement additionalInformation_answer;
	@FindBy(xpath = "//input[@name='ctl00$PlaceHolderMain$btnSubmit']")
	WebElement additionalInformation_submit;
	
	
	@FindBy(xpath = "//*[@id='txtUsername']")
	WebElement Login_username_textbox_enter;
	@FindBy(xpath = "//*[@id='txtPd']")
	WebElement Login_username_password_enter;
	@FindBy(xpath = "//*[@id='btnLogin']")
	WebElement Login_login_button_click;
	@FindBy(xpath = "//img[@title='Visa Online Home']")
	WebElement Home_title_verify;
	@FindBy(xpath = "//div[@id='layout-header-text' and text()[contains(.,'Chip Compliance Reporting Tool')]]")
	WebElement CCRT_title_Verify;
	@FindBy(xpath = "//span[@id='spanpersonaName']")
	WebElement Home_profile_menu_click;
	@FindBy(xpath = "//span[@id='linkSwitchProfile']")
	WebElement Home_profile_switchProfile_nest_click;
	@FindBy(xpath = "//a[@title='Additional Profile' and text()='LAC']")
	WebElement Home_profile_switchProfile_additionalProfile_Country_LAC_click;
	@FindBy(xpath = "//a[@title='Additional Profile' and text()='Canada']")
	WebElement Home_profile_switchProfile_additionalProfile_Country_Canada_click;

	@FindBy(xpath = "//a[text()='Chip Compliance Reporting Tool (CCRT)']") //// a[@id='CCRT']
	WebElement Home_CCRT_link_switchWindow_link;

	@FindBy(xpath = "//a[@href='/CCRT/Report/ReportList/Index?status=Draft']")
	WebElement CCRTpage_DraftReport_link_click;

	@FindBy(xpath = "//h2[text()[contains(.,'Basic Information')]]")
	WebElement Reportpage_basicInformation_verify;
	@FindBy(xpath = "//*[@id='btnNext']") // input[@id='btnNext']
	WebElement nextButton;
	@FindBy(xpath = "//h2[text()[contains(.,'Device Configuration')]]")
	WebElement Reportpage_deviecConfiguration_verify;
	
	
	@FindBy(xpath = "//td[text()[contains(.,'#####')]]//following-sibling::td[9]/div/div/button/i[@class='fas fa-pencil-alt']")
	WebElement Report_Testcase_Edit_click;
	
	
	
	
	
	public void click(String xpathValue) {
		driver.findElement(By.xpath(xpathValue)).click();
	}

	
	public void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			System.out.println("Timeout waiting for Page Load Request to complete.");
		}
	}

	public void switchtoWindow() {
		String winHandleBefore = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			driver.manage().window().maximize();
		}

	}

	public void moveCursorOutside()
	{
		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		robot.mouseMove(1000, 1000);
	}


	
	public void click_javascript(String xpathValue, String testcase) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",
				driver.findElement(By.xpath(getProperty(xpathValue).replace("#####", testcase))));
	}
	public String getProperty(String name) {
		FileInputStream fis = null;
		Properties properties = new Properties();
		try {
			fis = new FileInputStream(
					"./src/main/java/submission/OR.properties");
			System.out.println(fis);
		} catch (NullPointerException e) {
			System.out.println("file null pointer exception");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		try {
			properties.load(fis);
		} catch (IOException e) {
		} catch (NullPointerException e) {
			System.out.println("properties null pointer exception");
		}

		try {
			System.out.println("name " + properties.getProperty(name));
			properties.getProperty(name);
		} catch (NullPointerException e) {
			System.out.println("name null");
		}
		return properties.getProperty(name);
	}

	@FindBy(xpath = "//td[text()[contains(.,'#####')]]//following-sibling::td[8]/div/div/button/i[@class='fas fa-pencil-alt']")
	WebElement Contactless_Report_Testcase_Edit_click;
	public void getandReplace_click(String xpathValue, String testcase) {
		click(getProperty(xpathValue).replace("#####", testcase));

	}

	public void getandReplace_select(String xpathValue, String testcase, String Option) {
		String replacedXpath = getProperty(xpathValue).replace("#####", testcase);
		Select selectValue = new Select(driver.findElement(By.xpath(replacedXpath)));
		selectValue.selectByVisibleText(Option);
	}

	public void getandReplace_select_Index(String xpathValue, String testcase, String Option) {
		String replacedXpath = getProperty(xpathValue).replace("#####", testcase);
		click(replacedXpath);
		Select selectValue = new Select(driver.findElement(By.xpath(replacedXpath)));
		selectValue.selectByIndex(1);
	}

	public void select(String xpath, String Option) {
		Select selectValue = new Select(driver.findElement(By.xpath(xpath)));
		selectValue.selectByVisibleText(Option);
	}
	
	public void selectvalue(String xpathValue, String Option) {
		Select selectValue = new Select(driver.findElement(By.xpath(xpathValue)));
		selectValue.selectByValue(Option);
	}

	public void enter_with_replace(String xpathValue, String testcase, String inputValue) {
		driver.findElement(By.xpath(getProperty(xpathValue).replace("#####", testcase))).clear();
		driver.findElement(By.xpath(getProperty(xpathValue).replace("#####", testcase))).sendKeys(inputValue);
	}

	
	//Additional Verification
	@FindBy(xpath = "//label[@id='ctl00_PlaceHolderMain_lblQuestion']")
	WebElement additionalVerification_question;
	@FindBy(xpath = "//label[@id='ctl00_PlaceHolderMain_lblQuestion']/parent::p/span/input")
	WebElement additionalVerification_answer_textBox;
	@FindBy(xpath = "//div[@id='btnDiv']/input[@type='submit']")
	WebElement additionalVerification_submit_Button;
	@FindBy(xpath = "(//span[text()='X'])[2]")
	WebElement closeCCRTportal;
	@FindBy(xpath = "//button[text()='Upload Files']")
	WebElement uploadFiles;
	@FindBy(xpath = "//h1[text()[contains(.,'Additional Verification Required')]]")
	WebElement page_additionalVerification;
	@FindBy(xpath = "//label[text()='What is your favorite sport to watch?']")
	WebElement additionallVerification_Question1;
	@FindBy(xpath = "//label[text()='What was the color of the first vehicle you owned?']")
	WebElement additionalVerification_Question2;
	@FindBy(xpath = "//label[text()='Where did you meet your first spouse or partner for the very first time?']")
	WebElement additionalVerification_Question3;
	@FindBy(xpath = "//label[text()='What is your favorite flower?']")
	WebElement additionalVerification_Question4;
	
	@FindBy(xpath = "//td[contains(text(),'Test_Report')]")
	WebElement CCRTpage_Report_Title;
	@FindBy(xpath = "//a[contains(text(),'39696')]")
	WebElement CCRTpage_Report_id;


	@FindBy(xpath = "//span[@class='txtAnswer']/input")
	WebElement additionalVerification_answer_input;


	@FindBy(xpath = "//input[@name='ctl00$PlaceHolderMain$btnSubmit']")
	WebElement additionalVerification_answer_submit;
	
	//select drop down
		 @FindBy(xpath ="//div[contains(text(),'Test Case 1b - v7.0')]")
		 WebElement TestCaseResultdrp; 
		 
// Upload
	
	@FindBy(xpath = "//*[@id='btnUploadFiles']")
	WebElement UploadFileBtn;
	
	@FindBy(xpath ="//*[@id='UploadAdvtDocumentInputFile']")
	WebElement BrowseBtn;
	
	@FindBy(xpath ="//*[@id='UploadCdetDocumentInputFile']")
	WebElement BrowseBtnCDET;
	
	
	@FindBy(xpath ="//button[@id='btnUploadAdvtDocument']")
	WebElement UploadBtn;
	
	@FindBy(xpath ="//button[@id='btnUploadCdetDocument']")
	WebElement UploadBtnCDET;
	
	@FindBy(xpath = "//button[contains(text(),'Close')]")
	WebElement CloseBtn;

	@FindBy(xpath = "//button[@class='close'][@data-dismiss='modal']")
	WebElement CloseUploadWindow;
	
	@FindBy(xpath = "//button[@id='btnSave']")
	WebElement NextBtn;
	
}

