package no.tretoen.kafka.plaindemoconsumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.Serdes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.annotation.PreDestroy

@SpringBootApplication
class PlainDemoConsumerApplication {
    @Bean
    fun getConsumer(): Consumer<String, String> {
        val properties = Properties()
        properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = Serdes.String().deserializer().javaClass.name
        properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = Serdes.String().deserializer().javaClass.name
        properties[ConsumerConfig.GROUP_ID_CONFIG] = "plain-demo-consumer-application"
        properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"
        properties[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = "true"

        return KafkaConsumer(properties)
    }
}

fun main(args: Array<String>) {
    runApplication<PlainDemoConsumerApplication>(*args)
}

@Component
class KafkaLifeCycle(@Autowired val purchaseConsumer: PurchaseConsumer): ApplicationListener<ApplicationStartedEvent> {
    val log: Logger = LoggerFactory.getLogger(KafkaLifeCycle::class.java)
    val threadPool: ExecutorService = Executors.newFixedThreadPool(1)

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        threadPool.submit { purchaseConsumer.start() }
    }

    @PreDestroy
    fun destroy() {
        try {
            threadPool.shutdown()
        } catch (e: Exception) {
            log.error("Error shutting down thead pool")
        } finally {
            if (!threadPool.isShutdown) {
                threadPool.shutdownNow()
            }
        }
    }
}
