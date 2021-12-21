# G.A.D.A. - Team 5 - Fintech Project
## Project Future 7th Cycle - Financial Technology (Fintech) Academy
[![N|Solid](https://www.codehub.gr/wp-content/uploads/2018/01/cropped-CodeHub-logo_320x132.png)](https://www.codehub.gr/)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; [![N|Solid](https://www.regeneration.gr/wp-content/uploads/2021/07/Logos.svg)](https://www.regeneration.gr/)

## Brief
In this project, a simplified implementation of card and eWallet payment has been developed on IntelliJ by [Giorgos Karypidis](https://www.linkedin.com/in/georgios-karypidis/), [Alexander Kyriakou](https://www.linkedin.com/in/alexanderkyriakou/), [Dimitris Fountoulis](https://www.linkedin.com/in/dimitrisfountoulis/) and [Alexis Kotsampasis](https://www.linkedin.com/in/alexis-kotsampasis-738541a2/). The eWallet payment functionality was built on an existing source code provided by the Code.Hub team, which assisted us during the whole journey.

## Prerequisites for deployment
You'll need certain tools in order to be able to execute the code.
- [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows), or any Java IDE
- [Docker](https://docs.docker.com/get-docker/)
- [Postman](https://www.postman.com/downloads/)
- [RabbitMQ](https://www.rabbitmq.com/download.html)

The database entries used here are pulled from the implemented **H2 server** in IntelliJ.

## Deployment

1. Run **Edge** module
2. Run **Integration** module
2. Run **Docker**
3. Run **Postman**
4. Via a browser login to ```http://localhost:15672``` with credentials ```guest``` - ```guest```
5. In Postman select ```Post``` method with URL ```http://localhost:8080/api/feeder``` for Card payments or ```http://localhost:8080/api/wallet/feeder``` for eWallet payments. Then you'll need to supply a simple JSON query as follows (copy-paste it in Body tab and fill the gaps):
```
{
"cid": "CU#########",
"creditorName": "@@@ @@@",
"creditorIBAN": "@@##############",
"debtorName": "@@@ @@@",
"debtorIBAN": "@@##############",
"paymentAmount": "##.#",
"valuerDate": "Year/Month/Day",
"paymentCurrency": "EUR",
"feeAmount": "#",
"feeCurrency": "EUR"
}
```
Once the query has been sent, check on RabbitMQ that it has registered in Queues. If the payment can be prossesed, it'll be shown in the log file _payments_.

## The End!
Thank you Code.Hub, Piraeus Bank and ReGeneration for this amazing learning experience.