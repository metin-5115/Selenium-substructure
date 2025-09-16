package pages.hotel.hotelList;

import Helper.Helper;
import exception.ScenarioInfoException;
import factory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import pages.hotel.hotelList.constants.HotelListPageConstants;

import java.util.ArrayList;
import java.util.List;

public class HotelListPage extends HotelListPageConstants {
    Helper helper = new Helper(DriverFactory.getDriver());


    public HotelListPage(WebDriver driver) {
        super(driver);
    }

    public void iCheckHotelListPageTitle(){
        wait(1);
        WebElement element = find(hotelListPageTitle);
        waitVisibility(element);
    }
//    public int iGetRandomHotelIndex(){
//        int randomIndex = helper.createRandomNumber(14);
//        System.out.println("Random index: "+randomIndex);
//        return randomIndex;
//    }

    public void iWaitLoadForHotelList(){
        int i=0;
        do{
            wait(1);
            i++;
        }while (isElementExist(hotelListPreload) && i<65);
    }

    public List<String> iCheckNameInTheHotelCards(){
        List<WebElement> list = finds(hotelNameList);
        waitVisibility(list.get(0));
        List<String> hotelNameStringList = new ArrayList<>();
        for (WebElement element : list){
            String hotelName = getElementText(element);
            hotelNameStringList.add(hotelName);
        }
        return hotelNameStringList;
    }
    public List<String> iCheckCountryInTheHotelCards(){
        List<WebElement> list = finds(hotelCountryList);
        waitVisibility(list.get(0));
        List<String> hotelCountryStringList = new ArrayList<>();
        for (WebElement element : list){
            String hotelName = getElementText(element);
            hotelCountryStringList.add(hotelName);
        }
        return hotelCountryStringList;
    }
    public List<String> iCheckHotelCityInTheHotelCards(){
        List<WebElement> list = finds(hotelCityList);
        waitVisibility(list.get(0));
        List<String> hotelProvinceList = new ArrayList<>();
        for (WebElement element : list){
            String hotelProvince = getElementText(element);
            hotelProvinceList.add(hotelProvince);
        }
        return hotelProvinceList;
    }

    public List<String> iCheckHotelCountyInTheHotelCards(){
        List<WebElement> list = finds(hotelCountyList);
        waitVisibility(list.get(0));
        List<String> hotelLocationList = new ArrayList<>();
        for (WebElement element : list){
            String hotelLocation = getElementText(element);
            hotelLocationList.add(hotelLocation);
        }
        return hotelLocationList;
    }

    public String iGetHotelNameInTheHotelListPage(int hotelNumber){
        List<WebElement> list = finds(hotelNameList);
        String hotelName;
        try {
            waitVisibility(list.get(hotelNumber));
            hotelName = getElementText(list.get(hotelNumber));
        }catch (org.openqa.selenium.StaleElementReferenceException e){
            wait(2);
            hotelName = getElementText(finds(hotelNameList).get(hotelNumber));
        }
        return hotelName;
    }
    public String iGetHotelPersonCountInTheHotelListPage(int randomNumber){
        List<WebElement> list = finds(hotelPersonCountList);
        String hotelPersonCount = list.get(randomNumber).getText();
        return hotelPersonCount;
    }
    public String iGetHotelAccommodationDayInTheHotelListPage(int randomNumber){
        List<WebElement> list = finds(hotelAccommodationDayList);
        for (WebElement element : list) {
            System.out.println(element.getText());
        }
        String hotelAccommodationDay = list.get(randomNumber).getText();
        String[] split = hotelAccommodationDay.split("/");
        return split[0];
    }
    public String iGetHotelPriceInTheHotelListPage(int randomNumber){
        List<WebElement> list = finds(hotelPriceList);

        String hotelPrice = list.get(randomNumber).getText();
        return hotelPrice;
    }

    public String iGetHotelPriceDecimalInTheHotelListPage(int randomNumber){
        String hotelPrice = "";
        int number = randomNumber;
        if (isElementExist(By.xpath("//div[contains(@class,'hotel-container')]["+number+"]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'hotelCard-price')]//div[@class='price-decimal']"))){
            WebElement element = find(By.xpath("//div[contains(@class,'hotel-container')]["+number+"]//div[contains(@class,'hotelCard-right')]//div[contains(@class,'hotelCard-price')]//div[@class='price-decimal']"));
            hotelPrice = element.getText();
        }
        return hotelPrice;
    }

    public void iClickShowMoreHotel(){
        while (isElementExist(showMoreHotel)){
            clickJS(showMoreHotel);
            wait(1);
        }
        waitVisibility(listedAllHotel);
    }

    public List<String> iGetBreadCrumb(){
        List<String> list = new ArrayList<>();
        List<WebElement> list2 = finds(hotelBreadcrumb);
        for (WebElement element : list2){
            list.add(element.getText());
        }
        return list;
    }

    public int iGetContentCount(){
        List<WebElement> firstHotelPaginationCount;
        List<WebElement> secondHotelPaginationCount;
        String hotelName;
        String hotelName2;
        do {
            firstHotelPaginationCount = finds(hotelPaginationCount);
            hotelName = firstHotelPaginationCount.get(firstHotelPaginationCount.size()-1).getText();
            wait(3);
            secondHotelPaginationCount = finds(hotelPaginationCount);
            hotelName2 = secondHotelPaginationCount.get(secondHotelPaginationCount.size()-1).getText();
        }while (firstHotelPaginationCount.size() != secondHotelPaginationCount.size() && !hotelName.equalsIgnoreCase(hotelName2));
        return secondHotelPaginationCount.size();
    }
    public List<String> iClickInspectHotelButton(int randomNumber){
        List<WebElement> inspectHotelButton = finds(inspectHotel);
        List<WebElement> inspectHotel = finds(inspectHotel2);
        List<String> hotelInformation = new ArrayList<>();

        int count=1;
        while (true) {
            String elementText = inspectHotel.get(randomNumber).getText();

            String pageText = "Seçtiğiniz tarihlerde fiyat bilgisi bulunmamaktadır.\n" +
                    "444 10 31 numaralı telefonu arayarak bilgi alabilirsiniz.";



            int result=count-randomNumber;
            if (!elementText.replaceAll("\\s+", " ").equals(pageText.replaceAll("\\s+", " "))&& randomNumber==0) {
                // Fiyat bilgisi varsa işlemleri yap

                hotelInformation.add(iGetHotelNameInTheHotelListPage(randomNumber));
                hotelInformation.add(iGetHotelAccommodationDayInTheHotelListPage(randomNumber));
                hotelInformation.add(iGetHotelPriceInTheHotelListPage(randomNumber));
                waitVisibility(inspectHotelButton.get(randomNumber));
                clickJS(inspectHotelButton.get(randomNumber));
                return hotelInformation; // Fiyat bilgisi bulunan otel bilgilerini döndür
            }
            else if (!elementText.replaceAll("\\s+", " ").equals(pageText.replaceAll("\\s+", " "))&& randomNumber!=0) {
                // Fiyat bilgisi varsa işlemleri yap

                hotelInformation.add(iGetHotelNameInTheHotelListPage(randomNumber));
                hotelInformation.add(iGetHotelAccommodationDayInTheHotelListPage(result));
                hotelInformation.add(iGetHotelPriceInTheHotelListPage(result));
                waitVisibility(inspectHotelButton.get(randomNumber));
                clickJS(inspectHotelButton.get(randomNumber));
                return hotelInformation; // Fiyat bilgisi bulunan otel bilgilerini döndür
            }
            count++;
            randomNumber++;


            if (randomNumber >= inspectHotel.size()) {
                System.out.println("Tüm oteller kontrol edildi. Fiyat bilgisi bulunan bir otel bulunamadı.");
                break;
            }
        }

// Buraya geldiğinde artık fiyat bilgisi olan bir otel bulunmuş demektir.
        /*hotelInformation.add(iGetHotelNameInTheHotelListPage(randomNumber));
        hotelInformation.add(iGetHotelAccommodationDayInTheHotelListPage(randomNumber));
        hotelInformation.add(iGetHotelPriceInTheHotelListPage(randomNumber));
        waitVisibility(inspectHotelButton.get(randomNumber));
        clickJS(inspectHotelButton.get(randomNumber));*/


        /*if (!inspectHotel.get(randomNumber).getText().contains("Seçtiğiniz tarihlerde fiyat bilgisi bulunmamaktadır.")){
            waitVisibility(inspectHotelButton.get(randomNumber));
            clickJS(inspectHotelButton.get(randomNumber));
        }else{
            randomNumber++;
            hotelInformation.add(iGetHotelNameInTheHotelListPage(randomNumber));
            //hotelInformation.add(iGetHotelPersonCountInTheHotelListPage(randomNumber));
            hotelInformation.add(iGetHotelAccommodationDayInTheHotelListPage(randomNumber));
            hotelInformation.add(iGetHotelPriceInTheHotelListPage(randomNumber));
            waitVisibility(inspectHotelButton.get(randomNumber));
            clickJS(inspectHotelButton.get(randomNumber));
        }*/
        return hotelInformation;
    }

    public void iGetChangeDateButton(){
        while(!isElementExist(changeDateButton) && isElementExist(showMoreHotel)){
            clickJS(showMoreHotel);
            wait(1);
        }
        try{
            waitVisibilityWithSecond(changeDateButton,3);
        }catch (ScenarioInfoException e){
            throw new SkipException("TARİH DEĞİŞTİR BUTONU BULUNAMADI!");
        }
    }

    public List<String> iGetNotFoundPriceText(){
        List<String> text = getAllElementText(notFoundPriceText);
        return text;
    }
}
