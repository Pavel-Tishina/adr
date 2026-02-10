package com.paveltsikota.webcore.service.job.controller

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// ?????
class TimeJobController {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private var job: Job? = null
    private var startTimeMillis: Long = 0L

    private val isPaused = MutableStateFlow(false)

    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun start() {
        if (job != null) {
            println("Job already started")
            return
        }

        startTimeMillis = System.currentTimeMillis()

        job = scope.launch {
            println("Job started")

            while (isActive) {

                // Ждём, пока пауза не будет снята
                isPaused
                    .filter { paused -> !paused }
                    .first()

                val now = System.currentTimeMillis()
                val elapsed = now - startTimeMillis

                if (elapsed >= TWO_MINUTES) {
                    println("2 minutes passed. Job finished automatically.")
                    break
                }

                println("Current time: ${LocalTime.now().format(formatter)}")

                delay(1000)
            }
        }
    }

    fun pause() {
        if (job == null) return
        println("Job paused")
        isPaused.value = true
    }

    fun resume() {
        if (job == null) return
        println("Job resumed")
        isPaused.value = false
    }

    fun cancel() {
        println("Job cancelled")
        job?.cancel()
        job = null
    }

    companion object {
        private const val TWO_MINUTES = 2 * 60 * 1000L
    }
}
