package in.dogue.simulacrum.world

import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.antiqua.Antiqua
import Antiqua._

object BoardTile {
  val symbols = Vector(CP437.` `, CP437.`·`, CP437./, CP437.X, CP437.*, CP437.☼)
}

case class BoardTile(i:Int, bg:Color, fg:Color) {
  def incr = copy(i=i+1)
  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <+ (ij, BoadTile.symbols(i).mkTile(bg, fg))
  }

}
