package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Tile, TileRenderer}
import com.deweyvm.gleany.graphics.Color

case class DimCover(cols:Int, rows:Int) {
  def draw(dim:Double)(tr:TileRenderer):TileRenderer = {
    val draws = for (i <- 0 until cols ; j <- 0 until rows) yield {
      def f(t:Tile) = t.mapBg(_.mix(Color.Black, dim)).mapFg(_.mix(Color.Black, dim))
      ((i, j), f _)
    }
    tr `$$>` draws
  }
}
