package in.dogue.simulacrum.world

import in.dogue.antiqua.graphics.{TextFactory, TileRenderer, Tile}
import scala.util.Random
import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.data.CP437
import in.dogue.simulacrum.Simulacrum
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.saving.Hiscore

object PointBox {
  def create(mkTile:Random=>Tile, mkBorder:Random=>Tile, r:Random) = {
    val cols = 7
    val rows = 5
    val tiles = for (i <- 0 until cols; j <- 0 until rows) yield {
      val tile = if ((j != 0 && j != rows - 1) && (i == 0 || i == cols - 1)) {
        mkBorder(r).setCode(CP437.│)
      } else if (i == 0 && j == 0) {
        mkBorder(r).setCode(CP437.┌)
      } else if (i == cols - 1 && j == 0) {
        mkBorder(r).setCode(CP437.┐)
      } else if (i == 0 && j == rows - 1){
        mkBorder(r).setCode(CP437.└)
      } else if (i == cols - 1 && j == rows - 1) {
        mkBorder(r).setCode(CP437.┘)
      } else if ((j == 0 || j == rows - 1) && i != cols/2 ) {
        mkBorder(r).setCode(CP437.─)
      } else if (j == 0 || j == rows - 1) {
        val code = (j == 0).select(CP437.┬, CP437.┴)
        mkBorder(r).setCode(code)
      } else {
        mkTile(r)
      }
      ((i, j), tile)
    }
    PointBox(0, Hiscore.get, Simulacrum.tf, tiles)
  }
}

case class PointBox private (points:Int, hiscore:Int, tf:TextFactory, box:TileGroup) {
  def give(p:Int) = copy(points=points+p)

  def getPoints = if (points > hiscore) points else hiscore

  def isHiscore = points > hiscore

  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    val x0 = 1
    val y0 = 2
    (tr <++ (box |++| ij)
        <+< tf.create("%5s".format(points.toString)).draw(ij |+| ((x0,y0)))
        <+< tf.create("%5s".format(getPoints.toString)).draw(ij |+| ((x0,y0+1)))
        <+< tf.create("Score").drawFg(ij |+| ((x0, y0 - 1)))
      )
  }
}
