@reservation @hotelReservation @regression
Feature: Hotel Reservation Feature

  Background:
    Given I go to "tatil dukkanı" website

  @creditCard @hotel12111 @notMember
    # kesin çalışıyor
  Scenario Outline: ÜYE GİRİŞİ OLMADAN, KREDİ KARTI İLE VERİLEN KİŞİ BİLGİLERİ VE DESTİNASYONLARDA OTEL REZERVASYONU YAPILMASI
    And   I check header logo
    And   I fill hotel or location or category area with <location>
    And   I click searched text
    And   I choose check-in date after <check in> days and check-out date after <check out> days in hotel
    And   I click guest information area
    And   I choose <adult count> adult guest
    And   I choose <child count> child guest with <child age>
    And   I check guest information
    And   I click search button
#    HOTEL LIST PAGE
    And   I check hotel list page title
    And   I get random hotel name in the hotel list
#    And   I get random hotel person count in the hotel list
    And   I get random hotel accommodation day in the hotel list
    And   I get random hotel price in the hotel list
    And   I click random hotel inspect button in the hotel list


    Examples:
      | location   | check in | check out | adult count | child count | child age | guest gender                      | cancellation insurance |
  #    | Fethiye    | 32       | 35        | 2           | 0           | 0         | Kadın,Erkek                            | no                     |
  #    | Bodrum     | 33       | 37        | 2           | 0           | 0         | Erkek,Kadın                       | yes                    |
 #     | Marmaris   | 35       | 40        | 3           | 0           | 0         | Erkek,Kadın,Erkek                 | no                     |
      | Gazimağusa | 22       | 27        | 3           | 0           | 0         | Erkek,Kadın,Erkek           | yes                    |
 #     | Abant      | 20       | 25        | 1           | 1           | 3         | Erkek,Kız                         | no                     |
  ##    | Girne      | 30       | 35        | 2           | 0           | 0         | Erkek,Kadın                   | yes                    |
  #    | Bafra      | 20       | 26        | 2           | 0           | 0         | Erkek,Kadın           | no                     |
 #     | Belek      | 30       | 36        | 2           | 1           | 8         | Erkek,Kadın,Erkek     | yes                    |
 #     | Kemer      | 29       | 35        | 1           | 2           | 5,7       | Erkek,Kız,Kız                     | no                     |
 #     | Side       | 30       | 35        | 2           | 2           | 2,10      | Erkek,Kadın,Kız,Erkek             | yes                    |
 #     | Antalya    | 40       | 44        | 3           | 2           | 1,11      | Erkek,Kadın,Kadın,Kız,Erkek       | no                     |
      | Kıbrıs     | 45       | 50        | 2           | 0          | 0       | Erkek,Kadın | yes                    |
