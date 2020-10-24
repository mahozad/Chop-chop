package com.pleon.donim.node

import com.pleon.donim.*
import com.pleon.donim.Animatable.AnimationDirection.FORWARD
import com.pleon.donim.Animatable.AnimationProperties
import javafx.scene.text.Text
import javafx.util.Duration

class Time : Text(), Animatable {

    private lateinit var timer: Timer
    private lateinit var animationProperties: AnimationProperties

    private fun createTimer(onEnd: () -> Unit = {}) {
        timer = Timer(animationProperties.duration, Duration.seconds(1.0), onEnd)
        if (animationProperties.direction == FORWARD) {
            timer.elapsedTimeProperty().addListener { _, _, elapsedTime -> text = format(elapsedTime) }
        } else {
            timer.remainingTimeProperty().addListener { _, _, remainingTime -> text = format(remainingTime) }
        }
    }

    private fun format(duration: Duration) =
            String.format("%02d:%02d", duration.toMinutes().toInt(), duration.toSeconds().toInt() % 60)

    fun isFresh() = timer.elapsedTimeProperty().value == Duration.ZERO

    override fun startAnimation() {
        if (!this::timer.isInitialized) createTimer()
        timer.start()
    }

    override fun pauseAnimation() {
        if (this::timer.isInitialized) timer.stop()
    }

    override fun resetAnimation(properties: AnimationProperties) {
        animationProperties = properties
        // TODO: Also remove listeners from timer properties to avoid memory leak
        if (this::timer.isInitialized) timer.stop()
        createTimer()
        text = format(animationProperties.duration)
    }

    override fun endAnimation(onEnd: () -> Unit, graceful: Boolean, graceDuration: Duration) {
        // TODO: Also remove listeners from timer properties to avoid memory leak
        timer.stop()
        if (graceful) {
            runGraceAnimation(graceDuration, onEnd)
        } else {
            text = format(if (animationProperties.direction == FORWARD) animationProperties.duration else Duration.ZERO)
            onEnd()
        }
    }

    private fun runGraceAnimation(graceDuration: Duration, onEnd: () -> Unit) {
        val remaining = timer.remainingTimeProperty().value
        timer = Timer(graceDuration, Duration.millis(30.0), onEnd)
        timer.elapsedTimeProperty().addListener { _, _, elapsedTime ->
            val fractionOfTimer = elapsedTime / graceDuration
            val fractionOfRemaining = remaining * fractionOfTimer
            text = format(remaining - fractionOfRemaining)
        }
        timer.start()
    }
}
