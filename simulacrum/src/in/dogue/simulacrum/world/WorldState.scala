package in.dogue.simulacrum.world

import in.dogue.simulacrum.audio.SoundManager

sealed trait WorldState {
  def incr:WorldState
}
case class CPUTurn(t:Int) extends WorldState {
  def incr = copy(t=t+1)
}
case class PlayerTurn(t:Int) extends WorldState {
  def incr = copy(t=t+1)
}
case object CheckBoards extends WorldState {
  def incr = this
}


case class WorldStateTransitioner(speed:Int) {
  def update = this
  def process(ws:WorldState):WorldState = {
    ws match {
      case CPUTurn(t) if t >= speed =>
        SoundManager.ping.play()
        PlayerTurn(0)
      case PlayerTurn(t) if t >= speed =>
        CheckBoards
      case CheckBoards =>
        SoundManager.pong.play()
        CPUTurn(0)
      case s => s.incr
    }
  }
}
