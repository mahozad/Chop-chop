package ir.mahozad.donim.model

import javafx.scene.paint.Color
import javafx.scene.paint.Color.hsb
import javafx.util.Duration
import java.awt.TrayIcon.MessageType

val DEFAULT_FOCUS_DURATION: Duration = Duration.minutes(25.0)
val DEFAULT_BREAK_DURATION: Duration = Duration.minutes(2.0)

enum class Period(var duration: Duration,
                  val baseColor: Color,
                  val notification: String,
                  val notificationType: MessageType,
                  val defaultDuration: Duration) {

    FOCUS(DEFAULT_FOCUS_DURATION,
            hsb(110.0, 0.9, 1.0),
            "Your focus time started",
            MessageType.INFO,
            DEFAULT_FOCUS_DURATION),

    BREAK(DEFAULT_BREAK_DURATION,
            hsb(0.0, 0.9, 1.0),
            "Please take a break and return later",
            MessageType.WARNING,
            DEFAULT_BREAK_DURATION);

    lateinit var nextPeriod: Period

    fun setDuration(duration: String) {
        if (duration.isNotBlank()) {
            this.duration = Duration.minutes(duration.toDouble())
        } else {
            this.duration = defaultDuration
        }
    }
}
