# Text editor
Made by Anesu Matanga (20010613) and James Thompson (21011195)


## About text editor
This is a normal text editor that operates as you expect.

It has all the normal funcitonality that was expected from the assignment.

A list of some of the bonus features:

 - Reading both odt and rtf files
 - Saving to PDF files
 - Save warning when you try to overwrite current work of exit app without saving.
 - Current file indicator as well as it saved status denoted by * for unsaved
 - Syntax highlighting, code folding, auto indentation and 

The repositry also has CI setup to run the unit tests.

A clone of this repo and building it should let it run smoothly. The only external file it needs is the config.txt file however this is by default in the rerpo.

## Division of labour
In general James has been responsible for FileManager and EditorManager. While Anesu has been responsible for ConfigManager, SearchBoxManager as well as the GUI creation.

Here are some commit IDs to try demonstrate this however as progress in these areas were made over time single commits arent represntive of full work done.
Anesu
 - ConfigManager - 38f3c64e984a7cd061ca21bfbbcf61140eab5cc4
 - SearchBoxManager - 5f66b2cc4d212c01309524bf8c34fafd841bb08e
 - MainGuiClass - 53da76e459b72e454d5bc73acb2b10d85a72cc00
James
 - FileManager - 9245254faeb38bd0bdff46345d943ee552d3700c
 - EditorManager - cee8bfa017947892a1f1781bfa277fa8499bf588

## Commit messages types
The commits in the repo should somewhat follow this format to help make the log readable
➕ `:heavy_plus_sign:` when adding a file or implementing a feature<br>
🔨 `:hammer:` when fixing a bug or issue<br>
📜 `:scroll:` when updating docs, readme or comments<br>
✅ `:white_check_mark:` Significant milestones<br>
👕 `:shirt:` when refactoring or removing linter warnings<br>
❌ `:x:` when removing code or files<br>
