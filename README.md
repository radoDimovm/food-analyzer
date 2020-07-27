# Food Analyzer :hamburger:
Food-analyzer is a console server-client application for informing a person about the composition and energy value of food products in his menu. Its server use http communication protocol to get the data about the food from https://fdc.nal.usda.gov.

Note: The app doesn't work nowadays, because of the changes in https://fdc.nal.usda.gov API. Simply i have no interest in maintaining it.

## Project description:

В наши дни е особено важно да знаем какво ядем и да следим калориите, които консумираме. Нека си улесним живота като имплементираме приложение, което да ни информира за състава и енергийната стойност на хранителните продукти в менюто ни.

Един от най-изчерпателните и достоверни източници на подобна информация е [базата данни за състава на храните](https://ndb.nal.usda.gov/ndb/) на [U.S. Department of Agriculture](https://www.usda.gov/). За наше щастие, тази информацията е достъпна и чрез публично безплатно REST API, което е документирано [тук](https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORTV2.md).

Ще имплементираме Food Analyzer като многонишково клиент-сървър приложение.

## Food Analyzer Server

- Сървърът трябва да може да обслужва множество клиенти едновременно.
- Сървърът получава команди от клиентите и връща подходящ резултат.
- Сървърът извлича необходимите му данни от гореспоменатото *RESTful API* и запазва (кешира) резултата в локалната си файлова система.

    Например, при получаване на командата `get-food raffaello`, сървърът прави следната *HTTP GET* заявка: https://api.nal.usda.gov/ndb/search/?q=raffaello&api_key=DEMO_KEY и получава *HTTP response* със статус код *200* и с тяло следния *JSON*:

```javascript
{
  "list": {
    "q": "raffaello",
    "sr": "1",
    "ds": "any",
    "start": 0,
    "end": 1,
    "total": 1,
    "group": "",
    "sort": "r",
    "item": [
      {
        "offset": 0,
        "group": "Branded Food Products Database",
        "name": "RAFFAELLO, ALMOND COCONUT TREAT, UPC: 009800146130",
        "ndbno": "45142036",
        "ds": "LI",
        "manu": "Ferrero U.S.A., Incorporated"
      }
    ]
  }
}
```

Заявките към REST API-то изискват автентикация с API key, какъвто може да получите като се регистрирате [тук](https://ndb.nal.usda.gov/ndb/doc/index#).

От данните за продукта, ни интересува пълното му име (`RAFFAELLO, ALMOND COCONUT TREAT`) и уникалния му номер в базата, ndbno (`45142036`). Някои продукти, по-точно тези с група `Branded Food Products Database`, имат също и производител (`Ferrero U.S.A., Incorporated`) и UPC код (`009800146130`), който се съдържа в елемента `name`.

**Бележка:** UPC, или [Universal Product Code](https://en.wikipedia.org/wiki/Universal_Product_Code), е доминиращият в САЩ стандарт за баркод. С други думи, UPC кодът е числото, кодирано в баркода на опаковката на продуктите.

Сървърът кешира получената информация на локалната файлова система. При получаване на заявка, сървърът първо трябва да провери дали в кеша вече съществува информация за дадения продукт, и ако е така, директно да върне тази информация, вместо да направи нова заявка към REST API-то.

## Food Analyzer Client

Клиентът осъществява връзка с *Food Analyzer Server* на определен порт, чете команди от стандартния вход, изпраща ги към сървъра и извежда получения резултат на стандартния изход в human-readable формат. Клиентът може да изпълнява следните команди:

-	`get-food <food_name>` - извежда информацията, описана по-горе, за даден хранителен продукт. Ако сървърът върне множество продукти с даденото име, се извежда информация за всеки от тях. Ако пък липсва информация за продукта, се извежда подходящо съобщение.
-	`get-food-report <food_ndbno>` - по даден уникален номер на продукт (ndbno) извежда име на продукта, съставки (ingedients), енергийна стойност (калории), съдържание на белтъчини, мазнини, въглехидрати и фибри.
-   `get-food-by-barcode --upc=<upc_code>|--img=<barcode_image_file>` - извежда информация за продукт по неговия баркод, *ако такава е налична в кеша на сървъра* (обърнете внимание, че REST API-то не поддържа търсене на продукт по UPC код или баркод изображение). Задължително е да подадем един от двата параметъра: или UPC code, или баркод снимка (като пълен път и име на файла на локалната файлова система на клиента). Ако са указани и двата параметъра, img параметърът се игнорира.

За да реализирате търсене по баркод изображение, ще имате нужда от Java библиотека или уеб услуга, с които да конвертирате изображение на баркод към числото, което кодира. Може да ползвате open source библиотеката [ZXing "Zebra Crossing"](https://github.com/zxing/zxing) или [ZXing уеб услугата](https://zxing.org/w/decode.jspx).

### Пример за валидни входни данни

```
get-food butter
get-food-report 45142036
get-food-by-barcode --img=D:\User\Photos\BarcodeImage.jpg --upc=03600029142
```
