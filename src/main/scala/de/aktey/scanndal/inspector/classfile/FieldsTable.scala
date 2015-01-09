package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile.filter.FieldAccessFlag
import de.aktey.scanndal.classfile.{Attribute, ConstantPoolEntry, Field}

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableCell, TableColumn, TableView}

/**
 * author: rwagner
 */
class FieldsTable(attr: Seq[Field], cp: Seq[ConstantPoolEntry]) extends TableView[FieldsTable.InnerType](ObservableBuffer(attr.map(Option(_)): _*)) {
  editable = false
  columns ++= List(
    new TableColumn[FieldsTable.InnerType, String] {
      text = "access"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Field) => StringProperty((
            (if ((c.accessFlags & FieldAccessFlag.PUBLIC) > 1) Some("PUBLIC") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.PRIVATE) > 1) Some("PRIVATE") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.PROTECTED) > 1) Some("PROTECTED") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.STATIC) > 1) Some("STATIC") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.FINAL) > 1) Some("FINAL") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.VOLATILE) > 1) Some("VOLATILE") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.TRANSIENT) > 1) Some("TRANSIENT") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.SYNTHETIC) > 1) Some("SYNTHETIC") else None) ::
              (if ((c.accessFlags & FieldAccessFlag.ENUM) > 1) Some("ENUM") else None) ::
              Nil
            ).filter(_.isDefined).flatten.mkString(", ")
          )
        }
      }
    },
    new TableColumn[FieldsTable.InnerType, String] {
      text = "name"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Field) => StringProperty(s(c.nameIndex))
        }
      }
    },
    new TableColumn[FieldsTable.InnerType, String] {
      text = "descriptor"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Field) => StringProperty(s(c.descriptorIndex))
        }
      }
    },
    new TableColumn[FieldsTable.InnerType, Seq[Attribute]] {
      text = "attributes"
      cellValueFactory = {
        _.value match {
          case None => ObjectProperty(Nil)
          case Some(c: Field) => ObjectProperty(c.attributes)
        }
      }
      cellFactory = { _ =>
        new TableCell[FieldsTable.InnerType, Seq[Attribute]] {
          item.onChange { (_, _, attributes) => graphic = new AttributeTable(Option(attributes).getOrElse(Nil), cp)}
        }
      }
    },
    new TableColumn[FieldsTable.InnerType, String] {
      text = "raw"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Field) => StringProperty("" + c)
        }
      }
    }
  )

  private def s(idx: Int): String = new ConstantPoolUtil(cp).resolve(idx)

}

object FieldsTable {
  type InnerType = Option[Field]
}