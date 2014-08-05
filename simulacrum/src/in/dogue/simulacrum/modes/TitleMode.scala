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
import in.dogue.simulacrum.ui.Slider
import in.dogue.simulacrum.audio.SoundManager

object TitleMode {
  def create(cols:Int, rows:Int, delay:Int, r:Random) = {
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
      tf.create("Do as you see.").toTileGroup |++| ((-1, 0)),
      tf.create("move:  ↑ ↓ → ←").toTileGroup |++| ((0,2)),
      tf.create("add:   SPACE").toTileGroup |++| ((0,3)),
      tf.create("flip: LEFT_SHIFT").toTileGroup |++| ((0,4))
    ).flatten.sfilter{ case c => c.code != CP437.` `.toCode }
    val slider = Slider.createInt("Delay:", delay, 10)
    val fliptg = flip(tg)
    TitleMode(cols, rows, rect, tg |++| ((1,2)), fliptg |++| ((1, 9)), texts |++| ((10, 7)), slider, r)
  }

  private def flip(tg:TileGroup) = {
    val span = tg.getSpan
    tg.map { case ((i, j), t) =>
      val newCode = if (t.code == CP437.▄.toCode) {
        CP437.▀.toCode
      } else if (t.code == CP437.▀.toCode) {
        CP437.▄.toCode
      } else {
        t.code
      }
      ((i, (span.height - j)), t.setFg(Color.Black).setCode(newCode))

    }
  }
}

case class TitleMode private (cols:Int, rows:Int, rect:Rect, tg:TileGroup, rtg:TileGroup, text:TileGroup, slider:Slider[Int], r:Random) {
  def update:Mode = {
    if (!SoundManager.thing.isPlaying) {
      SoundManager.thing.play()
    }
    if (Controls.Space.justPressed) {
      val cd = CountMode.create(GameMode.create(cols, rows, slider.getValue, r).toMode)
      SoundManager.thing.stop()
      Transition.create(cols, rows, this.toMode, cd.toMode, r).toMode
    } else {
      copy(slider=slider.update).toMode
    }
  }
  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< rect.draw((0,0)) <|| tg <|| rtg <|| text <+< slider.draw((16, 13))
  }

  def toMode:Mode = Mode[TitleMode](_.update, _.draw, this)
}
