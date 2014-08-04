package in.dogue.simulacrum.world

import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import com.deweyvm.gleany.graphics.Color

object Cursor {
  def create(bcols:Int, brows:Int) = {
    Cursor((bcols/2, brows/2), Horizontal)
  }
}

case class Cursor(pos:Cell, or:Orientation) {
  def move(dd:(Int,Int)) = copy(pos=pos |+| dd)
  def flip(o:Boolean) = {
    copy(or=o.select(or, or.flip))
  }

  def getTiles:Seq[Cell] = {
    or match {
      case Horizontal => Seq(pos |- 1, pos, pos |+ 1)
      case Vertical => Seq(pos -| 1, pos, pos +| 1)
    }
  }

  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    val cells = getTiles
    val draws = cells.map { p =>
      def f(t:Tile) = t.setBg(Color.Blue)
      (ij |+| p, f _)
    }
    tr `$$>` draws
  }
}
