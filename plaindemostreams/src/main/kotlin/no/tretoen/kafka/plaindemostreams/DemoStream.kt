package no.tretoen.kafka.plaindemostreams

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PreDestroy

@Component
class DemoStream {

    private val kafkaStream: KafkaStreams

    init {
        val properties = Properties()

        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = "plaindemostreams2"
        properties[StreamsConfig.COMMIT_INTERVAL_MS_CONFIG] = 10000L
        //properties[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = "0"

        kafkaStream = KafkaStreams(DemoTopology().create(), properties)

        Runtime.getRuntime().addShutdownHook(Thread(kafkaStream::close))

        kafkaStream.start()
    }


    @PreDestroy
    fun close() {
        kafkaStream.close()
    }
}
