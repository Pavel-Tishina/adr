package com.paveltsikota.webcore.service.job.controller

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.stereotype.Service

@Service
class TimerCoroutineService {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val mutex = Mutex()

    private var job: Job? = null
    private var isPaused = false
    private var elapsedMillis = 0L

    private val maxMillis = 5 * 60 * 1000L
    private val tick = 1000L

    private val _status = MutableStateFlow("⛔ Остановлен")
    val status: StateFlow<String> = _status

    fun start(): String {
        if (job?.isActive == true) return "⚠️ Уже запущен"

        elapsedMillis = 0
        isPaused = false

        job = scope.launch {
            while (isActive && elapsedMillis < maxMillis) {
                delay(tick)

                mutex.withLock {
                    if (!isPaused) {
                        elapsedMillis += tick
                    }

                    _status.value = when {
                        isPaused -> "⏸ Пауза (${elapsedMillis / 1000} сек)"
                        else -> "▶️ Работает (${elapsedMillis / 1000} сек)"
                    }
                }
            }

            if (elapsedMillis >= maxMillis) {
                _status.value = "⛔ Завершён (5 минут)"
                cancel()
            }
        }

        _status.value = "▶️ Запущен"
        return "▶️ Таймер запущен"
    }

    fun pause(): String = runBlocking {
        mutex.withLock {
            if (job?.isActive != true) return@runBlocking "⚠️ Не запущен"
            if (isPaused) return@runBlocking "⚠️ Уже на паузе"

            isPaused = true
            _status.value = "⏸ Пауза (${elapsedMillis / 1000} сек)"
            "⏸ Пауза"
        }
    }

    fun resume(): String = runBlocking {
        mutex.withLock {
            if (job?.isActive != true) return@runBlocking "⚠️ Не запущен"
            if (!isPaused) return@runBlocking "⚠️ Не на паузе"

            isPaused = false
            _status.value = "▶️ Работает (${elapsedMillis / 1000} сек)"
            "▶️ Возобновлён"
        }
    }

    fun cancel(): String {
        job?.cancel()
        job = null
        elapsedMillis = 0
        isPaused = false

        _status.value = "❌ Отменён"
        return "❌ Отменён"
    }
}
