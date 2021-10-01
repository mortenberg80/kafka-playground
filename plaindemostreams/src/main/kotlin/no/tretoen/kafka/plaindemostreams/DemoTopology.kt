package no.tretoen.kafka.plaindemostreams

import no.tretoen.kafka.plaindemostreams.serdes.StockTrade
import no.tretoen.kafka.plaindemostreams.serdes.StockTradeAggregate
import no.tretoen.kafka.plaindemostreams.serdes.StockTradeAggregateSerde
import no.tretoen.kafka.plaindemostreams.serdes.StockTradeSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.WindowStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration


class DemoTopology {

    fun create(): Topology {

        val log: Logger = LoggerFactory.getLogger(DemoTopology::class.java)

        val builder = StreamsBuilder()

        val stream: KStream<String, StockTrade> = builder.stream("stock-trades", Consumed.with(Serdes.String(), StockTradeSerde()))

        val stockTradeAggregateSerde = StockTradeAggregateSerde()
        stream
            //.peek { k, v -> log.info("Got message with key=$k, value=$v") }
            .groupByKey()
            .windowedBy(TimeWindows.of(Duration.ofSeconds(10)).advanceBy(Duration.ofSeconds(10)))
            .aggregate(
                {StockTradeAggregate("TBD", 0, 0)},
                { _, v: StockTrade, a: StockTradeAggregate -> a.add(v) },
                Materialized.`as`<String?, StockTradeAggregate?, WindowStore<Bytes, ByteArray>?>("STOCK_TRADES_STORE").withValueSerde(
                    stockTradeAggregateSerde
                )
            )
            .toStream()
            .filter { _, v -> v.symbol == "ZXZZT"}
            .peek { k, v -> log.info("Got aggregate with key=${k.key()} in window ${prettyFormatWindow(k.window())}: value=$v") }
            //.to("stock-trades-aggregates", Produced.with(Serdes.String(), stockTradeAggregateSerde))

        return builder.build()
    }

    private fun prettyFormatWindow(window: Window): String {
        return "${window.startTime()} - ${window.endTime()}"
    }
}
