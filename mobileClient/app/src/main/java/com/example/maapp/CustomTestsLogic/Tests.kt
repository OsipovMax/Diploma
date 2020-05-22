package com.example.maapp.CustomTestsLogic

import android.view.View
import com.example.maapp.presentation.ui.tests.Modes
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class Tests(
    private val generalHeight: Int = 120,
    private val threadCount: Int = Runtime.getRuntime().availableProcessors() - 1,
    private val cycleCount: Int = 1000) {
    private val GROUP_SIZE: Int  = 1000
    private val AGE: Int = 17
    private val HEIGHT: Double = 178.73
    private val a = 5.5
    private val b = 3.5
    private val c = 7.3


    data class Person (var height: Double = 0.0, var age: Int)

    var computingScope = CoroutineScope(Dispatchers.IO)
    private var arrayWallCharacteristic: ArrayList<WallCharacteristic> = ArrayList(threadCount)
    private val arithmeticParameter = 3.3
    private var withoutMem = 5.5

    private val mtContext = newFixedThreadPoolContext(threadCount, "mtPool")
    private val mutex = Mutex()

    private var finallyRes = 0
    fun getRes(): Int {
        return finallyRes
    }


    suspend fun CoroutineScope.characteristicFunctionBuildWall(action: suspend() -> Unit) {
        val jobs = List(threadCount) {
            launch {
                action()
            }
        }
        jobs.forEach { it.join() }
    }

    fun testCoroutineFunc(mode: Modes) : ArrayList<Job> {
        val jobs : ArrayList<Job> = arrayListOf()
        finallyRes = 0
        for (j in 1..threadCount){
            jobs.add(
                computingScope.launch (start = CoroutineStart.LAZY) {
                    var res = 0
                    res = buildWallTest(mode)
                    mutex.lock()
                    finallyRes += res
                    mutex.unlock()
                }
            )
        }
        return jobs
    }
    fun buildWallTest(mode: Modes) : Int {
        val firstParam: Array<Double> = Array(cycleCount){3.7}
        val secondParam: Array<Double> = Array(cycleCount){5.3}
        val thirdParam: Array<Double> = Array(cycleCount){1.9}
        val wallArr: Array<Double> = Array(cycleCount){17.1}
        val beginTime: Long
        var wallLength = 0
        if (mode.name == "MEMORY") {
            beginTime = System.currentTimeMillis()
            memoryLoop@ while(true) {
                wallLength++
                for (j in 0 until generalHeight) {
                    for (k in 0 until cycleCount) {
                        wallArr[k] = firstParam[k] * secondParam[k] + thirdParam[k]
                        wallArr[k] = firstParam[k]
                        firstParam[k] = secondParam[k]
                        secondParam[k] = thirdParam[k]
                        thirdParam[k] = wallArr[k]
                    }
                }
                if(System.currentTimeMillis() - beginTime > 5 * 1000){
                    break@memoryLoop
                }
            }
        }
        else {
            beginTime = System.currentTimeMillis()
            procLoop@ while (true) {
                wallLength++
                for (j in 0 until generalHeight) {
                    for (k in 0 until cycleCount) {
                        withoutMem += a * 3.5 + b
                        withoutMem -= withoutMem * 0.5
                    }
                }
                if (System.currentTimeMillis() - beginTime > 5 * 1000) {
                    break@procLoop
                }
            }
            withoutMem += withoutMem * 0.32
        }
        System.gc()
        return (wallLength * 0.25).toInt()
    }

    suspend fun characteristicFunctionBuildWall (itemView: View, checkBoxFlag: Boolean) : ArrayList<WallCharacteristic> {
        CoroutineScope(mtContext).characteristicFunctionBuildWall {
            var wallCharacteristic = WallCharacteristic(0, 0.0)
            val beginTime = System.currentTimeMillis()
            var withoutMem = 0.0
            var i: Int = -1
            while (System.currentTimeMillis() - beginTime < 15 * 1000) {
                i++
                if (checkBoxFlag) {
                    for (j in 0 until generalHeight) {
                        for (k in 0 until (cycleCount)) {
                            //wall[i][j] = firstParam[k] * secondParam[k] + thirdParam[k]
                        }
                    }
                }
                else {
                    for (j in 0 until generalHeight) {
                        for (k in 0 until cycleCount) {
                            withoutMem = arithmeticParameter * arithmeticParameter + arithmeticParameter
                        }
                    }
                }
            }
            mutex.withLock {
                wallCharacteristic.height = generalHeight
                wallCharacteristic.length = i * 0.25
                wallCharacteristic.breakCount = wallCharacteristic.height * i
                arrayWallCharacteristic.add(wallCharacteristic)

            }
        }
        return arrayWallCharacteristic
    }

    fun personGenerator(personCount : Int) : Array<Person> {
        val personArray = Array(personCount){ Person(0.0, 0) }
        val randomHeight = List(personCount) { Random.nextDouble(130.0,195.0)}
        val randomAge = List(personCount) { Random.nextInt(5,21)}
        val person = Person(HEIGHT, AGE)
        println(randomAge.size)
        for (i in 0 until personCount) {
            personArray[i].age = randomAge[i]
            personArray[i].height = randomHeight[i]
        }
        personArray[1001].age = AGE
        personArray[1001].height = HEIGHT
        return personArray
    }

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    suspend fun quadraticComplexitySearchTest(itemView: View) : ArrayList<Int> {
        var objectsArray = personGenerator(GROUP_SIZE)
        var successFlag = 0
        var resultArray : ArrayList<Int> = ArrayList(2)
        var resultPersonCount = 0
        lateinit var jobs: Job
        for (it in 0 until threadCount) {
            jobs = CoroutineScope(mtContext).launch {
                val beginPosition = it * (objectsArray.size / threadCount)
                val endPosition = (it + 1) * (objectsArray.size / threadCount)
                val beginTime = System.currentTimeMillis()
                println(beginPosition)
                println(endPosition)
                loop@ for (i in beginPosition until endPosition) {
                    resultPersonCount = i

                    for (j in i + 1 until endPosition) {
                        //println("j = ${j}")
                        // for (k in 0 until 100) {
                        for (l in 0 until cycleCount) {
                            //tempParam[0] = firstParam[l] * secondParam[l] + thirdParam[l]
                        }
                        //}
                        if ((objectsArray[i].height == objectsArray[j].height)) {
                            successFlag = 1
                        }
                        if (System.currentTimeMillis() - beginTime > 15 * 1000) {
                            break@loop
                        }
                    }
                }

            }

        }
        jobs.join()
        if (successFlag == 1) {
            resultArray.add(1)
            resultArray.add(resultPersonCount)
        }
        else{
            resultArray.add(0)
            resultArray.add(resultPersonCount)
        }
        return resultArray
    }

    suspend fun searchTest (itemView: View) : ArrayList<Int> {
        var objectsArray = personGenerator(GROUP_SIZE)
        var successFlag = 0
        var resultArray : ArrayList<Int> = ArrayList(2)
        var resultPersonCount = 0
        lateinit var jobs: Job


        for (it in 0 until threadCount){
            jobs = CoroutineScope(mtContext).launch {
                val beginPosition = it * (objectsArray.size / threadCount)
                val endPosition = (it + 1) * (objectsArray.size / threadCount)
                val beginTime = System.currentTimeMillis()

                loop@ for (i in beginPosition until endPosition ) {
                    var temp = System.currentTimeMillis() - beginTime
                    resultPersonCount = i
                    for (j in 0 until 100) {
                        for (k in 0 until cycleCount) {
                            //tempParam[0] = firstParam[k] * secondParam[k] + thirdParam[k]
                        }
                    }
                    if ((objectsArray[i].height == HEIGHT) and (objectsArray[i].age == AGE)){
                        successFlag = 1
                    }
                    if (System.currentTimeMillis() - beginTime > 15 * 1000){
                        break@loop
                    }
                }
            }
        }
        jobs.join()
        if (successFlag == 1) {
            resultArray.add(1)
            resultArray.add(resultPersonCount)
        }
        else{
            resultArray.add(0)
            resultArray.add(resultPersonCount)
        }
        return resultArray
    }


    fun searchCoroutineFunc(mode: Modes) : ArrayList<Job> {
        val jobs : ArrayList<Job> = arrayListOf()
        finallyRes = 0
        for (j in 1..threadCount){
            jobs.add(
                computingScope.launch (start = CoroutineStart.LAZY) {
                    var res = 0
                    res = searchTest(mode, 5)
                    mutex.lock()
                    finallyRes += res
                    mutex.unlock()
                }
            )
        }
        return jobs
    }

    fun searchTest(mode: Modes, dur: Int) : Int {
        var resultPersonCount = 0
        val firstParam: Array<Double> = Array(cycleCount){3.7}
        val secondParam: Array<Double> = Array(cycleCount){5.3}
        val thirdParam: Array<Double> = Array(cycleCount){1.9}
        val tempParam: Array<Double> = Array(1){17.1}
        val beginTime = System.currentTimeMillis()
        if (mode.name == "PROCESSOR") {
            loop@ while(true){
                resultPersonCount++
                for (k in 0 until cycleCount) {
                    for (j in 0 until 100) {
                        tempParam[0] = firstParam[j] * secondParam[j] + thirdParam[j]
                    }
                }
                if (System.currentTimeMillis() - beginTime > dur * 1000) {
                    break@loop
                }
            }
        }
        else {
            loop2@ for (i in 0 until GROUP_SIZE){
                resultPersonCount++
                for (k in i + 1 until GROUP_SIZE){
                    for (j in 0 until 100) {
                        for (p in 0 until cycleCount) {
                            tempParam[0] = firstParam[p] * secondParam[p] + thirdParam[p]
                        }
                    }
                    if (System.currentTimeMillis() - beginTime > 5 * 1000) {
                        break@loop2
                    }
                }
            }
        }
        return resultPersonCount
    }


    fun bandwidthCoroutineFunc(mode: Modes) : ArrayList<Job> {
        val jobs : ArrayList<Job> = arrayListOf()
        finallyRes = 0
        for (j in 1..threadCount){
            jobs.add(
                computingScope.launch (start = CoroutineStart.LAZY) {
                    var res = 0
                    res = bandwidthTest(mode)
                    println("BAND RES = $res")
                    mutex.lock()
                    finallyRes += res
                    mutex.unlock()
                }
            )
        }
        return jobs
    }

    fun bandwidthTest(mode: Modes) : Int {
        var numAuto = 0
        var beginTime = 0.0.toLong()
        val firstParam: Array<Double> = Array(cycleCount){3.7}
        val secondParam: Array<Double> = Array(cycleCount){5.3}
        val thirdParam: Array<Double> = Array(cycleCount){1.9}
        val tempParam: Array<Double> = Array(1){17.1}
        if (mode.name == "PROCESSOR") {
            beginTime = System.currentTimeMillis()
            procLoop@ while (true) {
                numAuto++
                for (j in 0 until cycleCount) {
                    for (k in 0 until cycleCount) {
                        withoutMem += a * b + c
                        withoutMem -= withoutMem * 0.5
                    }
                }
                if(System.currentTimeMillis() - beginTime > 5 * 1000){
                    break@procLoop
                }
            }
            withoutMem += withoutMem * 0.32
        }
        else {
            beginTime = System.currentTimeMillis()
            memoryLoop@ while (true) {
                numAuto++
                for (j in 0 until cycleCount) {
                    for (k in 0 until cycleCount) {
                        tempParam[0] = firstParam[k] * secondParam[k] + thirdParam[k]
                    }
                }
                if (System.currentTimeMillis() - beginTime > 5 * 1000) {
                    break@memoryLoop
                }
            }
        }
        return numAuto
    }

}