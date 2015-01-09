package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile._

import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.scene.control.TableColumn._

/**
 * author: rwagner
 */
class ConstantPoolTable(cp: Seq[ConstantPoolTable.Type]) extends TableView[ConstantPoolTable.InnerType](ObservableBuffer(cp.map(Option(_)).zipWithIndex : _*)) {
  editable = false
  columns ++= List(
    new TableColumn[ConstantPoolTable.InnerType, String] {
      text = "#"
      cellValueFactory = { _.value match {
        case (_, idx) => StringProperty(s"$idx")
      }}
    },
    new TableColumn[ConstantPoolTable.InnerType, String] {
      text = "type"
      cellValueFactory = { _.value match {
        case (None, _) => StringProperty("")
        case (Some(c), _) => StringProperty(c.getClass.getSimpleName.substring("ConstantPoolEntry".length))
      }}
    },
    new TableColumn[ConstantPoolTable.InnerType, String] {
      text = "content"
      cellValueFactory = { _.value match {
        case (None, _) => StringProperty("")
        case (Some(c), _) => StringProperty(s(c))
      }}
    },
    new TableColumn[ConstantPoolTable.InnerType, String] {
      text = "raw"
      cellValueFactory = { _.value match {
        case (None, _) => StringProperty("empty")
        case (Some(c : ConstantPoolEntryUtf8), _) => StringProperty(s(c))
        case (Some(c : ConstantPoolEntryClass), _) => StringProperty(s"name: ${c.nameIdx}")
        case (Some(c : ConstantPoolEntryFieldref), _) => StringProperty(s"name: ${c.nameIdx}, nameType: ${c.nameAndTypeIndex}")
        case (Some(c : ConstantPoolEntryNameAndType), _) => StringProperty(s"name: ${c.nameIdx}, description: ${c.descriptionIdx}")
        case (Some(c : ConstantPoolEntryMethodref), _) => StringProperty(s"name: ${c.nameIdx}, nameType: ${c.nameAndTypeIndex}")
        case (Some(c : ConstantPoolEntryInterfaceMethodref), _) => StringProperty(s"name: ${c.nameIdx}, nameType: ${c.nameAndTypeIndex}")
        case (Some(c : ConstantPoolEntryString), _) => StringProperty(s"idx: ${c.stringIdx}")
        case (Some(c : ConstantPoolEntryInteger), _) => StringProperty(s"bytes: ${c.bytes}")
        case (Some(c : ConstantPoolEntryFloat), _) => StringProperty(s"bytes: ${c.bytes}")
        case (Some(c : ConstantPoolEntryLong), _) => StringProperty(s"hbytes: ${c.highBytes}, lbytes: ${c.lowBytes}")
        case (Some(c : ConstantPoolEntryDouble), _) => StringProperty(s"hbytes: ${c.highBytes}, lbytes: ${c.lowBytes}")
        case (Some(c), _) => StringProperty(c.toString)
      }}
    }
  )

  private def s(cpe : ConstantPoolEntry): String = new ConstantPoolUtil(cp).stringify(cpe)

}

object ConstantPoolTable {
  type Type = ConstantPoolEntry
  type InnerType = (Option[ConstantPoolEntry], Int)
}
