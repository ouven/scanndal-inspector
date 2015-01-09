package de.aktey.scanndal.inspector

import javafx.fxml.FXMLLoader
import javafx.scene.Parent

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene

object Inspector extends JFXApp {

  val view : Parent = FXMLLoader.load(getClass.getResource("Inspector.fxml"))

  stage = new JFXApp.PrimaryStage {
    title.value = "Scanndal - class file inspector"
    scene = new Scene(view)
  }
}