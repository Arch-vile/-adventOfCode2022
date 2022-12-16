package aoc.aoc.utils


fun loop(from: Int, to: Int, step: Int = 1): IntProgression {
    val stepSize = if (from > to) -1*step else step
    return IntProgression.fromClosedRange(from, to, stepSize)
}
