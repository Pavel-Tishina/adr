//package com.paveltsikota.webcore.websocket
//
//import org.springframework.scheduling.TaskScheduler
//import org.springframework.stereotype.Service
//import java.time.Instant
//import java.util.concurrent.ConcurrentHashMap
//
//@Service
//class WebSocketTaskManager(private val scheduler: TaskScheduler) {
//
//    private val tasks = ConcurrentHashMap<String, ManagedTask>()
//
//    fun register(task: ManagedTask) {
//        tasks[task.id] = task
//    }
//
//    fun start(id: String) {
//        tasks[id]?.let { task ->
//            scheduler.schedule({ task.start() }, Instant.now())
//        }
//    }
//
//    fun pause(id: String) = tasks[id]?.pause()
//    fun resume(id: String) = tasks[id]?.resume()
//    fun cancel(id: String) = tasks[id]?.cancel()
//
//    fun getProgress(id: String): Double? = tasks[id]?.getProgress()
//    fun getStatus(id: String): TaskStatus? = tasks[id]?.status
//}