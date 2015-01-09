package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile.{Attribute, ConstantPoolEntry, DefaultAttribute, RuntimeVisibleAnnotationsAttribute}

import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.scene.control.TableColumn._

/**
 * author: rwagner
 */
class AttributeTable(attr: Seq[AttributeTable.Type], cp: Seq[ConstantPoolEntry]) extends TableView[AttributeTable.InnerType](ObservableBuffer(attr.map(Option(_)): _*)) {
  editable = false
  columns ++= List(
    new TableColumn[AttributeTable.InnerType, String] {
      text = "type"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c) => StringProperty(c.typ)
        }
      }
    },
    new TableColumn[AttributeTable.InnerType, String] {
      text = "content"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: RuntimeVisibleAnnotationsAttribute) => StringProperty(s"${c.typ} = ${c.annotations}")
          case Some(c: DefaultAttribute) => StringProperty(s"name: $c")
        }
      }
    }
  )
}

object AttributeTable {
  type Type = Attribute
  type InnerType = Option[Type]
}
