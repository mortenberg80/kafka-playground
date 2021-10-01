package no.tretoen.kafka.plaindemoconsumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.errors.WakeupException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.CountDownLatch
import javax.annotation.PreDestroy

@Component
class PurchaseConsumer(
    val consumer: Consumer<String, String>
) {
    val shutdownLatch = CountDownLatch(1);
    val log: Logger = LoggerFactory.getLogger(PurchaseConsumer::class.java)

    fun start() {
        try {
            consumer.subscribe(listOf("no.tretoen.kafka.test.events"))

            while (true) {
                val consumerRecords = consumer.poll(Duration.ofMillis(Long.MAX_VALUE))
                consumerRecords.forEach {
                    log.info("Key: ${it.key()}, value: ${it.value()}")
                }
            }
        } catch (e: WakeupException) {
            // do nothing
        } catch (e: Exception) {
            log.error("Got exception: ${e.localizedMessage}", e)
        } finally {
            consumer.close(Duration.ofSeconds(5))
            shutdownLatch.countDown()
        }
    }

    @PreDestroy
    fun shutdown() {
        log.info("Received shutdown event")
        consumer.wakeup()
        shutdownLatch.await()
    }
}
