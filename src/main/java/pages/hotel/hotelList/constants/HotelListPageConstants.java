package pages.hotel.hotelList.constants;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotelListPageConstants extends BasePage {
    public By hotelListPageTitle = By.xpath("//div[contains(@class,'filter-title')]/h1");
    public By hotelPaginationCount = By.xpath("//div[@class='hotel-container']//div[@class='hotelCard-area']//h2/a");
    public By hotelListPreload = By.xpath("//span[contains(@class,'d-none')]//span[contains(@class,'spinner-border')]");

    //HOTEL CARD LEFT
    public By hotelNameList = By.xpath("//div[contains(@class,'hotelCard')]//div[@class='hotelCard-heading']//h2");
    public By hotelCountryList = By.xpath("//div[contains(@class,'hotelCard')]//ol/li[contains(@class,'breadcrumb-item')][1]");
    public By hotelCityList = By.xpath("//div[contains(@class,'hotelCard')]//ol/li[contains(@class,'breadcrumb-item')][2]");
    public By hotelCountyList = By.xpath("//div[contains(@class,'hotelCard')]//ol/li[contains(@class,'breadcrumb-item')][3]");

    //HOTEL CARD RIGHT
    public By hotelPriceList = By.xpath("//div//div[@class='hotelCard-area']//div[@class='hotelCard-content']//div[@class='hotelCard-right d-flex flex-column']//div[@class='mb-0']//div[@class='price-info']//div[@class='pax-info']//div[@class='my-1 hotelCard-price lh-1 d-none d-md-flex justify-content-center']//div[@class='fee']//div//div//div[@class='price price--main']");
    public By hotelPriceDecimalList = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'hotelCard-price')]//div[@class='price-decimal']");
    public By notFoundPriceText = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'hotelCard-price')]//small");
    public By hotelAccommodationDayList = By.xpath("//span[@class='room-days']");
    public By hotelPersonCountList = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'pax-info')]/span/span[2]");
    public By unavailableHotel = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'btn-wrapper')]/p");
    public By changeDateButton = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'btn-wrapper')]/span");
    public By hotelBreadcrumb = By.xpath("//div[@class='hotel-container']//div[contains(@class,'hotelCard-heading')]//ol");
    public By showMoreHotel = By.xpath("//button[normalize-space()='Daha Fazla Otel Göster']");
    public By listedAllHotel = By.xpath("//div[@class='filter-list']/div/span");
    public By inspectHotel = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'btn-wrapper')]/a/span");
    public By inspectHotel2 = By.xpath("//div[contains(@class,'hotel-container')]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'mb-0')]//div[contains(@class,'price-info')]");

    protected HotelListPageConstants(WebDriver driver) {
        super(driver);
    }
}
