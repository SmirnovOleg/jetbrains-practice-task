package com.copypaste

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ex.ClipboardUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.editor.actionSystem.EditorTextInsertHandler
import com.intellij.util.Producer
import java.awt.datatransfer.Transferable

/**
 * Class implements logic of handling text pasting events
 */
class MyEditorTextInsertHandler : EditorActionHandler(), EditorTextInsertHandler {
    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        val clipboardText = ClipboardUtil.getTextInClipboard()
        val message = "It seems you want to paste that text:\n\n" +
                clipboardText +
                "\n\nPlease make sure you really want to do this."
        val result = Messages.showYesNoDialog(message, "Warning!", Messages.getWarningIcon())
        // Check if user pressed "Yes" button
        if (result == 0 && clipboardText != null) {
            val offset = editor.caretModel.offset
            // Construct the runnable to paste text from clipboard at current offset
            fun runnable() = Runnable { editor.document.insertString(offset, clipboardText) }
            // Make the document change in the context of a write action
            WriteCommandAction.runWriteCommandAction(editor.project, runnable())
            // Move caret to correct offset
            editor.caretModel.moveToOffset(offset + clipboardText.length)
        }
    }

    override fun execute(editor: Editor?, dataContext: DataContext?, producer: Producer<Transferable>?) {
        editor?.let { super.execute(it, dataContext) }
    }
}