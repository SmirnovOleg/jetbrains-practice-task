package com.copypaste

import com.intellij.lang.ASTNode
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.TokenSet

/**
 * Class implements logic of showing messages with built abstract syntax tree of selected code
 */
class ASTInfoMessageAction : AnAction() {
    private var message = ""
    private val uselessNodeTypes = hashSetOf("WHITE_SPACE", "ERROR_ELEMENT")

    /**
     * Depth-first search for traversing tree of ASTNodes
     *
     * @param depth is used for pretty printing nested tree elements
     */
    private fun recursiveASTWalking(currentNode: ASTNode, depth: Int = 0) {
        val currentNodeType = currentNode.elementType.toString()
        // Add prefix for tree element string if necessary
        if (!uselessNodeTypes.contains(currentNodeType)) {
            message += '|'
            for (i in 1..depth)
                message += "--------"
            message += ">$currentNodeType\n"
        }
        // Recursively visit other children
        for (child in currentNode.getChildren(TokenSet.ANY))
            recursiveASTWalking(child, depth + 1)
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        // Prepare environment
        val currentProject = anActionEvent.project
        val editor = anActionEvent.getData(CommonDataKeys.EDITOR)
        val psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE) ?: return
        // Extract selected code
        val selectedCode = editor?.selectionModel?.selectedText ?: return
        val psiSelectedFile = PsiFileFactory
            .getInstance(currentProject)
            .createFileFromText(psiFile.language, selectedCode)
        // Build AST
        message = ""
        recursiveASTWalking(psiSelectedFile.node)
        // Show message
        Messages.showMessageDialog(currentProject,
            message,
            "Abstract syntax tree",
            Messages.getInformationIcon())
    }

}