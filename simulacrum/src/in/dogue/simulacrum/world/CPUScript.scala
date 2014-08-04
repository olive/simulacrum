package in.dogue.simulacrum.world

import scala.util.Random
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.audio.SoundManager

object CPUAction {
  val Moves = Vector(Up, Down, Left, Right, Flip)
  val Actions = Vector(Add, Sub)
}

sealed trait CPUAction {
  def getMove = (0,0)
  def isFlipping = false
  def isAdding = false
  def isSubbing = false
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
case object Add extends CPUAction {
  override def isAdding = true
}
case object Sub extends CPUAction {
  override def isSubbing = true
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
      val action = if (i % k == k / 4) {
        val moves = CPUAction.Moves.filter{m => canMove(m.getMove |+| cell)}.toVector
        val m = moves.randomR(r)
        cell = cell |+| m.getMove
        m
      } else if (i % k == 2*k/4) {
        val moves = CPUAction.Moves.filter{m => canMove(m.getMove |+| cell)}.toVector
        moves.randomR(r)
        val m = moves.randomR(r)
        cell = cell |+| m.getMove
        m
      } else if (i % k == 3*k/4) {
        CPUAction.Actions.randomR(r)
      } else {
        Nothing
      }
       /* if (i % k == k/2) {
        p += 1

        val act = (p % 2 == 0).select(moves, CPUAction.Actions).randomR(r)
        cell = cell |+| act.getMove
        act
      } else {
        Nothing
      }*/
      i -> action
    }.toMap
    CPUScript.create(map)
  }
}

case class CPUScript private (map:Map[Int, CPUAction]) {
  def isAdding(i:Int) = map(i).isAdding
  def isSubbing(i:Int) = map(i).isSubbing
  def isFlipping(i:Int) = map(i).isFlipping

  def getMove(i:Int):(Int,Int) = map(i).getMove
}
