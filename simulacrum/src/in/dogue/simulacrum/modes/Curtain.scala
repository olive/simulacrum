package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{TileRenderer, Rect}
import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.data.{Code, CP437}
import com.deweyvm.gleany.graphics.Color
import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._
import com.deweyvm.gleany.data.Recti

object Curtain {
  def create(left:Boolean, cols:Int, rows:Int, r:Random) = {
    def mkTile(c:Code) = {
      val fg = Color.Red.mix(Color.Orange, 0.2*r.nextDouble)
      val bg = Color.Red.dim(3+r.nextDouble)
      c.mkTile(bg, fg)
    }
    val tiles = for (i <- 0 until cols; j <- 0 until rows) yield {
      val c = if (j == 0) {
        if ((i == cols - 1 && left) || (i == 0 && !left)) {
          CP437.│
        } else {
          CP437.~
        }
      } else if (j == rows - 1) {
        CP437.⌡
      } else {
        CP437.│
      }
      ((i,j),  mkTile(c.toCode))
    }
    Curtain(tiles, tiles.getSpan)
  }
}

case class Curtain(r:TileGroup, span:Recti) {
  def width = span.width
  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <++ (r |++| ij)
  }
}
