package day19

import aoc.utils.Timer
import aoc.utils.findInts
import aoc.utils.readInput
import day17.rocks

fun main() {
    part1().let { println(it) }
}

class Optimizer {
    // timeLeft, geodeCollected
    private val highScore = mutableMapOf<Int,Int>()

    fun alreadyWorse(timeLeft: Int, resources: Resources, production: Resources): Boolean {
        // How much geode would accumulate if building one geode robot each turn forward
        val additionalGeode = (1..timeLeft).sum()

        // How much would current production yield
        val current = production.geode*timeLeft

        val total = additionalGeode + current + resources.geode

        return total <= (highScore[timeLeft] ?: -1)
    }

    fun update(timeLeft: Int, resources: Resources) {
        if((highScore[timeLeft] ?: -1) < resources.geode) {
            highScore[timeLeft] = resources.geode
        }
    }

    fun asString(): String {
        return highScore.map { "${it.key}:${it.value}" }.joinToString(",")
    }
}

data class Resources(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) {
    fun plus(other: Resources): Resources {
        return copy(
            ore = ore + other.ore,
            clay = clay + other.clay,
            obsidian = obsidian + other.obsidian,
            geode = geode + other.geode,
        )
    }

    fun minus(other: Resources): Resources {
        return copy(
            ore = ore - other.ore,
            clay = clay - other.clay,
            obsidian = obsidian - other.obsidian,
            geode = geode - other.geode,
        )
    }
}

data class Robot(val cost: Resources, val production: Resources)

data class BluePrint(val oreRobot: Robot, val clayRobot: Robot, val obsidianRobot: Robot, val geodeRobot: Robot)

fun part1(): Int {
    val bluePrints = readInput("day19-input.txt")
        .map { it.findInts() }
        .map {
            BluePrint(
                Robot(Resources(it[1], 0, 0, 0), Resources(1, 0, 0, 0)),
                Robot(Resources(it[2], 0, 0, 0), Resources(0, 1, 0, 0)),
                Robot(Resources(it[3], it[4], 0, 0), Resources(0, 0, 1, 0)),
                Robot(Resources(it[5], 0, it[6], 0), Resources(0, 0, 0, 1)),
            )
        }

    val timer = Timer(bluePrints.size.toLong())
    val efficiency = bluePrints
        .mapIndexed { index, bluePrint ->
            println("Processing blueprint ${index + 1}")
            val result = produceGeode(bluePrint)
            timer.processed = index.toLong() + 1
            val r = Pair(index + 1, result)
            println("DONE blueprint ${index + 1}: $r")
            r
        }
        .map {
            it.first * it.second.geode
        }
        .sum()

    println(efficiency)

    return efficiency
}

fun produceGeode(bluePrint: BluePrint): Resources {
    val highScore = Optimizer()
    val production = Resources(1, 0, 0, 0)
    val resources = Resources(0, 0, 0, 0)

    return testProduction(24, bluePrint, production, resources,highScore)
}

fun testProduction(
    timeLeft: Int,
    bluePrint: BluePrint,
    production: Resources,
    resources: Resources,
    highScore: Optimizer
): Resources {
    if (timeLeft == 0)
        return resources

    highScore.update(timeLeft, resources)

    if(highScore.alreadyWorse(timeLeft, resources, production)) {
        return resources
    }

    val newTime = timeLeft - 1

    val results = listOfNotNull(
        testProductionIfResources(newTime, bluePrint, production, resources, bluePrint.geodeRobot,highScore),
        testProductionIfResources(newTime, bluePrint, production, resources, bluePrint.oreRobot, highScore),
        testProductionIfResources(newTime, bluePrint, production, resources, bluePrint.clayRobot, highScore),
        testProductionIfResources(newTime, bluePrint, production, resources, bluePrint.obsidianRobot, highScore),
        if (hasResourcesForAnyRobot(bluePrint, resources))
            null
        else
            testProduction(newTime, bluePrint, production, resources.plus(production), highScore)
    )

    return results.maxByOrNull { it.geode }!!
}

fun hasResourcesForAnyRobot(bluePrint: BluePrint, resources: Resources): Boolean {
    return hasResourcesForRobot(bluePrint.oreRobot, resources) &&
            hasResourcesForRobot(bluePrint.clayRobot, resources) &&
            hasResourcesForRobot(bluePrint.obsidianRobot, resources) &&
            hasResourcesForRobot(bluePrint.geodeRobot, resources)
}

fun testProductionIfResources(
    newTime: Int,
    bluePrint: BluePrint,
    production: Resources,
    resources: Resources,
    robot: Robot,
    highScore: Optimizer
): Resources? {
    return if (hasResourcesForRobot(robot, resources)) testProduction(
        newTime,
        bluePrint,
        production.plus(robot.production),
        resources.minus(robot.cost).plus(production),
        highScore
    ) else null
}

fun hasResourcesForRobot(type: Robot, resources: Resources): Boolean {
    return resources.ore >= type.cost.ore &&
            resources.clay >= type.cost.clay &&
            resources.obsidian >= type.cost.obsidian
}

fun part2(): Int {
    return 1;
}
