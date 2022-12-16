package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class BinaryTest {

    @Test
    fun from_long() {
        assertEquals(123L, Binary.from(123L,2).asLong())
        assertEquals(0L, Binary.from(0L,10).asLong())
        assertThrows(Error::class.java) {
            Binary.from(-1L,10)
        }
    }

    @Test
    fun from_binary_string() {
        assertEquals(
            38234,
            Binary.from("1001010101011010").asLong()
        )
    }

    @Test
    fun fromList() {
        assertEquals(38234,
        Binary.from(listOf(1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0)).asLong())
    }

    @Test
    fun asBits() {
        assertEquals(
            listOf(1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0),
            Binary.from(38234,16).bits()
        )

        assertEquals(
            listOf(0, 0, 0, 0, 0, 0, 0, 1),
            Binary.from(1,8).bits()
        )
    }

    @Test
    fun asString() {
        assertEquals("1101110", Binary.from(110,7).asString())
        assertEquals("01101110", Binary.from(110,8).asString())
    }

    @Test
    fun bit() {
        val binary = Binary.from("010101")
        assertEquals(1, binary.bit(5))
        assertEquals(0, binary.bit(4))
        assertEquals(1, binary.bit(3))

        assertThrows(IndexOutOfBoundsException::class.java) {
            binary.bit(6)
        }
    }

    @Test
    fun invert() {
        assertEquals(
            Binary.from("111000").bits(),
            Binary.from("000111").invert().bits()
        )

        assertEquals(
            Binary.from("000").bits(),
            Binary.from("111").invert().bits()
        )
    }

    @Test
    fun shift() {


    }

}
