## Anti-copy-paster
*Test task for JetBrains practice*

### Description
* IntelliJ IDEA plugin which can show messages about an abstract syntax tree of selected code and warn if user wants to copy-paste.
* When user runs plugin using a keyboard combination or toolbar button, `ASTInfoMessageAction.actionPerformed` is invoked. That method gets selected code from an editor, creates PSI file and runs `recursiveASTWalking`, which traverses AST using DFS and constructs record for info message. 
* `InitModuleComponent.projectOpened` is used such as an entry point. It setups `MyEditorTextInsertHandler` for handling editor-paste events. When it occurs, `doExecute` is called, and if clipboard isn't empty it shows warning.

### TODO
* Add unit tests
* Think about using MVC pattern
* Add smart analysis of pasting code (is it actually user's code, should it be moved to separate method)
* Upgrade UI

### Troubleshooting/Problems
* Due to intellij-sdk docs `Services` should be used instead of `Component`, and `projectOpened` method is deprecated. But I don't actually understand where should I setup event handlers for editor, because [here](http://www.jetbrains.org/intellij/sdk/docs/tutorials/editor_basics/editor_events.html) it is declared in class, which generally has no semantic connection to it.
* `MyEditorTextInsertHandler` should implement (in order to avoid IDE fatal errors) `EditorTextInsertHandler` interface, which uses deprecated in Java `Producer<Transferable>`.
* Built-in `ASTNode`'s have all methods to iterate it in tree traversal order, but in fact body-nodes of if-conditions/loops/etc aren't a children of its command-node(am I wrong?). I've hardcoded nesting of blocks with curly braces, but it should be fixed in future.
