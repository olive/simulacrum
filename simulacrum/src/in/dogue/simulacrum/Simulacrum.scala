package in.dogue.simulacrum

import in.dogue.antiqua.graphics.{TextFactory}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.data.CP437

object Simulacrum {
  val tf = TextFactory(Color.Black, Color.White, CP437.unicodeToCode)
}
