package in.dogue.simulacrum.world

import in.dogue.simulacrum.input.Controls
import scala.annotation.tailrec
import scala.util.Random
import in.dogue.simulacrum.audio.SoundManager

object Controller {
  def dummy(cols_ :Int, rows_ :Int) = new Controller {
    self =>
    override val cols = cols_
    override val rows = rows_
    override def move: (Int, Int) = (0,0)

    override def swapping: Boolean = false

    override def update: Controller = self

    override def flipping: Boolean = false
  }

  def makeRandom(cols:Int, rows:Int, speed:Int, diff:Int, r:Random) = {
    cpu(cols, rows, 0, CPUScript.createRandom(cols, rows, speed, diff, r))
  }

  def cpu(cols_ :Int, rows_ :Int, i:Int, script:CPUScript):Controller = new Controller {
    self =>
    override val cols = cols_
    override val rows = rows_
    override def move: (Int, Int) = script.getMove(i)

    override def swapping: Boolean = script.isSwapping(i)

    override def update: Controller = {
      cpu(cols_, rows_, i+1, script)
    }

    override def flipping: Boolean = script.isFlipping(i)
  }

  def player(cols_ :Int, rows_ :Int) = new Controller {
    override val cols = cols_
    override val rows = rows_
    override def move: (Int, Int) = {
      val dx = Controls.AxisX.justPressed
      val dy = Controls.AxisY.justPressed
      (dx, dy)
    }

    override def swapping: Boolean = Controls.Space.justPressed

    override def update: Controller = this

    override def flipping: Boolean = Controls.Shift.justPressed
  }
}

trait Controller {
  val cols:Int
  val rows:Int
  protected def move:(Int,Int)
  protected def flipping:Boolean
  protected def swapping:Boolean
  def getMove = {
    val m = move
    if (m != ((0,0))) {
      SoundManager.oof.play()
    }
    m
  }

  def isSwapping = {
    val result = swapping
    if (result) {
      SoundManager.beeple.play()
    }
    result
  }

  def isFlipping = {
    val result = flipping
    if (result) {
      SoundManager.flip.play()
    }
    result
  }
  def update:Controller
}

