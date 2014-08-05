package in.dogue.simulacrum

import in.dogue.antiqua.graphics.{TileRenderer, Renderer}
import com.deweyvm.gleany.{GleanyGame, AssetLoader}
import in.dogue.antiqua.graphics.Tileset
import in.dogue.simulacrum.input.Controls
import in.dogue.simulacrum.modes.{ResultMode, GameMode, TitleMode, Mode}
import scala.util.Random
import in.dogue.simulacrum.saving.Hiscore

class Engine {
  val cols = 33
  val rows = 16
  val r = new Random()
  var mode:Mode = {
    TitleMode.create(cols, rows, 0, r).toMode
    //val gm = GameMode.create(cols, rows, 1, r)
    //FailMode.create(cols, rows, gm, r).toMode
  }
  val ts = new Tileset(16, 16, 16, 16, AssetLoader.loadTexture("16x16"))
  val renderer = new Renderer(cols*16, rows*16, 1, ts)

  def update() = {
    if (Controls.Escape.justPressed) {
      GleanyGame.exit()
    }
    mode = mode.update

  }

  def draw() = {
    val tr = TileRenderer.create(cols, rows)
    renderer.render(tr <+< mode.draw)
    ()
  }

}
