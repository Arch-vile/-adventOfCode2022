package utils

fun <T : Comparable<T>> closedRange(start: T, end: T): ClosedRange<T> {
   return start..end
}

fun <T : Comparable<T>> ClosedRange<T>.containsFully(test: ClosedRange<T>) =
   contains(test.start) && contains(test.endInclusive);

fun <T : Comparable<T>> ClosedRange<T>.hasOverlap(test: ClosedRange<T>) =
   contains(test.start) || contains(test.endInclusive) || test.contains(start) || test.contains(endInclusive)

