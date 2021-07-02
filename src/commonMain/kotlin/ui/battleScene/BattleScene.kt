package ui.battleScene

import WINDOW_SIZE
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.scene.AlphaTransition
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korio.async.launchImmediately
import ui.Resources
import ui.play
import ui.tiledScene.Direction
import ui.tiledScene.PlayerCharacter
import ui.tiledScene.TiledScene

class BattleScene(private val config: BattleConfig) : Scene() {

    override suspend fun Container.sceneInit() {
        val background = Resources.getImage("battleBackgrounds/${config.battle.terrain.battleName}.png")
        play(coroutineContext, "music/battle/${config.musicName}.mp3")

        val playerCombatant = Combatant(config.battle.botA, Direction.RIGHT)
        val enemyCombatant = Combatant(config.battle.botA, Direction.LEFT)
        val battleControls = BattleControls(config.battle, config.battle.botA)

        fixedSizeContainer(WINDOW_SIZE, WINDOW_SIZE, clip = false) {
            scaleView(WINDOW_SIZE, WINDOW_SIZE, 2.0, false) {
                Image(background, 0.0, 0.0, smoothing = false).addTo(this)
                addChild(playerCombatant)
                playerCombatant.init()

                addChild(enemyCombatant)
                enemyCombatant.init()

                addChild(battleControls)
                battleControls.init()


                keys {
                    up(Key.Z) {
                        endBattle()
                    }
                }
            }
        }
    }

    private fun endBattle() {
        launchImmediately {
            sceneContainer.changeTo<TiledScene>(
                config.level,
                PlayerCharacter(config.battle.botA),
                config.tile.x,
                config.tile.y,
                transition = AlphaTransition,
                time = TimeSpan(500.0)
            )
        }
    }
}