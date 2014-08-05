package in.dogue.simulacrum.modes

import in.dogue.antiqua.graphics.{Border, Rect, TileRenderer}
import in.dogue.simulacrum.world._
import com.deweyvm.gleany.graphics.Color
import scala.util.Random
import in.dogue.antiqua.data.{Array2d, CP437}
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.Simulacrum
import in.dogue.simulacrum.audio.SoundManager
import in.dogue.simulacrum.saving.Hiscore

object GameMode {
  def create(cols:Int, rows:Int, delay:Int, r:Random) = {
    def mkTile(r:Random) = {
      val fg = Color.Blue.dim(3 + r.nextDouble)
      val bg = Color.Blue.dim(6 + r.nextDouble)
      val code = Vector(CP437.|, CP437./, CP437.\, CP437.─)
      code.randomR(r).mkTile(bg, fg)
    }
    def mkBorder(r:Random) = {
      val bg = Color.Blue.dim(6 + r.nextDouble)
      val fg = Color.Blue.dim(1 + r.nextDouble)
      CP437.│.mkTile(bg, fg)
    }
    val pb = PointBox.create(mkTile,mkBorder,r)
    val div = (0 until rows).map { i => ((cols/2, i), mkBorder(r))}
    val rect = Rect.createTextured(cols, rows, mkTile, r)
    val bCols = 8
    val bRows = 8
    val border = Simulacrum.border(bCols + 2, bRows + 2)
    val left = Board.create(bCols, bRows, Controller.player(bCols, bRows))
    val right = Board.create(bCols, bRows, Controller.makeRandom(bCols, bRows, 60, 25, r))
    val wst = new WorldStateTransitioner(60, delay)
    GameMode(cols, rows, delay, rect, left, right, border, div, CheckBoards(0), wst, Vector(), pb, r)
  }
}

case class GameMode private (cols:Int, rows:Int, delay:Int, rect:Rect, left:Board, right:Board, border:Border, div:TileGroup, state:WorldState, wst:WorldStateTransitioner, pastBoards:Vector[Array2d[BoardTile]], pb:PointBox, r:Random) {
  def update = {
    val (failed, newSelf) = state match {
      case CPUTurn(_,_) =>
        false @@ copy(right=right.update)
      case PlayerTurn(_) =>
        false @@ copy(left=left.update)
      case CheckBoards(d) =>
        val fail = if (d == delay) {
          checkBoards(d)
        } else {
          false
        }
        fail @@ save
    }
    if (failed) {
      SoundManager.lose.play()
      val hiscore = Hiscore.get
      if (pb.score > hiscore) {
        Hiscore.put(pb.score)
      }
      ResultMode.create(cols, rows, this, pb.isHiscore, r).toMode
    } else {
      val (newState, points) = wst.process(state)
      newSelf.copy(state=newState, wst=wst.update, pb=pb.give(points)).toMode
    }
  }

  private def save = {
    val b = right.tiles
    val boards = b +: pastBoards
    val cut = if (boards.length > delay) {
      boards.take(delay)
    } else {
      boards
    }
    copy(pastBoards = cut)
  }


  private def checkBoards(d:Int):Boolean = {
    val board = if (d == 0) {
      right.tiles
    } else {
      pastBoards(d-1)
    }
    val test = for (i <- 0 until left.cols; j <- 0 until left.rows) yield {
      left.get((i, j)) == board.get((i, j))
    }
    !test.forall(id[Boolean])
  }

  def drawBorder(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <+< (state match {
      case PlayerTurn(_) => border.draw(ij |-| ((1, 1)))
      case CPUTurn(_,_) => border.draw(ij |-| ((1, 1)) |+| ((cols/2 + 1, 0)))
      case CheckBoards(_) => id[TileRenderer] _
    })
  }

  def draw(tr:TileRenderer):TileRenderer = {
    val ij = (4, 4)
    (tr <+< rect.draw((0,0))
        <++ div
        <+< left.draw((0,0) |+| ij)
        <+< right.draw((cols/2 + 1, 0) |+| ij)
        <+< drawBorder(ij)
        <+< pb.draw((cols/2 - 3, rows-10))
      )
  }

  def toMode:Mode = Mode[GameMode](_.update, _.draw, this)
}
