package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile.ConstantPoolEntry

import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.scene.control.TableColumn._

/**
 * author: rwagner
 */
class InterfacesTable(attr: Seq[Int], cp: Seq[ConstantPoolEntry]) extends TableView[InterfacesTable.InnerType](ObservableBuffer(attr.map(Option(_)): _*)) {
  editable = false
  columns ++= List(
    new TableColumn[InterfacesTable.InnerType, String] {
      text = "raw"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c) => StringProperty(c.toString)
        }
      }
    },
    new TableColumn[InterfacesTable.InnerType, String] {
      text = "name"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c) => StringProperty(s"name: ${s(c)}")
        }
      }
    }
  )

  private def s(idx: Int): String = new ConstantPoolUtil(cp).resolve(idx)

}

object InterfacesTable {
  type InnerType = Option[Int]
}