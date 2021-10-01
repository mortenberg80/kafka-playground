package no.tretoen.kafka.plaindemoproducer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serdes
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.util.*

@SpringBootApplication
class PlainDemoProducerApplication {
	@Bean
	fun getProducer(): Producer<String, String> {
		val properties = Properties()
		properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
		properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = Serdes.String().serializer().javaClass.name
		properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = Serdes.String().serializer().javaClass.name
		val producer = KafkaProducer<String, String>(properties)

		Runtime.getRuntime().addShutdownHook(Thread {
			producer.close()
		})

		return producer
	}
}

fun main(args: Array<String>) {
	runApplication<PlainDemoProducerApplication>(*args)
}
