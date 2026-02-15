package com.paveltsikota.webcore.rest.controller.test

import com.paveltsikota.webcore.service.job.controller.TimerCoroutineService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Deprecated(message = "Only 4 testing")
@RestController
@RequestMapping("/timer")
class TimerController(
    private val timerService: TimerCoroutineService
) {

    @PostMapping("/start")
    fun start() = timerService.start()

    @PostMapping("/pause")
    fun pause() = timerService.pause()

    @PostMapping("/resume")
    fun resume() = timerService.resume()

    @PostMapping("/cancel")
    fun cancel() = timerService.cancel()
}