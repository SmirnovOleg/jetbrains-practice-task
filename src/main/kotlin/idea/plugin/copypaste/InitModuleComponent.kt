package idea.plugin.copypaste

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.module.ModuleComponent

/**
 * Module component is used for setting ActionHandler when project has opened
 */
class InitModuleComponent: ModuleComponent {
    override fun projectOpened() {
        val actionManager = EditorActionManager.getInstance()
        actionManager.setActionHandler(IdeActions.ACTION_EDITOR_PASTE, MyEditorTextInsertHandler())
    }
}