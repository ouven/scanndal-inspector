package de.aktey.scanndal.inspector.classfile

import java.io.{File, FileInputStream}
import java.nio.file.Files
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, control => jfxsc}

import de.aktey.scanndal.classfile._
import de.aktey.scanndal.classfile.filter.ClassAccessFlag

import scalafx.Includes._
import scalafx.scene.control.TitledPane._
import scalafx.scene.control.{Label, TitledPane}

/**
 * author: rwagner
 */
class ClassfileController {

  private var f: File = _

  @FXML
  var fileContent: jfxsc.TextArea = _

  @FXML
  var fileInterpretation: jfxsc.Accordion = _

  var view: Parent = _

  def file: File = f

  def file_=(f: File): Unit = {
    this.f = f
    fileContent.text = Files.readAllBytes(file.toPath).map("%02X" format _).mkString
    val ClassFile(magic, minorVersion, majorVersion, constantPool, accessFlags, thisClass, superClass, interfaces, fields, methods, attributes) =
      ClassFileReader.readFromInputStream(new FileInputStream(file))

    val cp = new ConstantPoolUtil(constantPool)

    def makeStringTitledPane(t: String, c: Any) = new TitledPane {
      text = t
      content = new Label("" + c)
    }

    fileInterpretation.panes ++= List(
      makeStringTitledPane("Magic - C0FEBABE", magic),
      makeStringTitledPane(s"Version - $majorVersion.$minorVersion", majorVersion match {
        case 52 => "J2SE 8"
        case 51 => "J2SE 7"
        case 50 => "J2SE 6.0"
        case 49 => "J2SE 5.0"
        case 48 => "JDK 1.4"
        case 47 => "JDK 1.3"
        case 46 => "JDK 1.2"
        case 45 => "JDK 1.1"
        case _ => "unknown"
      }),

      new TitledPane {
        def flag(t: String, f: Int) = if ((accessFlags & f) > 1) Some(t) else None

        text = s"Accessflags - ${"%04x" format accessFlags}"
        content = new Label {
          text = (
            flag("PUBLIC", ClassAccessFlag.PUBLIC) ::
              flag("PRIVATE", ClassAccessFlag.PRIVATE) ::
              flag("PROTECTED", ClassAccessFlag.PROTECTED) ::
              flag("STATIC", ClassAccessFlag.STATIC) ::
              flag("FINAL", ClassAccessFlag.FINAL) ::
              flag("SUPER", ClassAccessFlag.SUPER) ::
              flag("INTERFACE", ClassAccessFlag.INTERFACE) ::
              flag("ABSTRACT", ClassAccessFlag.ABSTRACT) ::
              flag("SYNTHETIC", ClassAccessFlag.SYNTHETIC) ::
              flag("ANNOTATION", ClassAccessFlag.ANNOTATION) ::
              flag("ENUM", ClassAccessFlag.ENUM) ::
              Nil
            ).filter(_.isDefined).mkString(", ")
        }
      },

      makeStringTitledPane(s"This - $thisClass", cp.resolve(thisClass)),
      makeStringTitledPane(s"Super - $superClass", cp.resolve(superClass)),
      new TitledPane {
        text = s"Interfaces - size: ${interfaces.size}"
        content = new InterfacesTable(interfaces, constantPool)
      },
      new TitledPane {
        text = s"Fields - size: ${fields.size}"
        content = new FieldsTable(fields, constantPool)
      },
      new TitledPane {
        text = s"Methods - size: ${methods.size}"
        content = new MethodsTable(methods, constantPool)
      },
      new TitledPane {
        text = s"Attributes - size: ${attributes.size}"
        content = new AttributeTable(attributes, constantPool)
      },

      new TitledPane {
        text = s"ConstantPool - size: ${constantPool.size}"
        content = new ConstantPoolTable(constantPool)
      }
    )
  }
}

object ClassfileController {

  def apply(file: File) = {
    val loader = new FXMLLoader(getClass.getResource("Classfile.fxml"))
    val view = loader.load[Parent]
    val cf = loader.getController[ClassfileController]
    cf.view = view
    cf.file = file
    cf
  }
}
