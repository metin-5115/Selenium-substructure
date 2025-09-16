package stepDefinitions.hotel;

import assertion.Assertion;
import assertion.HotelDestination;
import com.jayway.jsonpath.JsonPath;
import exception.ScenarioInfoException;
import factory.DriverFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.testng.SkipException;
import pages.general.flightInformation.FlightInformationPage;
import pages.hotel.hotelList.HotelListPage;
import testContexts.FlightContext;
import testContexts.HotelContext;
import testContexts.TestContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HotelListPageSteps {

    private HotelListPage hotelListPage;
    private FlightInformationPage flightInformationPage;
    private FlightContext flightContext;
    private HotelContext hotelContext;
    private TestContext testContext;
    private int randomNumber = 0;
    public static List<List<String>> hotelListErrors = new ArrayList<>();

    public HotelListPageSteps(HotelContext hotelContext, FlightContext flightContext, TestContext testContext){
        hotelListPage = new HotelListPage(DriverFactory.getDriver());
        flightInformationPage = new FlightInformationPage(DriverFactory.getDriver());
        this.hotelContext = hotelContext;
        this.flightContext = flightContext;
        this.testContext = testContext;
        hotelContext.setHotelRandomNumber(randomNumber);
    }
    //    FLIGHT INFORMATION
    @And("^I check departure flight title with (.*) in the hotel list page$")
    public void iCheckDepartureFlightTitle(String parameter){
        Assertion.assertEquals(flightInformationPage.iGetDepartureFlightTitle(),parameter);
    }
    @And("^I get departure flight time in the hotel list page$")
    public void iGetDepartureFlightTime(){
        String flightText = flightInformationPage.iGetDepartureFlightTime();
//        TODO UÇAK BULUNAMAZSA TARİHLER DEĞİŞECEK
        if (flightText.equalsIgnoreCase("uçak bulunamadı")){
            new SkipException("Aramış olduğunuz tarih kriterine uygun paket bulunamadı");
        }else{
            flightContext.setDepartureFlightTime(flightText);
        }
    }
    @And("^I get departure flight arrival time in the hotel list page$")
    public void iGetDepartureFlightArrivalTime(){
        String flightArriveTime = flightInformationPage.iGetDepartureFlightArrivalTime();
        if (flightArriveTime.contains("Aramış olduğunuz tarih kriterine")){
            try {
                String file = Files.readString(Path.of("src/main/java/json/error.json"));
                String error = JsonPath.parse(file).read("hotel_list.flight_available_error");
                Assertion.assertEquals(error, flightArriveTime);
                throw new SkipException("PAKET İÇİN BU TARİHLERDE UÇAK BULUNAMADI!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            flightContext.setDepartureFlightArrivalTime(flightArriveTime);
        }
    }
    @And("^I get departure airport in the hotel list page$")
    public void iGetDepartureAirport(){
        flightContext.setDepartureAirport(flightInformationPage.iGetDepartureAirport());
    }
    @And("^I get arrival airport in the hotel list page$")
    public void iGetArrivalAirport(){
//       aktarmalı uçuşlar burada tek uçuş gibi test ediliyor.
        List<String> list = flightInformationPage.iGetArrivalAirport();
        flightContext.setArrivalAirport(list.get(list.size()-1));
    }
    @And("^I check return flight title with (.*) in the hotel list page$")
    public void iCheckReturnFlightTitle(String parameter){
        Assertion.assertEquals(flightInformationPage.iGetReturnFlightTitle(),parameter);
    }
    @And("^I get return departure flight time in the hotel list page$")
    public void iGetReturnDepartureFlightTime(){
        flightContext.setReturnDepartureFlightTime(flightInformationPage.iGetReturnDepartureFlightTime());
    }
    @And("^I get return departure flight arrival time in the hotel list page$")
    public void iGetReturnDepartureFlightArrivalTime(){
        flightContext.setReturnDepartureFlightArrivalTime(flightInformationPage.iGetReturnDepartureFlightArrivalTime());
    }
    @And("^I get return departure airport in the hotel list page$")
    public void iGetReturnDepartureAirport(){
        flightContext.setReturnDepartureAirport(flightInformationPage.iGetReturnDepartureAirport());
    }
    @And("^I get return arrival airport in the hotel list page$")
    public void iGetReturnArrivalAirport(){
//       aktarmalı uçuşlar burada tek uçuş gibi test ediliyor.
        List<String> list = flightInformationPage.iGetReturnArrivalAirport();
        flightContext.setReturnArrivalAirport(list.get(list.size()-1));
    }
    @And("^I get transfer information in the hotel list page$")
    public void iGetTransferInformation(DataTable table){
        String transferText = flightInformationPage.iGetTransferInformation();
        if (!transferText.contains("Transfer bulunamadı")){
            Assertion.assertTrue(transferText.contains(table.row(0).get(1)));
        }else{
            Assertion.assertEquals(transferText,table.row(1).get(1));
        }
        testContext.setTransferText(transferText);
    }

    @And("^I check departure transfer information in the hotel list page$")
    public void iGetDepartureTransferInformation(){
        String transferText = flightInformationPage.iGetDepartureTransferInfo();
        if (!transferText.contains("Transfer bulunamadı..")){
            try {
                Assertion.assertTrue(transferText.contains("Özel Transfer"));
            }catch (ScenarioInfoException e){
                Assertion.assertTrue(transferText.contains("Ekonomik Transfer"));
            }
        }else{
            Assertion.assertEquals(transferText,"Transfer bulunamadı..");
        }
        testContext.setTransferText(transferText);
    }

    @And("^I check return transfer information in the hotel list page$")
    public void iGetReturnTransferInformation(){
        String transferText = flightInformationPage.iGetReturnTransferInfo();
        if (!transferText.contains("Transfer bulunamadı")){
            try {
                Assertion.assertTrue(transferText.contains("Özel Transfer"));
            }catch (ScenarioInfoException e){
                Assertion.assertTrue(transferText.contains("Ekonomik Transfer"));
            }
        }else{
            Assertion.assertEquals(transferText,"Transfer bulunamadı..");
        }
        testContext.setTransferText(transferText);
    }
    List<Flight> unsortedFlights = new ArrayList<>();
    List<returnFlight> unsortedFlights2 = new ArrayList<>();
    @And("^I check departure flights in the hotel list page$")
    public void iCheckDepartureFlights(){
        flightInformationPage.iWaitFlights();

        List<String> allPrice;
        List<String> allCompany;
        List<String> allClasses;
        List<String> allTime;
        List<String> specialFlight = new ArrayList<>();
        flightInformationPage.showAllDepartureFlight();
        flightInformationPage.iClickCheckbox();

        allTime=flightInformationPage.getAllFlightTime2();
        allPrice =flightInformationPage.getAllFlightPrice();
        allCompany = flightInformationPage.getAllFlightCompany();
        allClasses = flightInformationPage.getAllFlightClass();
        System.out.println(allPrice);
        System.out.println(allTime);

        for (int i = 0; i < allPrice.size(); i++) {
            unsortedFlights.add(new Flight(Integer.valueOf(allPrice.get(i)), allTime.get(i)));
        }

        // Fiyat ve saat bilgisine göre sıralama
        List<Flight> sortedFlights = new ArrayList<>(unsortedFlights);
        sortedFlights.sort(Comparator
                .comparing(Flight::getPrice) // Fiyata göre sırala
                .thenComparing(Flight::getTime)); // Fiyatlar aynıysa saate göre sırala


        // Sıralanmış listeyi yazdır
        for (int i = 0; i < unsortedFlights.size(); i++) {
            Flight unsorted = unsortedFlights.get(i);
            Flight sorted = sortedFlights.get(i);

            // Elemanları karşılaştır ve assertion ile kontrol et
            if (!unsorted.equals(sorted)) {
                throw new AssertionError("Test başarısız! İndeks " + i + " farklı:\n"
                        + "Sıralanmamış: " + unsorted + "\n"
                        + "Sıralanmış: " + sorted);
            }
        }
        System.out.println(sortedFlights);
        System.out.println(unsortedFlights);
        System.out.println(allClasses);
        for (int i=0; i< allClasses.size(); i++){
            if (allClasses.get(0).equalsIgnoreCase("SPECIAL")){
                break;
            }

            else if (allClasses.get(i).equalsIgnoreCase("SPECIAL")){
                flightInformationPage.iClickFlightCompany(i);
                String ErrorMessage = flightInformationPage.iCheckClassMessage();
                System.out.println(ErrorMessage);
                Assertion.assertEquals(ErrorMessage,"Seçmiş olduğunuz uçuş için diğer uçuşunuz da Pegasus Havayolu seçilmelidir");
                flightInformationPage.iClickFlightCompany(0);
                break;
            }
        }


        for (int i=0; i<allCompany.size(); i++){
            if (allCompany.get(i).equalsIgnoreCase("PEGASUS") && allClasses.get(i).equalsIgnoreCase("SPECIAL")){
                specialFlight.add("True");
            } else {
                specialFlight.add("False");
            }
        }
        flightInformationPage.iClickSaveFlight();
        /*if (specialFlight.contains("True")){
            // Eğer Pegasus uçuşları varsa ve specialFlight listesinde "true" varsa assertion başarısız olur
            System.out.println("GİDİŞ UÇUŞ SINIFINDA SPECİAL VAR");
        } else {
            // Eğer Pegasus uçuşları yoksa buraya girer
            System.out.println("GİDİŞ UÇUŞ SINIFINDA SPECİAL YOK.");
        }*/
    }

    @And("I check flight time in the hotel detail page")
    public void iCheckFlightTimeInTheHotelDetailPage() {
        List<String> allTime;
        allTime=flightInformationPage.getHotelDetailAllFlightTime();
        System.out.println(allTime);
        Flight firstFlight = unsortedFlights.get(0); // unsortedFlights'ın ilk elemanı
        returnFlight firstReturnFlight = unsortedFlights2.get(0); // unsortedFlights2'nin ilk elemanı

        // İlk elemanları karşılaştır
        if (!allTime.get(0).equals(firstFlight.getTime())) {
            throw new AssertionError("allTime'in ilk elemanı unsortedFlights'in ilk elemanıyla eşleşmiyor! "
                    + "allTime[0]: " + allTime.get(0) + ", unsortedFlights[0]: " + firstFlight.getTime());
        }

        // İkinci elemanları karşılaştır
        if (!allTime.get(1).equals(firstReturnFlight.getTime())) {
            throw new AssertionError("allTime'in ikinci elemanı unsortedFlights2'nin ilk elemanıyla eşleşmiyor! "
                    + "allTime[1]: " + allTime.get(1) + ", unsortedFlights2[0]: " + firstReturnFlight.getTime());
        }
    }

    @And("^I check return flights in the hotel list page$")
    public void iCheckReturnFlights(){
        List<String> allCompany;
        List<String> allClasses;
        List<String> allPrice;
        List<String> allTime;
        List<String> specialFlight = new ArrayList<>();

        flightInformationPage.showAllReturnFlight();
        flightInformationPage.iClickCheckbox();

        allTime=flightInformationPage.getAllFlightTime3();
        allPrice =flightInformationPage.getAllFlightPrice();
        allCompany = flightInformationPage.getAllFlightCompany();
        allClasses = flightInformationPage.getAllFlightClass();
        System.out.println(allTime);
        System.out.println(allPrice);

        for (int i = 0; i < allPrice.size(); i++) {
            unsortedFlights2.add(new returnFlight(Integer.valueOf(allPrice.get(i)), allTime.get(i)));
        }

        // Fiyat ve saat bilgisine göre sıralama
        List<returnFlight> sortedFlights = new ArrayList<>(unsortedFlights2);
        sortedFlights.sort(Comparator
                .comparing(returnFlight::getPrice) // Fiyata göre sırala
                .thenComparing(returnFlight::getTime, Comparator.reverseOrder())); // Fiyatlar aynıysa saate göre sırala


        // Sıralanmış listeyi yazdır
        for (int i = 0; i < unsortedFlights2.size(); i++) {
            returnFlight unsorted = unsortedFlights2.get(i);
            returnFlight sorted = sortedFlights.get(i);

            // Elemanları karşılaştır ve assertion ile kontrol et
            if (!unsorted.equals(sorted)) {
                throw new AssertionError("Test başarısız! İndeks " + i + " farklı:\n"
                        + "Sıralanmamış: " + unsorted + "\n"
                        + "Sıralanmış: " + sorted);
            }
        }
        System.out.println(sortedFlights);
        System.out.println(unsortedFlights2);

        /*List<Integer> sortedPricesAsc = allPrice.stream()
                .map(Integer::valueOf)
                .sorted()
                .toList();
        System.out.println("Azdan çoka doğru sıralanmış hali: " + sortedPricesAsc);
        for(int i =0; i < allPrice.size(); i++){
            int firstSortedPrice = sortedPricesAsc.get(0); // Sıralı listenin ilk elemanı
            int firstOriginalPrice = Integer.valueOf(allPrice.get(0)); // Orijinal listenin ilk elemanını Integer'a çevir

// Assertion: Eğer eşit değilse test fail olur
            Assertion.assertEquals(firstOriginalPrice,firstSortedPrice);

        }*/


        for (int i=0; i<allCompany.size(); i++){
            if (allCompany.get(i).equalsIgnoreCase("PEGASUS") && allClasses.get(i).equalsIgnoreCase("SPECIAL")){
                specialFlight.add("True");
            } else {
                specialFlight.add("False");
            }
        }
        flightInformationPage.iClickSaveFlight();
        if (specialFlight.contains("True")){
            // Eğer Pegasus uçuşları varsa ve specialFlight listesinde "true" varsa assertion başarısız olur
            System.out.println("DÖNÜŞ UÇUŞ SINIFINDA SPECİAL VAR");
        } else {
            // Eğer Pegasus uçuşları yoksa buraya girer
            System.out.println("DÖNÜŞ UÇUŞ SINIFINDA SPECİAL YOK.");
        }
    }

    @And("^I check hotel list page title")
    public void iCheckHotelListPageTitle(){
        hotelListPage.iCheckHotelListPageTitle();
    }

    @And("^I wait load for hotel list")
    public void iWaitLoadForHotelList(){
        hotelListPage.iWaitLoadForHotelList();
        long endTime = System.currentTimeMillis();
        long loadTimeInMilliseconds = endTime - testContext.getStartTime();
        double loadTimeInSeconds = (double) loadTimeInMilliseconds / 1000;

        System.out.println("********************");
        System.out.println(testContext.getHomePageSearchArea());
        System.out.println("Sayfa yükleme süresi: " + loadTimeInSeconds + " saniye");
        System.out.println("********************");
    }

    @And("^I click show more hotel button")
    public void iClickShowMoreHotel(){
        hotelListPage.iClickShowMoreHotel();
    }

    @And("^I check hotel country with (.*) in the hotel cards")
    public void iCheckCountryInTheHotelCards(String parameter) {
        List<String> hotelCountryList = hotelListPage.iCheckCountryInTheHotelCards();
        boolean isTrue = false;
        for (String hotelCountry : hotelCountryList){
            if (hotelCountry.equalsIgnoreCase(parameter)){
                isTrue = true;
            }
        }
        Assertion.assertTrue(isTrue);
    }

    @And("^I check hotel city with (.*) in the hotel cards")
    public void iCheckHotelCityInTheHotelCards(String parameter) {
        List<String> hotelcityList = hotelListPage.iCheckHotelCityInTheHotelCards();
        boolean isTrue = false;
        for (String hotelCity : hotelcityList){
            if (hotelCity.equalsIgnoreCase(parameter)){
                isTrue = true;
            }
        }
        Assertion.assertTrue(isTrue);
    }

    @And("^I check hotel county with (.*) in the hotel cards")
    public void iCheckHotelCountyInTheHotelCards(String parameter) {
        List<String> hotelCountyList = hotelListPage.iCheckHotelCountyInTheHotelCards();
        boolean isTrue = false;
        for (String hotelCounty : hotelCountyList){
            if (hotelCounty.equalsIgnoreCase(parameter)){
                isTrue = true;
            }
        }
        Assertion.assertTrue(isTrue);
    }

    @And("^I check breadcrumb (.*) (.*) (.*) (.*)")
    public void iCheckBreadCrumb(String location,String country,String city, String county){
        List<String> hotelDestination = hotelListPage.iGetBreadCrumb();
        List<String> hotelNameList = hotelListPage.iCheckNameInTheHotelCards();
        List<String> wrongHotelDestination = new ArrayList<>();
        int i=0;
        for (String element : hotelDestination) {
            String[] split = element.split("\\n");
            if (!split[0].equalsIgnoreCase(country) || !split[1].equalsIgnoreCase(city) || !split[2].equalsIgnoreCase(county)) {
                String expect = country + ">" + city + ">" + county;
                String actual = split[0] + ">" + split[1] + ">" + split[2];
                wrongHotelDestination.add(location);
                wrongHotelDestination.add(hotelNameList.get(i));
                wrongHotelDestination.add(expect);
                wrongHotelDestination.add(actual);
                HotelDestination.countyHotelList.add(wrongHotelDestination);
            }
            i++;
        }
    }

    @And("^I get random hotel name in the hotel list")
    public void iGetHotelNameInTheHotelListPage(){
//        int hotelCount = hotelListPage.iGetContentCount();
//        randomNumber = new Random().nextInt(hotelCount);
        hotelListPage.iWaitLoadForHotelList();
        if(testContext.getHomePageSearchArea().contains("Abant")){
            randomNumber = 2;
        }
        hotelContext.setHotelRandomNumber(randomNumber);
        String hotelName = hotelListPage.iGetHotelNameInTheHotelListPage(randomNumber);
        hotelContext.setHotelName(hotelName);
    }
    @And("^I get random hotel name for unavailable hotel check")
    public void iGetHotelNameIForUnavailableHotelCheck(){
        if(testContext.getHomePageSearchArea().contains("Abant")){
            randomNumber = 2;
        }
        hotelContext.setHotelRandomNumber(randomNumber);
        String hotelName = hotelListPage.iGetHotelNameInTheHotelListPage(randomNumber);
        hotelContext.setHotelName(hotelName);

    }
    @And("^I get random hotel person count in the hotel list")
    public void iGetHotelPersonCountInTheHotelListPage(){
        String hotelPersonCount = hotelListPage.iGetHotelPersonCountInTheHotelListPage(randomNumber);
        hotelContext.setHotelPersonCount(hotelPersonCount);
    }
    @And("^I get random hotel accommodation day in the hotel list")
    public void iGetHotelAccommodationDayInTheHotelListPage(){
        String hotelAccommodationDay = hotelListPage.iGetHotelAccommodationDayInTheHotelListPage(randomNumber);
        hotelContext.setHotelAccommodationDay(hotelAccommodationDay);
    }
    @And("^I get random hotel price in the hotel list")
    public void iGetHotelPriceInTheHotelListPage(){
        String hotelPrice = hotelListPage.iGetHotelPriceInTheHotelListPage(randomNumber);
        String hotelPriceDecimal = hotelListPage.iGetHotelPriceDecimalInTheHotelListPage(randomNumber);
        hotelContext.setHotelPrice(hotelPrice+hotelPriceDecimal);
    }
    @And("^I click random hotel inspect button in the hotel list")
    public void iClickRandomHotelInspectButtonInTheHotelListPage(){
        List<String> hotelInformation = hotelListPage.iClickInspectHotelButton(randomNumber);
        if (hotelInformation.size()>0){
            hotelContext.setHotelName(hotelInformation.get(0));

            hotelContext.setHotelAccommodationDay(hotelInformation.get(1));
            hotelContext.setHotelPrice(hotelInformation.get(2));
        }
    }
    @And("^I get change date button in the hotel list")
    public void iGetChangeDateButton(){
        hotelListPage.iGetChangeDateButton();
    }
    @And("^I check not found price text in the hotel list")
    public void iCheckNotFoundPrice(){
        List<String> list = hotelListPage.iGetNotFoundPriceText();
        String pageText = "Seçtiğiniz tarihlerde fiyat bilgisi bulunmamaktadır.\n" +
                "444 10 31 numaralı telefonu arayarak bilgi alabilirsiniz.";
        for (String text : list){
            Assertion.assertEquals(pageText,text);
        }
    }

    @And("I check departure filtre flights in the hotel list page")
    public void iCheckDepartureFiltreFlightsInTheHotelListPage() {
        flightInformationPage.iWaitFlights();

        List<String> allPrice;
        List<String> allCompany;
        List<String> allClasses;
        List<String> allTime;
        List<String> specialFlight = new ArrayList<>();
        flightInformationPage.showAllDepartureFlight();
        flightInformationPage.iClickCheckbox();
        flightInformationPage.iClickFiltre();

        allTime=flightInformationPage.getAllFlightTime2();
        allPrice =flightInformationPage.getAllFlightPrice();
        allCompany = flightInformationPage.getAllFlightCompany();
        allClasses = flightInformationPage.getAllFlightClass();
        System.out.println(allPrice);
        System.out.println(allTime);

        for (int i = 0; i < allPrice.size(); i++) {
            unsortedFlights.add(new Flight(Integer.valueOf(allPrice.get(i)), allTime.get(i)));
        }

        // Fiyat ve saat bilgisine göre sıralama
        List<Flight> sortedFlights = new ArrayList<>(unsortedFlights);
        sortedFlights.sort(Comparator
                .comparing(Flight::getTime) // Fiyata göre sırala
                .thenComparing(Flight::getPrice)); // Fiyatlar aynıysa saate göre sırala


        // Sıralanmış listeyi yazdır
        for (int i = 0; i < unsortedFlights.size(); i++) {
            Flight unsorted = unsortedFlights.get(i);
            Flight sorted = sortedFlights.get(i);

            // Elemanları karşılaştır ve assertion ile kontrol et
            if (!unsorted.equals(sorted)) {
                throw new AssertionError("Test başarısız! İndeks " + i + " farklı:\n"
                        + "Sıralanmamış: " + unsorted + "\n"
                        + "Sıralanmış: " + sorted);
            }
        }
        System.out.println(sortedFlights);
        System.out.println(unsortedFlights);
        System.out.println(allClasses);
        for (int i=0; i< allClasses.size(); i++){
            if (allClasses.get(0).equalsIgnoreCase("SPECIAL")){
                break;
            }

            else if (allClasses.get(i).equalsIgnoreCase("SPECIAL")){
                flightInformationPage.iClickFlightCompany(i);
                String ErrorMessage = flightInformationPage.iCheckClassMessage();
                System.out.println(ErrorMessage);
                Assertion.assertEquals(ErrorMessage,"Seçmiş olduğunuz uçuş için diğer uçuşunuz da Pegasus Havayolu seçilmelidir");
                flightInformationPage.iClickFlightCompany(0);
                break;
            }
        }


        for (int i=0; i<allCompany.size(); i++){
            if (allCompany.get(i).equalsIgnoreCase("PEGASUS") && allClasses.get(i).equalsIgnoreCase("SPECIAL")){
                specialFlight.add("True");
            } else {
                specialFlight.add("False");
            }
        }
        flightInformationPage.iClickSaveFlight();
        /*if (specialFlight.contains("True")){
            // Eğer Pegasus uçuşları varsa ve specialFlight listesinde "true" varsa assertion başarısız olur
            System.out.println("GİDİŞ UÇUŞ SINIFINDA SPECİAL VAR");
        } else {
            // Eğer Pegasus uçuşları yoksa buraya girer
            System.out.println("GİDİŞ UÇUŞ SINIFINDA SPECİAL YOK.");
        }*/

    }


    @And("I get depature flight price in flight tab")
    public void getDepartureFlightPrice() {
        flightContext.setDepartureFlightPrice(flightInformationPage.getDepartureFlightPrice());
    }

    @And("I get depature flight depature flight area name in flight tab")
    public void getDepartureFlightFromArea() {
        flightContext.setDepartureFlightFromArea(flightInformationPage.getDepartureFlightFromArea());
    }

    @And("I get depature flight arrival flight area name in flight tab")
    public void getDepartureFlightToArea() {
        flightContext.setDepartureFlightToArea(flightInformationPage.getDepartureFlightToArea());
    }

    @And("I get depature flight depature flight time in flight tab")
    public void getDepartureFlightTime() {
        flightContext.setDepartureFlightTime1(flightInformationPage.getDepartureFlightTime());
    }

    @And("I get depature flight arrival flight time in flight tab")
    public void getDepartureArrivalTime() {
        flightContext.setDepartureArrivalTime(flightInformationPage.getDepartureArrivalTime());
    }

    @And("I get arrival flight price in flight tab")
    public void getArrivalFlightPrice() {
        flightContext.setArrivalFlightPrice(flightInformationPage.getArrivalFlightPrice());
    }

    @And("I get arrival flight depature flight area name in flight tab")
    public void getArrivalFlightFromArea() {
        flightContext.setArrivalFlightFromArea(flightInformationPage.getArrivalFlightFromArea());
    }

    @And("I get arrival flight arrival flight area name in flight tab")
    public void getArrivalFlightToArea() {
        flightContext.setArrivalFlightToArea(flightInformationPage.getArrivalFlightToArea());
    }

    @And("I get arrival flight depature flight time in flight tab")
    public void getArrivalDepartureTime() {
        flightContext.setArrivalDepartureTime(flightInformationPage.getArrivalDepartureTime());
    }

    @And("I get arrival flight arrival flight time in flight tab")
    public void getArrivalArrivalTime() {
        flightContext.setArrivalArrivalTime(flightInformationPage.getArrivalArrivalTime());
    }

    @And("I click reservation button in the flight tab")
    public void clickReservationButton() {
        flightInformationPage.clickReservationButton();
    }



    @And("I check return filtre flights in the hotel list page")
    public void iCheckReturnFiltreFlightsInTheHotelListPage() {
    }


    class returnFlight {
        private final int price;
        private final String time;

        public returnFlight(int price, String time) {
            this.price = price;
            this.time = time;
        }

        public int getPrice() {
            return price;
        }

        public String getTime() {
            return time;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            returnFlight returnFlight = (returnFlight) obj;
            return price == returnFlight.price && Objects.equals(time, returnFlight.time);
        }

        @Override
        public int hashCode() {
            return Objects.hash(price, time);
        }

        @Override
        public String toString() {
            return "Fiyat: " + price + ", Saat: " + time;
        }
    }


    class Flight {
        private final int price;
        private final String time;

        public Flight(int price, String time) {
            this.price = price;
            this.time = time;
        }

        public int getPrice() {
            return price;
        }

        public String getTime() {
            return time;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Flight flight = (Flight) obj;
            return price == flight.price && Objects.equals(time, flight.time);
        }

        @Override
        public int hashCode() {
            return Objects.hash(price, time);
        }

        @Override
        public String toString() {
            return "Fiyat: " + price + ", Saat: " + time;
        }
    }
}
