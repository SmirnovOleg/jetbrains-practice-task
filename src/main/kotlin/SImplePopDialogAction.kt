import com.intellij.lang.ASTNode
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.psi.*
import com.intellij.psi.tree.TokenSet


class ASTMessageAction : AnAction() {
    private var pluginMessage = ""
    private val uselessNodeTypes = hashSetOf("WHITE_SPACE", "ERROR_ELEMENT")

    private fun recursiveWalking(currentNode: ASTNode, depth: Int = 0) {
        val currentNodeType = currentNode.elementType.toString()
        if (!uselessNodeTypes.contains(currentNodeType)) {
            // Add prefix for tree element string
            pluginMessage += '>'
            for (i in 1..depth)
                pluginMessage += "------"
            pluginMessage += "$currentNodeType\n"
        }
        // Recursive visiting other children
        for (child in currentNode.getChildren(TokenSet.ANY))
            recursiveWalking(child, depth + 1)
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        // Preparing environment
        val currentProject = anActionEvent.project
        val editor = anActionEvent.getData(CommonDataKeys.EDITOR)
        val psiFile = anActionEvent.getData(CommonDataKeys.PSI_FILE) ?: return
        // Getting selected code
        val selectedCode = editor?.selectionModel?.selectedText ?: return
        val psiSelectedFile = PsiFileFactory
            .getInstance(currentProject)
            .createFileFromText(psiFile.language, selectedCode)
        // Building AST
        pluginMessage = ""
        recursiveWalking(psiSelectedFile.node)
        // Showing message
        Messages.showMessageDialog(currentProject,
            pluginMessage,
            "AST",
            Messages.getInformationIcon())
    }

}