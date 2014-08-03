package in.dogue.simulacrum

import com.deweyvm.gleany.saving.{ControlName, ControlNameCollection}


class SimulacrumControl(descriptor: String) extends ControlName {
  override def name: String = descriptor
}

object SimulacrumControls extends ControlNameCollection[SimulacrumControl] {
  def fromString(string: String): Option[SimulacrumControl] = None
  def makeJoypadDefault: Map[String,String] = Map()
  def makeKeyboardDefault: Map[String,java.lang.Float] = Map()
  def values: Seq[SimulacrumControl] = Seq()
}
