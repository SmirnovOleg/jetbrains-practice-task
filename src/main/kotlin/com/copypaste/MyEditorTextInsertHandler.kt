package com.copypaste

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ex.ClipboardUtil
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.editor.actionSystem.EditorTextInsertHandler
import com.intellij.util.Producer
import java.awt.datatransfer.Transferable
import com.intellij.openapi.editor.actions.BasePasteHandler

/**
 * Class implements logic of handling text pasting events
 */
class MyEditorTextInsertHandler : BasePasteHandler(), EditorTextInsertHandler {
    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        val clipboardText = ClipboardUtil.getTextInClipboard()
        val message = "It seems you want to paste that text:\n\n" +
                clipboardText +
                "\n\nPlease make sure you really want to do this."
        val result = Messages.showYesNoDialog(message, "Warning!", Messages.getWarningIcon())
        // Check if user pressed "Yes" button
        if (result == 0 && clipboardText != null) {
            super.doExecute(editor, caret, dataContext)
        }
    }

    // Implement this method to prevent IDE fatal errors
    override fun execute(editor: Editor?, dataContext: DataContext?, producer: Producer<Transferable>?) {}
}