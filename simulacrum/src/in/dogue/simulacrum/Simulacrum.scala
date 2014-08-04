package in.dogue.simulacrum

import in.dogue.antiqua.graphics.{Tile, Border, TextFactory}
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.data.CP437
import in.dogue.antiqua.Antiqua
import Antiqua._

object Simulacrum {
  val tf = TextFactory(Color.Black, Color.White, CP437.unicodeToCode)
  val border = Border(CP437.doubleBorder)(Color.Black, Color.White) _
  def gf(s:String) = {
    Tile.groupFromFile(s, "tiles", CP437.intToCode, _.mkTile(Color.Black, Color.White))
  }
}
