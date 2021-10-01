package no.tretoen.kafka.plaindemoproducer.controller

import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(val producer: Producer<String, String>) {

    val log: Logger = LoggerFactory.getLogger(DemoController::class.java)

    @PostMapping("/test/{personId}")
    fun purchase(@PathVariable personId: String) {
        val topic = "no.tretoen.kafka.test.events"
        log.info("Sending message to '$topic'")
        producer.send(ProducerRecord(topic, personId, "My test event"))
    }
}
