package de.aktey.scanndal.inspector

import javafx.fxml.FXML
import javafx.scene.{control => jfxsc}
import javafx.{event => jfxe}

import de.aktey.scanndal.inspector.classfile.ClassfileController

import scalafx.Includes._
import scalafx.scene.control.Tab
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter

/**
 * author: rwagner
 */
class InspectorController {

  @FXML
  var openedFilesPanel: jfxsc.TabPane = _

  @FXML
  private def handleFileOpen(event: jfxe.ActionEvent): Unit = {
    val fc = new FileChooser
    fc.setSelectedExtensionFilter(new ExtensionFilter("JVM class file", "class"))
    Option(fc.showOpenDialog(null)) map { file =>
      openedFilesPanel += new Tab {
        text = file.getName
        content = ClassfileController(file).view
      }
    }
  }
}
