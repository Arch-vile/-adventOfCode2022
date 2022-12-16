package aoc.utils

import java.lang.Integer.min
import java.util.function.Predicate

data class Entry<T>(val cursor: Cursor, val value: T)
data class Size(val width: Int, val height: Int)
data class Cursor(val x: Int, val y: Int) {
    fun move(amount: Int) = copy(x = x + amount,y = y + amount)
    fun moveX(amount: Int) = copy(x = x + amount)
    fun moveY(amount: Int) = copy(y = y + amount)
    fun move(other: Cursor) = copy(x = x + other.x, y = y + other.y)
    fun minus(other: Cursor) = Cursor(x-other.x, y-other.y)
}

class Matrix<T>(input: List<List<T>>, filler: ((x: Int, y: Int) -> T)? = null) {

    private val data: MutableList<MutableList<Entry<T>>> = mutableListOf()

    init {
        for (y in input.indices) {
            val maxColumns = maxColumnCount(input)
            val row = mutableListOf<Entry<T>>()
            data.add(row)
            for (x in 0 until maxColumns) {
                row.add(Entry(Cursor(x, y), valueOrFilled(input, y, x, filler)))
            }
        }
    }

    constructor(width: Long, height: Long, init: (Int, Int) -> T) : this(initialize(width, height, init))

    private fun valueOrFilled(input: List<List<T>>, y: Int, x: Int, filler: ((x: Int, y: Int) -> T)?): T {

        if (input[y].size <= x) {
            if (filler == null) {
                throw Error("all rows must have equal amount of columns or you must give a filler function")
            } else {
                return filler(x, y)
            }
        }

        return input[y][x]
    }

    private fun maxColumnCount(input: List<List<T>>) = input.maxOf { it.size }


    fun get(x: Int, y: Int) = data[y][x]
    fun get(pos: Cursor) = data[pos.y][pos.x]

    fun find(predicate: Predicate<T>): Entry<T>? {
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                if (predicate.test(data[y][x].value))
                    return data[y][x]
            }
        }
        return null;
    }

    fun replace(current: Entry<T>, value: T) {
        data[current.cursor.y][current.cursor.x] = current.copy(value = value)
    }
    fun replace(point: Cursor, newValue: (Entry<T>) -> T) {
       replace(point.x, point.y,newValue)
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
     *
     * Fails if out of bounds unless a fill value is given.
     */
    fun tile(start: Cursor, end: Cursor, fill: T? = null): Matrix<T> {
        return Matrix((start.y..end.y).map { y ->
            (start.x..end.x).map { x ->
                if (fill == null || isInBounds(Cursor(x, y)))
                    data[y][x].value
                else
                    fill
            }
        })
    }

    fun tileAround(center: Cursor, radius: Int, fill: T? = null): Matrix<T> {
        return tile(center.move(radius*-1), center.move(radius), fill)
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


    /**
     * All nodes reading left to right, top to bottom
     */
    fun allInColumnOrder(): List<Entry<T>> = data.flatten()

    fun rotateCW(): Matrix<T> {
        val foo = IntRange(0, data[0].size - 1)
            .map { x ->
                ((data.size - 1) downTo 0).map { y ->
                    data[y][x].value
                }
            }
        return Matrix(foo)
    }

    fun visualize(columnSeparator: String = ","): String {
        return data.joinToString("\n") { row -> row.map { it.value }.joinToString(columnSeparator) };
    }

    fun height() = data.size
    fun width() = data[0].size

    override fun equals(other: Any?): Boolean {
        return if (other is Matrix<*>) {
            if (data.size != other.data.size)
                return false

            val mismatch = data.asSequence()
                .mapIndexed { index, row -> row.equals(other.data[index]) }
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
                cursor.move(it)
            }
            .filter { it.y < data.size && it.y >= 0 && it.x < data[0].size && it.x >= 0 }
            .map { get(it) }
    }

    fun getRelative(cursor: Cursor, relative: Cursor): Entry<T>? {
        val location = cursor.move(relative)
        if(isInBounds(location))
            return get(location)
        else
            return null
    }

    /**
     * Cells up,down,left and right
      */
    fun getDirectNeighbours(current: Cursor): List<Entry<T>> {
        return getRelativeAt(current, listOf(
            Cursor(1,0),
            Cursor(-1,0),
            Cursor(0,1),
            Cursor(0,-1),
        ))
    }

    fun flipHorizontal(): Matrix<T> = Matrix(data.reversed().map { it.map { it.value } })

    fun flipVertical(): Matrix<T> = Matrix(data.map { it.reversed().map { it.value } })

    fun <V> combine(other: Matrix<V>, combiner: (Entry<T>, Entry<V>) -> T) =
        combine(other, Cursor(0, 0), combiner)

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

        for (y in start.y until start.y + combineTileHeight) {
            for (x in start.x until start.x + combineTileWidth) {
                replace(x, y) { current -> combiner(current, other.data[y - start.y][x - start.x]) }
            }
        }
    }

    // Mutable
    fun rows() = this.data

    fun values() = this.data.map { it.map { it.value } }

    fun <V> map(mapper: (Entry<T>) -> V): Matrix<V> {
        val newData = data.map { row -> row.map { cell -> mapper(cell) } }
        return Matrix(newData)
    }

    /**
     * Start from given cursor (excluded) and trace the route determined by the router function.
     * Router function is passed the updated cursor after each step. Stops when cursor goes
     * out of bounds or null is returned from router
     */
    fun trace(startFrom: Cursor, router: (Cursor) -> Cursor?): List<Entry<T>> {
        if (!isInBounds(startFrom))
            throw Error("Start is out of bounds $startFrom")

        val route = mutableListOf<Entry<T>>()
        var next = router(startFrom)
        while (next != null && isInBounds(next)) {
            route.add(get(next.x, next.y))
            next = router(next)
        }
        return route
    }

    fun isInBounds(next: Cursor): Boolean {
        return next.x >= 0 && next.x < width() && next.y >= 0 && next.y < height()
    }


    companion object {
        private fun <T> initialize(width: Long, height: Long, init: (x: Int, y: Int) -> T): List<List<T>> {
            return (0 until height)
                .map { y ->
                    (0 until width).map { x ->
                        init(x.toInt(), y.toInt())
                    }
                }
        }

        fun fromString(content: List<String>): Matrix<String> {
            return Matrix(content.map { it.toList() })
        }
    }


}
