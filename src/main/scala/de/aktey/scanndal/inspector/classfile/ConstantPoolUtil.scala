package de.aktey.scanndal.inspector.classfile

import de.aktey.scanndal.classfile._

/**
 * author: rwagner
 */
class ConstantPoolUtil(cp: Seq[ConstantPoolEntry]) {

  def resolve(idx: Int) = stringify(cp(idx))
  
  def stringify(cpe: ConstantPoolEntry): String = cpe match {
    case c: ConstantPoolEntryUtf8 => new String(c.bytes, "UTF-8")
    case c: ConstantPoolEntryClass => resolve(c.nameIdx)
    case c: ConstantPoolEntryFieldref => s"${resolve(c.nameIdx)}, ${resolve(c.nameAndTypeIndex)}"
    case c: ConstantPoolEntryNameAndType => s"${resolve(c.nameIdx)}, ${resolve(c.descriptionIdx)}"
    case c: ConstantPoolEntryMethodref => s"name: ${resolve(c.nameIdx)}, nameType: ${resolve(c.nameAndTypeIndex)}"
    case c: ConstantPoolEntryInterfaceMethodref => s"name: ${resolve(c.nameIdx)}, nameType: ${resolve(c.nameAndTypeIndex)}"
    case c: ConstantPoolEntryString => s"idx: ${resolve(c.stringIdx)}"
    case c: ConstantPoolEntryInteger => s"bytes: ${c.bytes}"
    case c: ConstantPoolEntryFloat => s"bytes: ${c.bytes}"
    case c: ConstantPoolEntryLong => s"hbytes: ${c.highBytes}, lbytes: ${c.lowBytes}"
    case c: ConstantPoolEntryDouble => s"hbytes: ${c.highBytes}, lbytes: ${c.lowBytes}"
    case c => c.toString
  }
}
