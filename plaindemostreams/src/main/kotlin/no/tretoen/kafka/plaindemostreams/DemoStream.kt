package no.tretoen.kafka.plaindemostreams

import no.tretoen.kafka.plaindemostreams.serdes.StockTradeAggregate
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyWindowStore
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PreDestroy


@Component
class DemoStream {

    private val kafkaStream: KafkaStreams

    private val stockTradeStoreName = "STOCK_TRADES_STORE"

    init {
        val properties = Properties()

        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = "plaindemostreams2"
        properties[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = 10000L
        //properties[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = "0"

        kafkaStream = KafkaStreams(DemoTopology().create(stockTradeStoreName), properties)

        Runtime.getRuntime().addShutdownHook(Thread(kafkaStream::close))

        kafkaStream.start()
    }

    fun getStockTradeStateStore(): ReadOnlyWindowStore<String, StockTradeAggregate> {
        return kafkaStream.store(stockTradeStoreName, QueryableStoreTypes.windowStore())
    }


    @PreDestroy
    fun close() {
        kafkaStream.close()
    }
}
