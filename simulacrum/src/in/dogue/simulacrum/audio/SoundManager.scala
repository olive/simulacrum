package in.dogue.simulacrum.audio

import com.deweyvm.gleany.AssetLoader

object SoundManager {
  val ping =  load("ping", 1.0)
  val pong =  load("pong", 1.0)
  val `0` =   load("0", 1.0)
  val `321` = load("321", 1.0)
  val oof = load("oof", 1.0)
  val flip = load("flip", 1.0)
  val beeple = load("beeple", 1.0)
  def load(s:String, adj:Double) = {
    val sound = AssetLoader.loadSound(s)
    sound.setAdjustVolume(adj.toFloat)
    sound
  }
}
