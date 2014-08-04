package in.dogue.simulacrum.world

import in.dogue.antiqua.data.Array2d
import in.dogue.antiqua.graphics.TileRenderer
import in.dogue.antiqua.Antiqua
import Antiqua._

object Board {
  def create(cols:Int, rows:Int, ctrl:Controller) = {
    val tiles = Array2d.tabulate(cols, rows) { case p =>
      BoardTile.create
    }
    Board(tiles, Cursor.create(cols, rows), ctrl)
  }
}

case class Board(tiles:Array2d[BoardTile], cursor:Cursor, controller:Controller) {
  def cols = tiles.cols
  def rows = tiles.rows
  def get(ij:Cell) = tiles.get(ij)
  def update:Board = {
    val move = controller.getMove
    val flip = controller.isFlipping
    val newCursor = cursor.move(move).flip(flip)
    val nt = if (controller.isSwapping) {
      val curTiles = cursor.getTiles
      curTiles.foldLeft(tiles) { case (ts, c) =>
        ts.update(c, _.incr)
      }

    } else {
      tiles
    }
    copy(controller = controller.update, cursor = newCursor, tiles = nt)
  }

  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tiles.foldLeft(tr) { case (r, (p, bt)) =>
      r <+< bt.draw(p |+| ij)

    } <+< cursor.draw(ij)
  }


}
