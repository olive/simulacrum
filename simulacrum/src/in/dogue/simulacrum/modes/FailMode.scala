package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import in.dogue.simulacrum.Simulacrum
import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.input.Controls
import scala.util.Random

object FailMode {
  def create(cols:Int, rows:Int, gm:GameMode, r:Random) = {
    val tg = Simulacrum.gf("blooper")
    val tgSpan = tg.getSpan
    val ntg = tg |++| (((cols - tgSpan.width)/2, 1))
    val tryAgain = Simulacrum.tf.create("Try again: SPACE").toTileGroup |++| ((0,0))
    val toMenu = Simulacrum.tf.create("Main menu: BACK").toTileGroup |++| ((0, 1))
    val text = Vector(tryAgain, toMenu).flatten |++| ((9, rows - 3))
    FailMode(cols, rows, gm, ntg, text, r)
  }
}

case class FailMode private (cols:Int, rows:Int, gm:GameMode, tg:TileGroup, text:TileGroup, r:Random) {
  def update = {
    if (Controls.Back.justPressed) {
      val mode = TitleMode.create(cols, rows, r)
      Transition.create(cols, rows, this.toMode, mode.toMode, r).toMode
    } else if(Controls.Space.justPressed) {
      val mode = GameMode.create(cols, rows, gm.delay, r)
      Transition.create(cols, rows, this.toMode, mode.toMode, r).toMode
    } else {
      this.toMode

    }
  }

  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< gm.draw <++ tg <|| text
  }

  def toMode:Mode = Mode[FailMode](_.update, _.draw, this)
}
