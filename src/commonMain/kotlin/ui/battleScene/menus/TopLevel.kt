package ui.battleScene.menus

import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.view.Image
import com.soywiz.korge.view.addTo
import com.soywiz.korio.async.launchImmediately
import ui.battleScene.BattleControls
import ui.battleScene.BattleOption
import ui.battleScene.BattleScene
import ui.battleScene.Combatant
import ui.tiledScene.Direction

class TopLevel(
    private val parent: BattleScene,
    private val background: Image,
    private val playerCombatant: Combatant,
    private val enemyCombatant: Combatant,

    ) {
    private var battleControls = getControls()
    private val inspectMenu by lazy { InspectWho(parent, background, playerCombatant, enemyCombatant, this) }

    suspend fun reDraw() {
        parent.screen.keys {
            up(Key.SPACE) {
                battleControls.selectedAction?.action?.invoke()
            }
        }

        parent.screen.removeChildren()
        background.addTo(parent.screen)
        parent.screen.addChild(playerCombatant)
        playerCombatant.init()

        parent.screen.addChild(enemyCombatant)
        enemyCombatant.init()

        parent.screen.addChild(battleControls)
        battleControls.init()
    }

    private fun reDrawOptions(highlighted: Direction? = null) {
        parent.screen.removeChild(battleControls)
        battleControls = getControls(highlighted)
        parent.screen.addChild(battleControls)
        battleControls.init()
    }

    private fun getControls(highlighted: Direction? = null): BattleControls {
        val up = BattleOption("Inspect") { launchImmediately(parent.context) { inspectMenu.reDraw() } }
        val right = BattleOption("Action")
        val left = BattleOption("Flee") { parent.endBattle() }
        val down = BattleOption("Done")
        return BattleControls(up, down, left, right, ::reDrawOptions, highlighted)
    }

}