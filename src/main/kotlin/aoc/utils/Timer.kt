package aoc.utils

import java.time.Duration
import java.time.LocalTime
import kotlin.concurrent.thread

data class Timer(val target: Long) {
    var startTime: LocalTime = LocalTime.now()
    var processed: Long = 0

    init {
        printStats()
    }

    fun printStats() {
        thread {
            val timeElapsed = Duration.between(startTime, LocalTime.now())
            val estimatedTime = timeElapsed.multipliedBy((target - processed) / processed)

            if (estimatedTime.toHours() > 2)
                println("Processed: $processed in ${timeElapsed.toHours()} hours estimated time remaining ${estimatedTime.toHours()} hours")
            else
                println("Processed: $processed in ${timeElapsed.toMinutes()} minutes estimated time remaining ${estimatedTime.toMinutes()} minutes")
            Thread.sleep(10000)
            printStats()
        }
    }
}
