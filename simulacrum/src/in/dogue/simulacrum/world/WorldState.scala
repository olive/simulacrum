package in.dogue.simulacrum.world

import in.dogue.simulacrum.audio.SoundManager
import in.dogue.antiqua.Antiqua
import Antiqua._

sealed trait WorldState {
  def incr:WorldState
}
case class CPUTurn(t:Int, delay:Int) extends WorldState {
  def incr = copy(t=t+1)
}
case class PlayerTurn(t:Int) extends WorldState {
  def incr = copy(t=t+1)
}
case class CheckBoards(delay:Int) extends WorldState {
  def incr = this
}


case class WorldStateTransitioner(speed:Int, delay:Int) {
  def update = this
  def process(ws:WorldState):(WorldState,Int) = {
    ws match {
      case CPUTurn(t, d) if t >= speed && d == delay =>
        SoundManager.ping.play()
        PlayerTurn(0) @@ 0
      case CPUTurn(t, d) if t >= speed && d < delay =>
        SoundManager.pong.play()
        CheckBoards(d+1) @@ 0
      case PlayerTurn(t) if t >= speed =>
        CheckBoards(delay) @@ ((delay+1)*10)
      case CheckBoards(d) =>
        SoundManager.pong.play()
        CPUTurn(0, d) @@ 0

      case s => s.incr @@ 0
    }
  }
}
