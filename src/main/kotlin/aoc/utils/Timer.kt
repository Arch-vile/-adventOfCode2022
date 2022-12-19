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
            if (processed != 0L) {
                val timeElapsed = Duration.between(startTime, LocalTime.now())
                val estimatedTime = timeElapsed.multipliedBy((target - processed) / processed)

                if (estimatedTime.toHours() > 2)
                    println("$timeElapsed Processed: $processed/$target in ${timeElapsed.toHours()} hours estimated time remaining ${estimatedTime.toHours()} hours")
                else
                    println("$timeElapsed Processed: $processed/$target in ${timeElapsed.toMinutes()} minutes estimated time remaining ${estimatedTime.toMinutes()} minutes")
            }
            Thread.sleep(60000*5)
            printStats()
        }
    }

}
