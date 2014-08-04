package in.dogue.simulacrum

import in.dogue.antiqua.graphics.{Border, TextFactory}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.data.CP437

object Simulacrum {
  val tf = TextFactory(Color.Black, Color.White, CP437.unicodeToCode)
  val border = Border(CP437.doubleBorder)(Color.Black, Color.White) _
}
