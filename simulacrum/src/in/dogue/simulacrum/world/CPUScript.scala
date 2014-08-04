package in.dogue.simulacrum.world

import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.audio.SoundManager

object CPUAction {
  val All = Vector(Up, Down, Left, Right, Flip, Swap, Nothing)
  val Moves = Vector(Up, Down, Left, Right)
  val Actions = Vector(Flip, Swap)
}

sealed trait CPUAction {
  def getMove = (0,0)
  def isFlipping = false
  def isSwapping = false
}
case object Up extends CPUAction {
  override def getMove = (0, -1)
}
case object Left extends CPUAction {
  override def getMove = (-1, 0)
}
case object Right extends CPUAction {
  override def getMove = (1, 0)
}
case object Down extends CPUAction {
  override def getMove = (0, 1)
}
case object Flip extends CPUAction {
  override def isFlipping = true
}
case object Swap extends CPUAction {
  override def isSwapping = true
}
case object Nothing extends CPUAction

object CPUScript {
  def create(map:Map[Int, CPUAction]) = {
    CPUScript(map.withDefaultValue(Nothing))
  }

  def createRandom(cols:Int, rows:Int, k:Int, diff:Int, r:Random) = {
    var p = 0
    var cell = (4,4)
    def canMove(pos:Cell) = {
      pos.inRange((0,0,cols, rows))
    }
    val map:Map[Int, CPUAction] = (0 until diff*k).map { i =>
      val action = if (i % k == k/2) {
        p += 1
        val moves = CPUAction.Moves.filter{m => canMove(m.getMove |+| cell)}.toVector
        val act = (p % 2 == 0).select(moves, CPUAction.Actions).randomR(r)
        cell = cell |+| act.getMove
        act
      } else {
        Nothing
      }
      i -> action
    }.toMap
    CPUScript.create(map)
  }
}

case class CPUScript private (map:Map[Int, CPUAction]) {
  def isSwapping(i:Int) = map(i).isSwapping
  def isFlipping(i:Int) = map(i).isFlipping

  def getMove(i:Int):(Int,Int) = map(i).getMove
}
