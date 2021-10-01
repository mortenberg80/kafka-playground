package no.tretoen.kafka.plaindemostreams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class PlainDemoStreamsApplication {

}

fun main(args: Array<String>) {
    runApplication<PlainDemoStreamsApplication>(*args)
}
