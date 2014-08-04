package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import in.dogue.simulacrum.Simulacrum
import in.dogue.antiqua.Antiqua.TileGroup

object FailMode {
  def create(gm:GameMode) = {
    val tg = Simulacrum.gf("blooper")
    FailMode(gm, tg)
  }
}

case class FailMode(gm:GameMode, tg:TileGroup) {
  def update = this.toMode

  def draw(tr:TileRenderer):TileRenderer = {
    tr <+< gm.draw <++ tg
  }

  def toMode:Mode = Mode[FailMode](_.update, _.draw, this)
}
