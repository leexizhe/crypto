Description

Develop a crypto trading system with SpringBoot framework and in memory H2 Database.
Design the table structure based on the understanding of the questions.
Functional Scope
1. User able to buy/sell the supported crypto trading pairs
2. User able to see the list of trading transactions
3. User able to see the crypto currencies wallet balance
   
Assumption
1. User has already authenticated and authorised to access the APIs
2. User's initial wallet balance 50,000 USDT in DB record.
3. Only support Ethereum - ETHUSDT and Bitcoin - BTCUSDT pairs of crypto trading.

Task
1. Price aggregation from the source below:
Binance Url : https://api.binance.com/api/v3/ticker/bookTicker
Houbi Url : https://api.huobi.pro/market/tickers
Create a 10 seconds interval scheduler to retrieve the pricing from the source above and store the best pricing into the database.
Hints: Bid Price use for SELL order, Ask Price use for BUY order
2. Create an api to retrieve the latest best aggregated price.
3. Create an api which allows users to trade based on the latest best aggregated price.
Remarks: Do not integrate with other third party system
4. Create an api to retrieve the user’s crypto currencies wallet balance
5. Create an api to retrieve the user trading history.
