package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Border, Rect, TileRenderer}
import in.dogue.simulacrum.world._
import com.deweyvm.gleany.graphics.Color
import scala.util.Random
import in.dogue.antiqua.data.CP437
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.Simulacrum
import in.dogue.simulacrum.audio.SoundManager

object GameMode {
  def create(cols:Int, rows:Int, delay:Int, r:Random) = {
    def mkTile(r:Random) = {
      val fg = Color.Blue.dim(3 + r.nextDouble)
      val bg = Color.Blue.dim(6 + r.nextDouble)
      val code = Vector(CP437.|, CP437./, CP437.\, CP437.─)
      code.randomR(r).mkTile(bg, fg)
    }
    val div = (0 until rows).map { i =>
      val bg = Color.Blue.dim(6 + r.nextDouble)
      val fg = Color.Blue.dim(1 + r.nextDouble)
      ((cols/2, i), CP437.|.mkTile(bg, fg))}
    val rect = Rect.createTextured(cols, rows, mkTile, r)
    val bCols = 8
    val bRows = 8
    val border = Simulacrum.border(bCols + 2, bRows + 2)
    val left = Board.create(bCols, bRows, Controller.player(bCols, bRows))
    val right = Board.create(bCols, bRows, Controller.makeRandom(bCols, bRows, 60, 25, r))
    val wst = new WorldStateTransitioner(60)
    GameMode(cols, rows, delay, rect, left, right, border, div, CheckBoards, wst, r)
  }
}

case class GameMode private (cols:Int, rows:Int, delay:Int, rect:Rect, left:Board, right:Board, border:Border, div:TileGroup, state:WorldState, wst:WorldStateTransitioner, r:Random) {
  def update = {
    val (failed, newSelf) = state match {
      case CPUTurn(_) =>
        false @@ copy(right=right.update)
      case PlayerTurn(_) =>
        false @@ copy(left=left.update)
      case CheckBoards =>
        checkBoards @@ this
    }
    if (failed) {
      SoundManager.lose.play()
      FailMode.create(cols, rows, this, r).toMode
    } else {
      val newState = wst.process(state)
      newSelf.copy(state=newState, wst=wst.update).toMode
    }
  }


  private def checkBoards:Boolean = {
    val test = for (i <- 0 until left.cols; j <- 0 until left.rows) yield {
      left.get((i, j)) == right.get((i, j))
    }
    !test.forall(id[Boolean])
  }

  def drawBorder(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <+< (state match {
      case PlayerTurn(_) => border.draw(ij |-| ((1, 1)))
      case CPUTurn(_) => border.draw(ij |-| ((1, 1)) |+| ((cols/2 + 1, 0)))
      case CheckBoards => id[TileRenderer] _
    })
  }

  def draw(tr:TileRenderer):TileRenderer = {
    val ij = (4, 4)
    (tr <+< rect.draw((0,0))
        <++ div
        <+< left.draw((0,0) |+| ij)
        <+< right.draw((cols/2 + 1, 0) |+| ij)
        <+< drawBorder(ij)
      )
  }

  def toMode:Mode = Mode[GameMode](_.update, _.draw, this)
}
