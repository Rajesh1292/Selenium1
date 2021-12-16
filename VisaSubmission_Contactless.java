package submission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.sun.awt.SecurityWarning;
import com.sun.xml.internal.stream.buffer.sax.Properties;

import EMVValidation.EMVRetrieval;
import EMVValidation.Wrappers;
import io.github.bonigarcia.wdm.WebDriverManager;
import regressionClassB.Main;
import runFiles.RunFile;
import validation.payApp_EMVValidation.PayAppEMVValidation;
import wrappers.Report;
import wrappers.ReusableClass;
import wrappers.ScreenRecorderUtil;

public class VisaSubmission_Contactless {

	private static final String String = null;
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;

	static String testcase;
	static String Online_Select;
	static String TVR;
	static String Offline_Status;
	static String correctCVM;
	static String correctMessageDisplayed;
	static String RRN;
	static String authID;
	static String comments;
	public boolean ClassB_CDET = false;
	public boolean PayApp_CDET = false;
	static ReusableClass rs = new ReusableClass();
	XSSFWorkbook wb;
	XSSFSheet sheet;
	static LinkedHashMap<String, String> inputMapping = new LinkedHashMap<String, String>();
	Wrappers wc = new Wrappers();
	Report rpt = new Report();
	static int count;

	public void retrieveFields() {
		XSSFWorkbook wb = null;
		// File file = new File(wc.getProperty("inputMappingSheet"));
		File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\inputmapping.xlsx");
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb = new XSSFWorkbook(fi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String field;
		String row = "1";
		String col = "1";
		Sheet sheet = wb.getSheetAt(0);
		System.out.println("last row " + sheet.getLastRowNum());
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			field = sheet.getRow(i).getCell(0).getStringCellValue().trim();
			row = sheet.getRow(i).getCell(1).getStringCellValue().trim();
			col = sheet.getRow(i).getCell(2).getStringCellValue().trim();
			System.out.println(field + " " + row + " " + col);
			inputMapping.put(field, row + " " + col);
		}

	}

	@Test
	public void start() {
		// CDET - Contactless - Supports both Class B and PayApp Contactless
		try {
			ScreenRecorderUtil.startRecord("VisaSubmission");
		} catch (Exception e4) {
			e4.printStackTrace();
		}

		EMVRetrieval ev = new EMVRetrieval();
		PayAppEMVValidation py = new PayAppEMVValidation();
		if (ClassB_CDET == true && PayApp_CDET == false) {
			ev.start();
		} else if (ClassB_CDET == false && PayApp_CDET == true) {
			py.start();
		}

		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		options.setExperimentalOption("useAutomationExtension", false);
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(options);
		String baseURL = rs.ReadConfig("baseURL");
		String username = rs.ReadConfig("username");
		String password = rs.ReadConfig("password");
//		String region = rs.ReadConfig("region");
//		String report = rs.ReadConfig("report");
		String report = Main.reportinputvisa;
		String region = Main.regioninputvisa;
		++count;
		ev.report("passed", "Test Case - ",
				"baseURL " + baseURL + " username" + username + "password" + password + "report" + report);

		String localdirectoryCDET = RunFile.classB_sourceCardlogPath;
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(baseURL);
		driver.manage().window().maximize();
		ReusableClass bc = new ReusableClass(driver);
		PagefactoryClass pg = new PagefactoryClass(driver);
		pg.Login_username_textbox_enter.sendKeys(username);
		pg.Login_username_password_enter.sendKeys(password);
		pg.Login_login_button_click.click();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		if (!driver.findElements(By.xpath("//h1[text()='System Error']")).isEmpty()) {
			ev.report("failed", "Test Case ", "Encountered system error! Login failed");
			ev.flush();
			System.exit(0);
		}
		++count;
		ev.report("passed", "Test Case " + count, "Random message");
		if (!driver.findElements(By.xpath("//h1[text()[contains(.,'Additional Verification Required')]]")).isEmpty()) {
			// if (pg.additionalInformation_Title.isDisplayed()) {
			if (pg.additionalInformation_getQuestion.getText().equals("What is your favorite sport to watch?")) {
				pg.additionalInformation_answer.sendKeys("cricket");
			} else if (pg.additionalInformation_getQuestion.getText().equals("What is your favorite flower?")) {
				pg.additionalInformation_answer.sendKeys("rose");
			} else if (pg.additionalInformation_getQuestion.getText()
					.equals("What is your favorite sport or outdoor activity to participate in?")) {
				pg.additionalInformation_answer.sendKeys("cricket");
			} else if (pg.additionalInformation_getQuestion.getText()
					.equals("What is the last name of your favorite athlete?")) {
				pg.additionalInformation_answer.sendKeys("tendulkar");
			}

			pg.additionalInformation_submit.click();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (!driver.findElements(By.xpath("//h1[text()='System Error']")).isEmpty()) {
				ev.report("failed", "Test Case ", "Encountered system error! Login failed");
				ev.flush();
				System.exit(0);
			}
		}
		if (pg.Home_title_verify.isDisplayed()) {
			pg.Home_profile_menu_click.click();
			pg.Home_profile_switchProfile_nest_click.click();
			List<WebElement> regions = driver.findElements(By.xpath("//*[@id='linkSwitchProfile']/ul/li/a"));
			System.out.println(regions.size());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			

			for (int i = 0; i < regions.size(); i++) {
				System.out.println(regions.get(i).getText());
				
				if (regions.get(i).getText().contentEquals(region)) {
					regions.get(i).click();	
				}
				
				else if (regions.get(i).getText().contentEquals("First data merchant services - "+region)) {
					regions.get(i).click();	
					break;
				}								
							
			}

			pg.waitForPageLoaded();
			if (pg.Home_title_verify.isDisplayed()) {
				pg.Home_profile_menu_click.click();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				pg.Home_profile_switchProfile_nest_click.click();
				// pg.Home_profile_switchProfile_additionalProfile_Country_LAC_click.click();
				pg.waitForPageLoaded();
				pg.moveCursorOutside();
				pg.Home_title_verify.click();
				pg.waitForPageLoaded();
				pg.Home_CCRT_link_switchWindow_link.click();
				pg.waitForPageLoaded();
				pg.switchtoWindow();

				if (pg.CCRT_title_Verify.isDisplayed()) {
					bc.scrolldown_javascript();
					pg.CCRTpage_DraftReport_link_click.click();
					pg.waitForPageLoaded();
					for (int i = 0; i < 5; i++) {
						bc.scrollleft_javascript();
					}
					try {
						String x1 = ReusableClass.createXpath("//a[contains(text(),'{0}')]", report);
						driver.findElement(By.xpath(x1)).click();
					} catch (org.openqa.selenium.NoSuchElementException e) {
						ev.report("failed", "Test Case - Report ID not identified in the CCRT portal",
								"|Report ID not identified in the CCRT portal");
						ev.flush();
						System.exit(0);
					}
					pg.waitForPageLoaded();
					if (pg.Reportpage_basicInformation_verify.isDisplayed()) {
						for (int i = 0; i < 4; i++) {
							bc.scrolldown_javascript();
						}
						pg.nextButton.click();
						pg.waitForPageLoaded();
					}
					++count;
					ev.report("passed", "Test Case " + count, "Random message");
					if (pg.Reportpage_deviecConfiguration_verify.isDisplayed()) {
						for (int i = 0; i < 11; i++) {
							bc.scrolldown_javascript();
						}
						pg.nextButton.click();
						pg.waitForPageLoaded();
					}
					bc.scrolldown_javascript();
					bc.scrolldown_javascript();
					// bc.scrolldown_javascript();
					// bc.scrolldown_javascript();
					String excelFilePath = RunFile.classB_EMV_excel_Retrieval;
					Workbook workbook = null;
					FileInputStream inputStream = null;
					try {
						inputStream = new FileInputStream(new File(excelFilePath));
						workbook = new XSSFWorkbook(inputStream);

					} catch (FileNotFoundException e1) {
						System.out.println("file not found");
					} catch (IOException e) {
						e.printStackTrace();
					}

					Sheet loginSheet = workbook.getSheetAt(0);
					correctCVM = "Yes";
					for (int i = 1; i <= loginSheet.getLastRowNum(); i++) {
						testcase = loginSheet.getRow(i).getCell(2).getStringCellValue().replace("_0", " ")
								.replace("_1", " 1").replace("_2", " 2").replace("Test_Case", "Test Case").
								// replace("1a", "1a - v2.3").
								replace(".00000001.report", "").replace(".00000001(2).report", "")
								.replace(".00000001(3).report", "").replace(".00000001(4).report", "").replace("_", "")
								.trim();
						System.out.println("test case " + testcase);

						Online_Select = "Approved";
						authID = loginSheet.getRow(i).getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
								.getStringCellValue().trim();
						RRN = loginSheet.getRow(i).getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
								.getStringCellValue().trim();

						pg.click_javascript("Contactless_Report_Testcase_Edit_click", testcase);
						pg.waitForPageLoaded();
						pg.getandReplace_select("Report_Testcase_Edit_Online_Select", testcase, Online_Select);
						pg.getandReplace_select("Report_Testcase_Edit_Offline_Select", testcase, "N/A");
						pg.getandReplace_select("Report_Testcase_Edit_CorrectCVM_Select", testcase, correctCVM);
						pg.getandReplace_select("Report_Testcase_Edit_CorrectMessage_Select", testcase, "Yes");

						if (!RRN.isEmpty()) {
							pg.enter_with_replace("Contactless_Report_Testcase_Edit_RRN_enter", testcase, RRN);
						}
						if (!authID.isEmpty()) {
							pg.enter_with_replace("Contactless_Report_Testcase_Edit_Approval_enter", testcase, authID);
						}
						pg.enter_with_replace("Contactless_Report_Testcase_Edit_Comment_enter", testcase,
								"Transaction approved");

						pg.waitForPageLoaded();
						try {
							pg.getandReplace_click("Contactless_Report_Testcase_Edit_Save_button", testcase);
							if (!driver.findElements(By.xpath("//h2[text()='Data cannot be saved']")).isEmpty()) {
								pg.Save_notsaved_button_click.click();
							}

							if (!driver
									.findElements(
											By.xpath("//h2[@id='swal2-title']//following::button[text()='Close']"))
									.isEmpty()) {
								pg.Save_notsaved_button_click.click();
							}

						} catch (Exception e) {

						}

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
						}

						// catch block e.printStackTrace(); }
						pg.waitForPageLoaded();
						bc.scrolldown_javascript();
					}

				}
				rpt.reporting2();

				// List file directory **/
				String directory = localdirectoryCDET;
				System.out.println("Filepath is: " + directory);

				File file = new File(directory);
				String[] directories = file.list(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						return new File(current, name).isDirectory();
					}
				});
				System.out.println(Arrays.toString(directories));

				HashMap<String, String> testcaselist = new LinkedHashMap<String, String>();
				for (String string : directories) {
					string = string.replace("_", " ");
					string = string.replace("01 ", "1");
					string = string.replace("Test Case 0", "Test Case ");
					string = string + "- v2.3";
					if (string.startsWith("Test"))
						testcaselist.put(string, null);
				}

				TreeMap<String, String> map = new TreeMap<String, String>();

				for (String string : directories) {
					map.put(string, null);
				}
				map.remove(".index");
				map.remove("Contactless");
				for (String testcasefolder : directories) {
					System.out.println(directory + testcasefolder);

					File root = new File(directory + "\\" + testcasefolder);
					try {
						boolean recursive = true;

						Collection files = FileUtils.listFiles(root, null, recursive);

						for (Iterator iterator = files.iterator(); iterator.hasNext();) {
							File file1 = (File) iterator.next();
							String sourceFilename = file1.getName();
							String ext1 = FilenameUtils.getExtension(sourceFilename);
							if (sourceFilename.contains(".amex") && ext1.contains("xml")) {

								for (Entry<String, String> string2 : map.entrySet()) {
									if (string2.getKey().contains(testcasefolder)) {
										map.put(testcasefolder, file1.getAbsolutePath());
									}
								}
								System.out.println(file1.getAbsolutePath());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				for (Entry<String, String> e : map.entrySet()) {
					System.out.println(e.getKey() + " " + e.getValue());
				}

				/** Upload trace files **/

				pg.UploadFileBtn.click();
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e3) {
//					// TODO Auto-generated catch block
//					e3.printStackTrace();
//				}
				WebDriverWait wait = new WebDriverWait(driver, 15);
				wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(By.xpath("//*[@id='CdetDocumentType']"),
						By.tagName("option")));
				Select sl = new Select(driver.findElement(By.xpath("//*[@id='CdetDocumentType']")));
				sl.selectByVisibleText("EMVCo L3 – Card to Terminal Log File");
				sl.selectByIndex(0);

				for (Entry<String, String> m : map.entrySet()) {
					String key = m.getKey();
					String path = m.getValue();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}
//					wait.until(ExpectedConditions.elementToBeClickable(pg.UploadBtnCDET));
					pg.BrowseBtnCDET.sendKeys(path);
					key = key.replace("_", " ");
					key = key.replace("01 ", "1");
					key = key.replace("Test Case 0", "Test Case ");
					key = key + "a - v2.3";
					// System.out.println(key);

					try {
//			Select se = new Select(driver.findElement(By.xpath("//*[@id='CdetTestCaseResultId']")));
//			se.selectByVisibleText(key);

//						Select sl = new Select(driver.findElement(By.xpath("//*[@id='CdetDocumentType']")));
//						sl.selectByVisibleText("EMVCo L3 – Card to Terminal Log File");
//						sl.selectByIndex(0);
//						
						List<WebElement> testcaseList = driver
								.findElements(By.xpath("//*[@id='CdetTestCaseResultId']/option"));
						for (WebElement ele : testcaseList) {
							String currentOption = ele.getText();
							if (currentOption.contains(key)) {
								ele.click();
								break;
							}
						}
						wait.until(ExpectedConditions.elementToBeClickable(pg.UploadBtnCDET));
						pg.UploadBtnCDET.click();

					} catch (org.openqa.selenium.ElementClickInterceptedException e) {
						ev.report("passed", "Test Case - " + key + "File upload failed",
								"ElementClickInterceptedException exception");
					}
					wait.until(ExpectedConditions.elementToBeClickable(pg.CloseBtn));
					pg.CloseBtn.click();
					wait.until(ExpectedConditions.elementToBeClickable(pg.CloseUploadWindow));
				}
				pg.CloseUploadWindow.click();
				// pg.NextBtn.click();

			}
			ev.flush();
		}
		try {
			ScreenRecorderUtil.stopRecord();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
