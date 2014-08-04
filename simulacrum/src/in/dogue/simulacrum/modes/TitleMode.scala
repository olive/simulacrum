package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Rect, Tile, TileRenderer}
import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._
import scala.util.Random
import in.dogue.simulacrum.Simulacrum
import in.dogue.simulacrum.input.Controls

object TitleMode {
  def create(cols:Int, rows:Int, r:Random) = {
    def mkTile(r:Random) = {
      val bg = Color.Grey.dim(6 + r.nextDouble)
      val fg = Color.Grey.dim(3 + r.nextDouble)
      val code = Vector(CP437.-, CP437.`=`, CP437.≡, CP437.` `, CP437.` `).randomR(r)
      code.mkTile(bg, fg)
    }
    val rect= Rect.createTextured(cols, rows, mkTile, r)
    val tg = Tile.groupFromFile("title", "tiles", CP437.intToCode, c => c.mkTile(Color.Black, Color.White))
    import Simulacrum._
    val texts = Vector(
      tf.create("move: ↑↓→←").toTileGroup |++| ((0,0)),
      tf.create("swap: SPACE").toTileGroup |++| ((0,1)),
      tf.create("flip: LEFT_SHIFT").toTileGroup |++| ((0,2)),
      tf.create("Do as you see.").toTileGroup |++| ((0, 4))
    ).flatten
    TitleMode(cols, rows, rect, tg |++| ((1,2)), texts |++| ((9, 8)), r)
  }
}

case class TitleMode private (cols:Int, rows:Int, rect:Rect, tg:TileGroup, text:TileGroup, r:Random) {
  def update:Mode = {
    if (Controls.Space.justPressed) {
      val cd = CountMode.create(GameMode.create(cols, rows, r).toMode)
      Transition.create(cols, rows, this.toMode, cd.toMode, r).toMode
    } else {
      this.toMode
    }
  }
  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< rect.draw((0,0)) <|| tg <|| text
  }

  def toMode:Mode = Mode[TitleMode](_.update, _.draw, this)
}
