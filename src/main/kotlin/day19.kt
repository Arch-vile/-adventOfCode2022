package day19

import aoc.utils.Timer
import aoc.utils.findInts
import aoc.utils.readInput

fun main() {
    part2().let { println(it) }
}

fun part1(): Int {
    val bluePrints = bluePrints()
    return run(24, bluePrints)
        .map {
            it.first * it.second.geode
        }
        .sum()
}

fun part2(): Int {

    // 661217579
    // 4318464576 lol
    val bluePrints = bluePrints().take(3)
    run(32, bluePrints)
    return 1
}

class Optimizer {
    // timeLeft, geodeCollected
    private val highScore = mutableMapOf<Int, Int>()

    fun alreadyWorse(timeLeft: Int, resources: Resources, production: Resources): Boolean {
        // How much geode would accumulate if building one geode robot each turn forward
        val additionalGeode = (1..timeLeft).sum()

        // How much would current production yield
        val current = production.geode * timeLeft

        val total = additionalGeode + current + resources.geode

        return total <= (highScore[timeLeft] ?: -1)
    }

    fun update(timeLeft: Int, resources: Resources) {
        if ((highScore[timeLeft] ?: -1) < resources.geode) {
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

    fun asString(): String {
        return ",$ore,$clay,$obsidian,$geode,"
    }
}

data class Robot(val cost: Resources, val production: Resources)

data class BluePrint(val oreRobot: Robot, val clayRobot: Robot, val obsidianRobot: Robot, val geodeRobot: Robot)


val states = mutableMapOf<String,Resources>();
fun run(time: Int, bluePrints: List<BluePrint>): List<Pair<Int, Resources>> {

    val timer = Timer(bluePrints.size.toLong())
    return bluePrints
        .mapIndexed { index, bluePrint ->
            println("Processing blueprint ${index + 1}")
            val result = produceGeode(time, bluePrint)
            timer.processed = index.toLong() + 1
            val r = Pair(index + 1, result)
            println("DONE blueprint ${index + 1}: $r took ${globalCounter} steps")


//            states.toList()
//                .sortedByDescending { it.second }
//                .filter { it.first.split(",").last().toInt() > 10 }
//                .take(50)
//                .forEach{ println(it) }

            globalCounter = 0
            states.clear()
            r
        }
}

fun produceGeode(time: Int, bluePrint: BluePrint): Resources {
    val highScore = Optimizer()
    val production = Resources(1, 0, 0, 0)
    val resources = Resources(0, 0, 0, 0)

    return testProduction(time, bluePrint, production, resources, highScore)
}

var globalCounter = 0L;



fun testProduction(
    timeLeft: Int,
    bluePrint: BluePrint,
    production: Resources,
    resources: Resources,
    highScore: Optimizer
): Resources {

    val key = production.asString()+resources.asString()+timeLeft

    val result = states[key]
    if(result!=null)
       return result

    globalCounter++
//    println("$identifier score: ${resources.geode}")

    if (timeLeft == 0)
        return resources


    if (highScore.alreadyWorse(timeLeft, resources, production)) {
//        print("$identifier Stopping")
        return resources
    }

    val newTime = timeLeft - 1

//    val prettyGoodAction = whatsNext(
//        newTime,
//        bluePrint,
//        production,
//        resources,
//        highScore
//    )


    val results = listOfNotNull(
//        prettyGoodAction,
        testProductionIfResources(
            newTime,
            bluePrint,
            production,
            resources,
            bluePrint.geodeRobot,
            highScore
        ),
        testProductionIfResources(
            newTime,
            bluePrint,
            production,
            resources,
            bluePrint.oreRobot,
            highScore
        ),
        testProductionIfResources(
            newTime,
            bluePrint,
            production,
            resources,
            bluePrint.clayRobot,
            highScore
        ),
        testProductionIfResources(
            newTime,
            bluePrint,
            production,
            resources,
            bluePrint.obsidianRobot,
            highScore
        ),
        if (hasResourcesForAnyRobot(bluePrint, resources))
            null
        else
            testProduction(newTime, bluePrint, production, resources.plus(production), highScore)
    )

    val bestResult = results.maxByOrNull { it.geode }!!
    highScore.update(timeLeft, resources)

    if(timeLeft > 10)
        states[key]=bestResult

    return bestResult
}

fun whatsNext(
    timeLeft: Int,
    bluePrint: BluePrint,
    production: Resources,
    resources: Resources,
    highScore: Optimizer
): Resources? {

    if (production.ore <= 1) {
        if (hasResourcesForRobot(bluePrint.oreRobot, resources))
            return testProduction(
                timeLeft,
                bluePrint,
                production.plus(bluePrint.oreRobot.production),
                resources.minus(bluePrint.oreRobot.cost).plus(production),
                highScore
            )
    }

    if (production.clay < 1) {
        if (hasResourcesForRobot(bluePrint.clayRobot, resources))
            return testProduction(
                timeLeft,
                bluePrint,
                production.plus(bluePrint.clayRobot.production),
                resources.minus(bluePrint.clayRobot.cost).plus(production),
                highScore
            )
    }

    if (production.obsidian < 1) {
        if (hasResourcesForRobot(bluePrint.obsidianRobot, resources))
            return testProduction(
                timeLeft,
                bluePrint,
                production.plus(bluePrint.obsidianRobot.production),
                resources.minus(bluePrint.obsidianRobot.cost).plus(production),
                highScore
            )
    }

    if (production.geode < 1) {
        if (hasResourcesForRobot(bluePrint.geodeRobot, resources))
            return testProduction(
                timeLeft,
                bluePrint,
                production.plus(bluePrint.geodeRobot.production),
                resources.minus(bluePrint.geodeRobot.cost).plus(production),
                highScore
            )
    }

    return null
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

private fun bluePrints(): List<BluePrint> {
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
    return bluePrints
}
