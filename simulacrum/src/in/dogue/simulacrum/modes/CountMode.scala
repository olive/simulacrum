package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.simulacrum.Simulacrum

object CountMode {
  def create(gm:Mode) = CountMode(gm, Counter.create)
}

case class CountMode(gameMode:Mode, ctr:Counter) {
  def update = {
    if (ctr.isDone) {
      gameMode
    } else {
      copy(ctr=ctr.update).toMode
    }
  }
  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< gameMode.draw <+< selfDraw
  }

  private def selfDraw(tr:TileRenderer):TileRenderer = {
    tr <+< ctr.draw((13,6))
  }

  def toMode:Mode = Mode[CountMode](_.update, _.draw, this)
}
