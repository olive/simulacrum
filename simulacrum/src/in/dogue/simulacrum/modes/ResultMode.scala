package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import in.dogue.simulacrum.Simulacrum
import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.input.Controls
import scala.util.Random
import in.dogue.simulacrum.audio.SoundManager

object ResultMode {
  def create(cols:Int, rows:Int, gm:GameMode, isHiscore:Boolean, r:Random) = {
    val bloop = Simulacrum.gf("blooper")
    val hisc = Simulacrum.gf("hiscore")
    val tg = isHiscore.select(bloop, hisc)
    val bloopSpan = bloop.getSpan
    val hiscSpan = hisc.getSpan
    val offset1 = ((cols - bloopSpan.width)/2, 1)
    val offset2 = ((cols - hiscSpan.width)/2, 1)
    val fail = tg |++| offset1
    val hiscore = hisc |++| offset2
    val tryAgain = Simulacrum.tf.create("Try again: ENTER").toTileGroup |++| ((0,0))
    val toMenu = Simulacrum.tf.create("Main menu: BACK").toTileGroup |++| ((0, 1))
    val text = Vector(tryAgain, toMenu).flatten |++| ((9, rows - 3))
    ResultMode(cols, rows, gm, isHiscore, fail, hiscore, text, gm.delay, 0, r)
  }
}

case class ResultMode private (cols:Int, rows:Int, gm:GameMode, isHighscore:Boolean, tg:TileGroup, hs:TileGroup, text:TileGroup, delay:Int, t:Int, r:Random) {
  def update = {
    if (Controls.Back.justPressed) {
      val mode = TitleMode.create(cols, rows, delay, r)
      Transition.create(cols, rows, this.toMode, mode.toMode, r).toMode
    } else if(Controls.Enter.justPressed) {
      val mode = GameMode.create(cols, rows, gm.delay, r)
      Transition.create(cols, rows, this.toMode, mode.toMode, r).toMode
    } else {
      if (isHighscore && t == 120) {
        SoundManager.victory.play()
        copy(tg=hs, t=t+1).toMode
      } else {
        copy(t=t+1).toMode
      }


    }
  }

  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< gm.draw <++ tg <|| text
  }

  def toMode:Mode = Mode[ResultMode](_.update, _.draw, this)
}
