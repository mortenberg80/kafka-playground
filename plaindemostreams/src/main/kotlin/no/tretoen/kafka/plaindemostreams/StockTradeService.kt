package no.tretoen.kafka.plaindemostreams

import no.tretoen.kafka.plaindemostreams.serdes.StockTradeAggregate
import org.apache.kafka.streams.state.ReadOnlyWindowStore
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class StockTradeService(stream: DemoStream) {
    private val windowedStockTradeStateStore: ReadOnlyWindowStore<String, StockTradeAggregate> = stream.getStockTradeStateStore()

    fun getAggregate(symbol: String): StockTradeAggregate? {
        val now = Instant.now()
        val fetch = windowedStockTradeStateStore.fetch(symbol, now.minusSeconds(10), now)
        if (fetch?.hasNext() == true) {
            return fetch.next()?.value
        }
        return null
    }

    fun getHighest(): StockTradeAggregate? {
        val now = Instant.now()
        val results = windowedStockTradeStateStore.fetchAll(now.minusSeconds(300), now)
        if (!results.hasNext()) return null
        return results.asSequence()
            .map { it.value }
            .reduce { a, b -> if (a.balance > b.balance) { return@reduce a } else { return@reduce b } }
    }
}
