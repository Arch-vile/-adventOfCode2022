package utils

import java.lang.Integer.min

data class Entry<T>(val cursor: Cursor, val value: T)
data class Size(val width: Int, val height: Int)
data class Cursor(val x: Int, val y: Int)

class Matrix<T>(input: List<List<T>>) {

    private val data: MutableList<MutableList<Entry<T>>> = mutableListOf()

    init {
        for(y in input.indices) {
            val columns = input[0].size
            val row = mutableListOf<Entry<T>>()
            data.add(row)
            for(x in 0 until columns) {
               if(input[y].size != columns)
                   throw Error("all rows must have equal amount of columns")
               row.add(Entry(Cursor(x,y),input[y][x]))
            }
        }
    }

    constructor(width: Long, height: Long, init: (Int,Int) -> T) : this(initialize(width, height, init))

    fun get(x: Int, y: Int) = data[y][x]


    fun find(value: T): Entry<T>? {
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                if (data[y][x].value == value)
                    return data[y][x]
            }
        }
        return null;
    }

    fun replace(current: Entry<T>, value: T) {
        data[current.cursor.y][current.cursor.x] = current.copy(value=value)
    }

    fun replace(x: Int, y: Int, newValue: (Entry<T>) -> T) {
        data[y][x] = data[y][x].copy(value = newValue(data[y][x]))
    }

    // Rows for which the matcher matches for each element in row
    fun keepRowsWithValues(matcher: (Entry<T>) -> Boolean): Matrix<T> {
        return Matrix(data
            .filter { row -> row.count(matcher) == row.size }
            .map { row -> row.map { it.value } })
    }

    // Columns for which the matcher matches for each element in column
    fun keepColumnsWithValues(matcher: (Entry<T>) -> Boolean): Matrix<T> {
        return rotateCW().keepRowsWithValues(matcher)
    }

    /**
     * Return tile (part) of this Matrix between the given points. Start point must be with
     * smaller (or equal) x and y than end point.
     */
    fun tile(start: Point, end: Point): Matrix<T> {
        return Matrix((start.y..end.y).map { y ->
            (start.x..end.x).map { x ->
               data[y.toInt()][x.toInt()].value
            }
        })
    }

    /**
     * Splits this matrix to multiple sub-matrixes determined by a moving window
     * of given size (width & height). Window starts from position 0,0 and is moved
     * by given step (width & height) after each new matrix created.
     * Each matrix will be of given size unless partial is set to true causing smaller
     * matrixes to be created if the current window would fall outside of matrix's borders.
     */
    fun windowed(size: Size, step: Size, partial: Boolean = false): List<Matrix<T>> {
        val slidingX = (0 until data[0].size).windowed(size.width, step.width, partial)
        val slidingY = (0 until data.size).windowed(size.height, step.height, partial)
        return slidingY
            .flatMap { yCoords ->
               slidingX.map { xCoords ->
                   val data = yCoords.map { y ->
                      xCoords.map { x ->
                          data[y][x].value
                      }
                   }
                   Matrix(data)
               }
            }
    }

    fun findAll(matcher: (Entry<T>) -> Boolean): List<Entry<T>> {
        val found = mutableListOf<Entry<T>>()
        for (y in 0 until data.size) {
            for (x in 0 until data.first().size) {
                if (matcher(data[y][x])) found.add(data[y][x])
            }
        }
        return found.toList()
    }

    fun all(): List<Entry<T>> = data.flatten()

    fun rotateCW(): Matrix<T> {
        val foo = IntRange(0, data[0].size - 1)
            .map { x ->
                ((data.size - 1) downTo 0).map { y ->
                    data[y][x].value
                }
            }
        return Matrix(foo)
    }

    override fun toString(): String {
        return data.joinToString("\n") { row -> row.map { it.value }.toString() };
    }

    fun height() = data.size
    fun width() = data[0].size

    override fun equals(other: Any?): Boolean {
        return if(other is Matrix<*>) {
            if(data.size != other.data.size)
                return false

            val mismatch = data.asSequence()
                .mapIndexed { index, row -> row.equals(other.data[index])  }
                .filter { !it }
                .firstOrNull()

            return mismatch == null
        } else false
    }

    // Returns entries relative to the given point
    // Could return less if goes out of bounds
    fun getRelativeAt(cursor: Cursor, getRelative: List<Cursor>): List<Entry<T>> {
        return getRelative
            .map {
                val relY = cursor.y+it.y
                val relX =cursor.x+it.x
                Pair(relX, relY)
            }
            .filter { it.second < data.size && it.second >= 0 && it.first < data[0].size && it.first >= 0 }
            .map { data[it.second][it.first] }
    }

    fun flipHorizontal(): Matrix<T> = Matrix(data.reversed().map { it.map { it.value } })

    fun flipVertical(): Matrix<T> = Matrix(data.map { it.reversed().map { it.value } })

    fun <V> combine(other: Matrix<V>, combiner: (Entry<T>, Entry<V>) -> T) =
        combine(other, Cursor(0,0), combiner)

    // Combine values in given matrix with this one.
    // If matrices are of different size (or not positioned evenly) the size
    // of this matrix will not change (overflow values are ignored).
    // Combine will start from given starting cursor location.
    fun <V> combine(other: Matrix<V>, start: Cursor, combiner: (Entry<T>, Entry<V>) -> T) {
        val thisTileHeight = height() - start.y
        val thisTileWidth = width() - start.x
        val otherTileHeight = other.height()
        val otherTileWidth = other.width()

        val combineTileHeight = min(thisTileHeight, otherTileHeight)
        val combineTileWidth = min(thisTileWidth, otherTileWidth)

        for(y in start.y until start.y+combineTileHeight) {
           for(x in start.x until start.x+combineTileWidth) {
               replace(x,y) {current -> combiner(current, other.data[y - start.y][x - start.x])}
           }
        }
    }

    fun <V> map(mapper: (Entry<T>) -> V): Matrix<V> {
        val newData = data.map { row -> row.map { cell -> mapper(cell) } }
       return Matrix(newData)
    }

    companion object {
        private fun <T> initialize(width: Long, height: Long, init: (x: Int, y: Int) -> T): List<List<T>> {
            return (0 until height)
                .map { y ->
                    (0 until width).map { x ->
                        init(x.toInt(),y.toInt())
                    }
                }
        }
    }


}
