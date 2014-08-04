package in.dogue.simulacrum.world

import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.antiqua.Antiqua
import Antiqua._

object BoardTile {
  val symbols = Vector(CP437.` `, CP437.`·`, CP437./, CP437.X, CP437.*, CP437.☼)
  def create = BoardTile(0, Color.Black, Color.White)
}

case class BoardTile(i:Int, bg:Color, fg:Color) {
  def incr = copy(i=math.min(i+1, 5))
  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <+ (ij, BoardTile.symbols(i).mkTile(bg, fg))
  }

  override def equals(other:Any):Boolean = {
    if (!other.isInstanceOf[BoardTile]) {
      return false
    }
    val t = other.asInstanceOf[BoardTile]
    t.i == i
  }

}
