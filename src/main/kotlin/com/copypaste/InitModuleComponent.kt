package com.copypaste

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.module.ModuleComponent

/**
 * Module component is used for setting ActionHandler when project has opened
 */
class InitModuleComponent: ModuleComponent {
    override fun projectOpened() {
        val actionManager = EditorActionManager.getInstance()
        // Start handling paste events
        actionManager.setActionHandler(IdeActions.ACTION_EDITOR_PASTE, MyEditorTextInsertHandler())
    }
}