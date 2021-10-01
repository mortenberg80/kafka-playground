package no.tretoen.kafka.plaindemostreams.api

import no.tretoen.kafka.plaindemostreams.StockTradeService
import no.tretoen.kafka.plaindemostreams.api.model.StockTradeAggregateJson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class StockTradeAggregateController(val stockTradeService: StockTradeService) {
    val log: Logger = LoggerFactory.getLogger(StockTradeAggregateController::class.java)

    @GetMapping(path = ["/stocktrade/aggregate/{symbol}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAggregate(@PathVariable symbol: String): StockTradeAggregateJson {
        val aggregate = stockTradeService.getAggregate(symbol)

        aggregate?.let {
            return StockTradeAggregateJson(it.symbol, it.numberOfTransactions, it.balance)
        }

        throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        )
    }

    @GetMapping(path = ["/stocktrade/aggregate/highest"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getHighest(): StockTradeAggregateJson {
        val aggregate = stockTradeService.getHighest()

        aggregate?.let {
            return StockTradeAggregateJson(it.symbol, it.numberOfTransactions, it.balance)
        }

        throw ResponseStatusException(
            HttpStatus.NOT_FOUND, "entity not found"
        )
    }
}
