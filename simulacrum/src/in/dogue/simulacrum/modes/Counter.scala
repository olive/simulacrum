package in.dogue.simulacrum.modes

import in.dogue.antiqua.Antiqua.{Cell, TileGroup}
import in.dogue.antiqua.graphics.{TileRenderer, Tile}
import in.dogue.antiqua.data.CP437
import com.deweyvm.gleany.graphics.Color
import in.dogue.antiqua.Antiqua
import Antiqua._
import in.dogue.simulacrum.audio.SoundManager

object Counter {
  def create = {
    val width = 7
    def gf(s:String) = {
      val group = Tile.groupFromFile(s, "tiles", CP437.intToCode, _.mkTile(Color.Black, Color.White))
      val span = group.getSpan
      group |++| (((width - span.width)/2, 0))

    }
    val `3` = gf("3")
    val `2` = gf("2")
    val `1` = gf("1")
    val go = gf("go")
    val maxT = 240
    Counter(maxT, maxT, 0, Vector(`3`, `2`, `1`, go))
  }
}

case class Counter(t:Int, maxT:Int, prevK:Int, tgs:Vector[TileGroup]) {
  def update = {
    val pK = getK
    val result = copy(t=t.drop1)
    if (result.getK != pK || t == maxT - 1) {
      if (result.getK == 3) {
        SoundManager.`0`.play()
      } else {
        SoundManager.`321`.play()
      }
    }
    result
  }

  private def getK = math.min(math.max((maxT - t) / 60, 0), 3)

  def isDone = t <= 0

  def draw(ij:Cell)(tr:TileRenderer):TileRenderer = {
    if (t == maxT) {
      tr
    } else {
      tr <++ (tgs(getK) |++| ij)
    }

  }
}
