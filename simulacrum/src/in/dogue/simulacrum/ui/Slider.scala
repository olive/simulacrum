package in.dogue.simulacrum.ui

import in.dogue.antiqua.Antiqua.TileGroup
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.antiqua.graphics.{TileRenderer, Tile}
import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.graphics.Color
import in.dogue.simulacrum.Simulacrum
import in.dogue.simulacrum.input.Controls
import in.dogue.simulacrum.audio.SoundManager

object Slider {
  def createInt(s:String, min:Int, max:Int) = {
    val lArrow = CP437.◄.mkTile(Color.Black, Color.White)
    val rArrow = CP437.►.mkTile(Color.Black, Color.White)
    val tf = Simulacrum.tf
    val title = tf.create(s).toTileGroup
    def render(i:Int) = tf.create("%2s".format(i.toString)).toTileGroup
    Slider[Int](title, lArrow, rArrow, render, min, max, min, _ + 1, _ - 1)
  }
}

case class Slider[T](title:TileGroup, lArrow:Tile, rArrow:Tile, render:T => TileGroup, min:T, max:T, value:T, incr:T => T, decr:T => T) {

  def getValue = value

  def update = {
    val dx = Controls.AxisX.zip(15, 5)
    val slid = dx match {
      case 1 if value != max =>
        SoundManager.blip.play()
        copy(value = incr(value))
      case -1 if value != min =>
        SoundManager.blip.play()
        copy(value = decr(value))
      case _ => this
    }
    slid
  }

  private def drawArrows(ij:Cell)(tr:TileRenderer):TileRenderer = {
    val less = if (value == min) {
      lArrow.mapFg(_.dim(4))
    } else {
      lArrow
    }

    val great = if (value == max) {
      rArrow.mapFg(_.dim(4))
    } else {
      rArrow
    }

    tr <| (ij |- 1, less) <| (ij |+ 4, great)

  }

  private def drawRender(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <|| (render(value) |++| (ij |+ 1))
  }

  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    tr <|| (title |++| ((-8, 0) |+| ij)) <+< drawRender(ij) <+< drawArrows(ij)
  }
}
