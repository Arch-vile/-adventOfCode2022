package utils

import java.util.BitSet

/**
 * All operations assume most significant bit on position/index 0
 */
class Binary private constructor(private val bits: BitSet, private val bitCount: Int) {
    fun asLong(): Long {
        if (bits.toLongArray().size > 1) {
            throw Error("Bits have expanded over single long value")
        }

        return if (bits.toLongArray().isEmpty()) 0
        else
            bits.toLongArray()[0]
    }

    fun bits(): List<Int> =
        (0 until bitCount).map { bit(it) }

    fun bit(index: Int): Int {
        return if (bits.get(bitCount - index - 1)) 1 else 0
    }

    fun asString(): String {
        return (0 until bitCount)
            .map { if (bit(it) == 1) '1' else '0' }
            .joinToString("")
    }

    /**
     * New binary with inverted bits.
     */
    fun invert(): Binary {
        val copy = (bits.clone() as BitSet)
        copy.flip(0, bitCount)
        return Binary(copy, bitCount)
    }

    override fun toString(): String {
        return asString()
    }

    companion object {
        fun from(from: Long, bitCount: Int): Binary {
            if (from < 0) throw Error("Negative numbers not allowed")
            return Binary(BitSet.valueOf(longArrayOf(from)),bitCount)
        }

        fun from(from: String): Binary {
            val bits = BitSet()
            from.forEachIndexed { index, bit ->
                val i = from.length - index - 1
                when (bit) {
                    '1' -> bits.set(i)
                    '0' -> bits.clear(i)
                    else -> throw Error("$bit at index $index is not 0 or 1")
                }
            }
            return Binary(bits,from.length)
        }

        fun from(from: List<Int>): Binary {
            val bits = BitSet()
            from.forEachIndexed { index, bit ->
                val i = from.size - index - 1
                when (bit) {
                    1 -> bits.set(i)
                    0 -> bits.clear(i)
                    else -> throw Error("$bit at index $index is not 0 or 1")
                }
            }
            return Binary(bits, from.size)
        }
    }

}


