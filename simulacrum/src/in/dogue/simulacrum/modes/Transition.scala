package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.TileRenderer
import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._

object Transition {
  def create(cols:Int, rows:Int, from:Mode, to:Mode, r:Random) = {
    val left = Curtain.create(true, cols, rows, r)
    val right = Curtain.create(false, cols, rows, r)
    val cover = DimCover(cols, rows)
    Transition(cols, rows, from, to, 0, cols*2, left, right, cover)

  }
}

case class Transition(cols:Int, rows:Int, from:Mode, to:Mode, t:Int, max:Int, left:Curtain, right:Curtain, cover:DimCover) {
  def update = {
    if (t >= max) {
      to
    } else {
      copy(t=t+1).toMode
    }
  }

  def draw(tr:TileRenderer):TileRenderer = {
    val leftPos = if (t <= max/2) {
      math.min(t/2 - left.width, -cols/2) @@ 0
    } else {
      val tt = t - max/2
      val x0 = -cols/2
      (x0 - tt/2) @@ 0
    }
    val rightPos = if (t <= max/2) {
      math.max(cols/2, cols - t/2) @@ 0
    } else {
      val tt = t - max/2
      (cols/2 + tt/2) @@ 0
    }
    println(t + " " + leftPos)
    val toDraw = if (t <= max/2) {
      from
    } else {
      to
    }
    val dim = if (t <= max/2) {
      t / (max/2.0)
    } else {
      1 - (t - max/2)/(max/2.0)
    }
    tr <+< toDraw.draw <+< cover.draw(dim) <+< left.draw(leftPos) <+< right.draw(rightPos)
  }

  def toMode:Mode = Mode[Transition](_.update, _.draw, this)
}
