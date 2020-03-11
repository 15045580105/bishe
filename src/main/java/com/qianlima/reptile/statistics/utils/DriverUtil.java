package com.qianlima.reptile.statistics.utils;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @program: spider
 * @description: 浏览器启动类
 * @author: Zhao Xiaoyang
 * @create: 2019-03-14 11:27
 **/
@Component
public class DriverUtil {

    @Resource
    HttpRequestUtil httpRequestUtil;

    public  WebDriver getDriver(WebDriver driver,int use_proxy){
        Properties prop = System.getProperties();
        String proxyUrl = null;
        if(use_proxy==1){
          proxyUrl = httpRequestUtil.getProxyIp();
        }
        if(proxyUrl!=null){
            prop.setProperty("http.proxyHost", StringUtils.substringBefore(proxyUrl,":"));
            prop.setProperty("http.proxyPort", StringUtils.substringAfter(proxyUrl,":"));
        }
        String os = prop.getProperty("os.name");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));

        if (os.startsWith("Win") || os.startsWith("win")) {
            System.setProperty("webdriver.chrome.driver", "C:\\CrawlerDrivers/chrome/chromedriver.exe");
        } else if (os.equals("Unix") || os.equals("Linux")) {
            System.setProperty("webdriver.chrome.driver", "/opt/webdriver/chromedriver");
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
        }
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        /*cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
                UnexpectedAlertBehaviour.IGNORE);*/
        cap.setCapability("goog:chromeOptions", options);

        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable("performance", Level.ALL);
        cap.setCapability("loggingPrefs", logPrefs);
        if (driver == null) {
            driver = new ChromeDriver(cap);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        return driver;
    }
}
