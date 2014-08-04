package in.dogue.simulacrum.world

sealed trait Orientation {
  def flip:Orientation
}
case object Vertical extends Orientation {
  override val flip = Horizontal
}
case object Horizontal extends Orientation {
  override val flip = Vertical
}
