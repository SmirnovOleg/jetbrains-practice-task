package idea.plugin.copypaste

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
    private val insignificantNodeTypes = hashSetOf("WHITE_SPACE", "ERROR_ELEMENT")

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
            Messages.getInformationIcon()
        )
    }

    /**
     * Depth-first search for traversing tree of ASTNodes
     *
     * @param current stores current node in AST
     * @param depth is used for correct printing nested AST nodes
     */
    private fun recursiveASTWalking(current: ASTNode, depth: Int = 0) {
        val currentNodeType = current.elementType.toString()
        // Add pretty prefix if necessary
        if (currentNodeType !in insignificantNodeTypes) {
            message += '|' + "--------".repeat(depth) + ">$currentNodeType\n"
        }
        // Recursively visit children
        var additionalDepth = 1
        for (child in current.getChildren(TokenSet.ANY)) {
            // Additional nesting for blocks within curly braces
            val childNodeType = child.elementType.toString()
            if (childNodeType == "LBRACE")
                additionalDepth++
            recursiveASTWalking(child, depth + additionalDepth)
            if (childNodeType == "RBRACE")
                additionalDepth--
        }
    }
}