package in.dogue.simulacrum.world

import in.dogue.antiqua.data.Array2d
import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.antiqua.Antiqua
import Antiqua._

case class Board(tiles:Array2d[BoardTile]) {
  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tiles.foldLeft(tr) { case (r, (p, bt)) =>
      r <+< bt.draw(p)

    }
  }
}
