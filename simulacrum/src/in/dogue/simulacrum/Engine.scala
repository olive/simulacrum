package in.dogue.simulacrum

import in.dogue.antiqua.graphics.{TileRenderer, Renderer}
import com.deweyvm.gleany.{GleanyGame, AssetLoader}
import in.dogue.antiqua.graphics.Tileset
import in.dogue.simulacrum.input.Controls
import in.dogue.simulacrum.modes.{TitleMode, Mode}
import scala.util.Random

class Engine {
  val cols = 65
  val rows = 32
  val r = new Random(0)
  var mode:Mode = TitleMode.create(cols, rows, r).toMode
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
