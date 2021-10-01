package no.tretoen.kafka.plaindemostreams.serdes

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serdes.WrapperSerde
import org.apache.kafka.common.serialization.Serializer


data class StockTrade(
    val side: String,
    val quantity: Int,
    val symbol: String,
    val price: Int,
    val account: String,
    val userid: String
)

data class StockTradeAggregate(
    val symbol: String,
    val numberOfTransactions: Int,
    val balance: Int
) {
    fun add(stockTrade: StockTrade): StockTradeAggregate {
        val symbol = if (this.symbol == "TBD") {
            stockTrade.symbol
        } else {
            this.symbol
        }

        val direction = when (stockTrade.side) {
            "BUY" -> 1
            "SELL" -> -1
            else -> 0
        }
        val delta = stockTrade.quantity * stockTrade.price * direction

        return StockTradeAggregate(symbol, this.numberOfTransactions + 1, this.balance + delta)
    }
}

class StockTradeSerde :
    WrapperSerde<StockTrade>(
        JsonSerializer<StockTrade>(),
        JsonDeserializer(StockTrade::class.java)
    )

class StockTradeAggregateSerde : WrapperSerde<StockTradeAggregate>(
    JsonSerializer<StockTradeAggregate>(),
    JsonDeserializer(StockTradeAggregate::class.java)
)

class JsonSerializer<T> : Serializer<T> {
    private val objectMapper = ObjectMapper()
    override fun configure(props: Map<String?, *>?, isKey: Boolean) {
        objectMapper.registerModule(KotlinModule())
    }

    override fun serialize(topic: String?, data: T?): ByteArray? {
        return if (data == null) null else try {
            objectMapper.writeValueAsBytes(data)
        } catch (e: Exception) {
            throw SerializationException("Error serializing JSON message", e)
        }
    }

    override fun close() {
        // nothing to do
    }
}

class JsonDeserializer<T> : Deserializer<T> {
    private val objectMapper = ObjectMapper()
    private var tClass: Class<T>? = null

    constructor() {}
    constructor(tClass: Class<T>?) {
        this.tClass = tClass
        this.objectMapper.registerModule(KotlinModule())
    }

    override fun configure(props: Map<String?, *>?, isKey: Boolean) {
    }

    override fun deserialize(topic: String?, bytes: ByteArray?): T? {
        if (bytes == null) return null
        val data: T = try {
            objectMapper.readValue(bytes, tClass)
        } catch (e: java.lang.Exception) {
            throw SerializationException(e)
        }
        return data
    }

    override fun close() {
        // nothing to do
    }
}
