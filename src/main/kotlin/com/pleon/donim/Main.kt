package com.pleon.donim

import com.pleon.donim.util.DecorationUtil.centerOnScreen
import com.pleon.donim.util.HostServicesUtil
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}

class Main : Application() {

    override fun start(primaryStage: Stage) {
        SvgImageLoaderFactory.install() // Enable svg wherever other formats are applicable
        Platform.setImplicitExit(false) // For minimize to tray to work correctly
        HostServicesUtil.hostServices = hostServices

        val root = FXMLLoader.load<Parent>(javaClass.getResource("/fxml/scene-splash.fxml"))
        primaryStage.scene = Scene(root).apply { fill = Color.TRANSPARENT }
        primaryStage.isResizable = false
        primaryStage.title = "Donim"
        primaryStage.icons.add(Image("/svg/logo.svg"))
        primaryStage.initStyle(StageStyle.TRANSPARENT)
        primaryStage.show()
        centerOnScreen(primaryStage)
    }

}
