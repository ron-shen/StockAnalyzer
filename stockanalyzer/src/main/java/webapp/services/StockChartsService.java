package webapp.services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import webapp.components.DateProvider;
import webapp.entities.Stock;
import webapp.repositories.StockRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class StockChartsService {
    private HashSet<String> industries = new HashSet<String>();
    private ChromeDriver driver;
    private StockRepository stockRepository;
    private HashMap<String, String> industryActualNameToScanName = new HashMap<>();
    private DateProvider dateProvider;

    public HashSet<String> getIndustries() {
        return industries;
    }
    public ChromeDriver getDriver() {
        return driver;
    }
    public StockChartsService(ChromeDriver driver, StockRepository stockRepository,
                              DateProvider dateProvider) {
        this.driver = driver;
        this.stockRepository = stockRepository;
        this.dateProvider = dateProvider;
        addAllIndustries();
        initIndustryActualNameToScanName();
    }


    public void login(String username, String password){
        driver.get("https://stockcharts.com/login/index.php");
        WebElement usernameField = driver.findElement(By.id("form_UserID"));
        WebElement passwordField = driver.findElement(By.id("form_UserPassword"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        WebElement loginButton = driver.findElement(By.tagName("button"));
        loginButton.click();
//        String currentUrl = driver.getCurrentUrl();
//        if (currentUrl.equals("https://stockcharts.com/def/servlet/Favorites.CServlet")) {
//            System.out.println("Login successful. Redirected to the dashboard.");
//        } else {
//            System.out.println("Login failed. Not redirected to the expected page.");
//        }
    }
    public Set<Stock> scan(String[] industries){
        checkValidIndustryName(industries);
        convertIndustryName(industries);
        driver.get("https://stockcharts.com/def/servlet/ScanUI");
        WebElement optionButton = driver.findElement(By.xpath("//option[text()='weekly scan']"));
        optionButton.click();
        WebElement scanButton = driver.findElement(By.id("runScan"));
        scanButton.click();
        //click Run Scan Button opens the scan result in a new tab
        //we have to switch to that window to get the result
        String originalWindowHandle = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(originalWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        WebElement entriesButton = driver.findElement(By.xpath("//option[text()='All']"));
        entriesButton.click();
        List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr"));
        return filterScanResult(elements, industries);
    }

    public List<Stock> updateStockList(Set<Stock> scannedStocks){
        //compare Stockcharts scanned stocks and stocks saved in DB
        //If a stock in scan result but not in DB, add it
        //If a stock in scan result and in DB, update it
        //If a stock not in scan result but in DB, remove it
        List<Stock> savedStocks = stockRepository.findAll();
        Set<Stock> commonStocks = new HashSet<>();
        List<Stock> notInScanStocks = new ArrayList<>();
        for (Stock savedStock : savedStocks) {
            if (scannedStocks.contains(savedStock)) {
                commonStocks.add(savedStock);
                savedStock.setNewScanned(false);
            }
            else{
                notInScanStocks.add(savedStock);
            }
        }
        savedStocks.removeAll(notInScanStocks);
        scannedStocks.removeAll(commonStocks);
        savedStocks.addAll(scannedStocks);
        stockRepository.saveAll(savedStocks);
        return savedStocks;
    }

    //filter the stocks based on the industries interested
    private Set<Stock> filterScanResult(List<WebElement> elements, String[] industries){
        Set<Stock> stocks = new HashSet<>();
        Set<String> industriesSet = new HashSet<>(Arrays.asList(industries));
        LocalDate dateNow = dateProvider.getCurrentDate();
        for(WebElement element : elements){
            String industry = element.findElement(By.xpath("td[6]")).getText();
            if(industriesSet.contains(industry)){
                String symbol = element.findElement(By.xpath("td[2]/span[@class='symlink']")).getText();
                String sector = element.findElement(By.xpath("td[5]")).getText();
                Stock stock = new Stock(symbol, sector, industry, dateNow, true);
                stocks.add(stock);
            }
        }
        return stocks;
    }

    private void addAllIndustries(){
        //XLK
        industries.add("Renewable Energy Equipment");
        industries.add("Computer Hardware");
        industries.add("Semiconductors");
        industries.add("Electrical Components & Equipment"); //Electrical Components;
        industries.add("Software");
        industries.add("Electronic Equipment");
        industries.add("Telecom Equipment"); // Telecommunications Equipment;
        industries.add("Computer Services");
        //XLY
        industries.add("Recreational Services");
        industries.add("Broadline Retailers");
        industries.add("Business Training & Employment Agencies"); //Business Training Agencies
        industries.add("Apparel Retailers");
        industries.add("Specialty Retailers");
        industries.add("Travel & Tourism");
        industries.add("Automobiles");
        industries.add("Gambling");
        industries.add("Clothing & Accessories");
        industries.add("Auto Parts");
        industries.add("Toys");
        industries.add("Recreational Products");
        industries.add("Hotels");
        industries.add("Home Improvement Retailers");
        industries.add("Restaurants & Bars");
        industries.add("Specialized Consumer Services"); //Special Consumer Services
        industries.add("Home Construction");
        industries.add("Furnishings");
        industries.add("Tires");
        industries.add("Durable Household Products");
        industries.add("Footwear");
        //XLU
        industries.add("Water");
        industries.add("Conventional Electricity");
        industries.add("Multiutilities");
        industries.add("Gas Distribution");
        //XLV
        industries.add("Medical Supplies");
        industries.add("Medical Equipment");
        industries.add("Pharmaceuticals");
        industries.add("Health Care Providers");
        industries.add("Biotechnology");
        //XLC
        industries.add("Internet");
        industries.add("Mobile Telecommunications");
        industries.add("Broadcasting & Entertainment"); //Entertainment;
        industries.add("Media Agencies");
        industries.add("Fixed Line Telecommunications"); //Fixed Telecommunications
        industries.add("Publishing");
        //XLB
        industries.add("Paper");
        industries.add("Gold Mining");
        industries.add("Nonferrous Metals");
        industries.add("Mining"); //General Mining
        industries.add("Specialty Chemicals");
        industries.add("Containers & Packaging");
        industries.add("Commodity Chemicals");
        industries.add("Aluminum");
        industries.add("Steel");
        //XLF
        industries.add("Financial Administration");
        industries.add("Reinsurance");
        industries.add("Consumer Finance");
        industries.add("Property & Casualty Insurance"); //Property-Casualty Insurance
        industries.add("Insurance Brokers");
        industries.add("Specialty Finance");
        industries.add("Full Line Insurance");
        industries.add("Life Insurance");
        industries.add("Banks");
        industries.add("Asset Managers");
        industries.add("Mortgage Finance");
        industries.add("Investment Services");
        //XLP
        industries.add("General Retailers");
        industries.add("Nondurable Household Products"); //Nondurable Home Products
        industries.add("Food Retailers & Wholesalers");
        industries.add("Personal Products");
        industries.add("Food Products");
        industries.add("Drug Retailers");
        industries.add("Soft Drinks");
        industries.add("Tobacco");
        industries.add("Brewers");
        industries.add("Distillers & Vintners");
        //XLI
        industries.add("Marine Transportation");
        industries.add("Airlines");
        industries.add("Waste & Disposal Services");
        industries.add("Commercial Vehicles & Trucks"); //Commercial Vehicles
        industries.add("Diversified Industrials");
        industries.add("Industrial Machinery");
        industries.add("Delivery Services");
        industries.add("Building Materials & Fixtures"); //Building Materials
        industries.add("Transportation Services");
        industries.add("Defense");
        industries.add("Heavy Construction");
        industries.add("Business Support Services");
        industries.add("Railroad");
        industries.add("Aerospace");
        industries.add("Trucking");
        //XLE
        industries.add("Oil Equipment & Services");
        industries.add("Exploration & Production");
        industries.add("Integrated Oil & Gas");
        industries.add("Pipelines");
        industries.add("Coal");
        //XLRE
        industries.add("Industrial & Office REITs");
        industries.add("Hotel & Lodging REITs");
        industries.add("Real Estate Holding & Development"); //Real Estate Development
        industries.add("Residential REITs");
        industries.add("Specialty REITs");
        industries.add("Diversified REITs");
        industries.add("Retail REITs");
        industries.add("Mortgage REITs");
        industries.add("Real Estate Services");
    }
    private void initIndustryActualNameToScanName(){
        /**
         * Some industry names in sectors summary are different from that in scan result
         * e.g. Electrical Components & Equipment in sector summary,
         * but it is Electrical Components in the scan result.
         * so we have to map those names.
         */
        industryActualNameToScanName.put("Electrical Components & Equipment", "Electrical Components");
        industryActualNameToScanName.put("Telecom Equipment", "Electrical Components");
        industryActualNameToScanName.put("Business Training & Employment Agencies", "Business Training Agencies");
        industryActualNameToScanName.put("Specialized Consumer Services", "Special Consumer Services");
        industryActualNameToScanName.put("Broadcasting & Entertainment", "Entertainment");
        industryActualNameToScanName.put("Fixed Line Telecommunications", "Fixed Telecommunications");
        industryActualNameToScanName.put("Property & Casualty Insurance", "Property-Casualty Insurance");
        industryActualNameToScanName.put("Nondurable Household Products", "Nondurable Home Products");
        industryActualNameToScanName.put("Commercial Vehicles & Trucks", "Commercial Vehicles");
        industryActualNameToScanName.put("Building Materials & Fixtures", "Building Materials");
        industryActualNameToScanName.put("Real Estate Holding & Development", "Real Estate Development");
    }
    private void checkValidIndustryName(String[] industries){
        for(String industry: industries){
            if(!this.industries.contains(industry)){
                System.out.println(industry + "is not a valid name");
                return;
            }
        }
    }
    private void convertIndustryName(String[] industries){
        for(int i = 0; i < industries.length; i++){
            if(this.industryActualNameToScanName.containsKey(industries[i])){
                industries[i] = industryActualNameToScanName.get(industries[i]);
            }
        }
    }
}
