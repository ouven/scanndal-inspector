package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile.filter.MethodAccessFlag
import de.aktey.scanndal.classfile.{Attribute, ConstantPoolEntry, Method}

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableCell, TableColumn, TableView}

/**
 * author: rwagner
 */
class MethodsTable(attr: Seq[Method], cp: Seq[ConstantPoolEntry]) extends TableView[MethodsTable.InnerType](ObservableBuffer(attr.map(Option(_)): _*)) {
  editable = false
  columns ++= List(
    new TableColumn[MethodsTable.InnerType, String] {
      text = "access"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Method) => StringProperty((
            (if ((c.accessFlags & MethodAccessFlag.PUBLIC) > 1) Some("PUBLIC") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.PRIVATE) > 1) Some("PRIVATE") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.PROTECTED) > 1) Some("PROTECTED") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.STATIC) > 1) Some("STATIC") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.FINAL) > 1) Some("FINAL") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.SYNCHRONIZED) > 1) Some("SYNCHRONIZED") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.VARARGS) > 1) Some("VARARGS") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.NATIVE) > 1) Some("NATIVE") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.ABSTRACT) > 1) Some("ABSTRACT") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.STRICT) > 1) Some("STRICT") else None) ::
              (if ((c.accessFlags & MethodAccessFlag.SYNTHETIC) > 1) Some("SYNTHETIC") else None) ::
              Nil
            ).filter(_.isDefined).flatten.mkString(", ")
          )
        }
      }
    },
    new TableColumn[MethodsTable.InnerType, String] {
      text = "name"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Method) => StringProperty(s(c.nameIndex))
        }
      }
    },
    new TableColumn[MethodsTable.InnerType, String] {
      text = "descriptor"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Method) => StringProperty(s(c.descriptorIndex))
        }
      }
    },
    new TableColumn[MethodsTable.InnerType, Seq[Attribute]] {
      text = "attributes"
      cellValueFactory = {
        _.value.fold(ObjectProperty(Seq[Attribute]())) { c => ObjectProperty(c.attributes)}
      }
      cellFactory = { p =>
        new TableCell[MethodsTable.InnerType, Seq[Attribute]] {
          item.onChange { (_, _, attrs) => graphic = new AttributeTable(Option(attrs).getOrElse(Nil), cp)}
        }
      }
    },
    new TableColumn[MethodsTable.InnerType, String] {
      text = "raw"
      cellValueFactory = {
        _.value match {
          case None => StringProperty("")
          case Some(c: Method) => StringProperty("" + c)
        }
      }
    }
  )

  private def s(idx: Int): String = new ConstantPoolUtil(cp).resolve(idx)

}

object MethodsTable {
  type InnerType = Option[Method]
}