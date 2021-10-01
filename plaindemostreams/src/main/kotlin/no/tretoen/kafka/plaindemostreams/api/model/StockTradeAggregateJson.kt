package no.tretoen.kafka.plaindemostreams.api.model

data class StockTradeAggregateJson(
    val symbol: String,
    val numberOfTransactions: Int,
    val balance: Int
)
