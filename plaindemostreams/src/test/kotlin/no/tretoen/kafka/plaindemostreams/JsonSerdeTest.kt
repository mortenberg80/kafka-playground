package no.tretoen.kafka.plaindemostreams

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.tretoen.kafka.plaindemostreams.serdes.StockTrade
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JsonSerdeTest {

    val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        objectMapper.registerModule(KotlinModule())
    }

    @Test
    fun `should deserialize json properly`() {
        val json = """
        {
          "side": "SELL",
          "quantity": 13,
          "symbol": "ZBZX",
          "price": 138,
          "account": "XYZ789",
          "userid": "User_5"
        }
        """.trimIndent()

        val stockTrade = objectMapper.readValue(json, StockTrade::class.java)

        assertThat(stockTrade).isNotNull
    }
}
