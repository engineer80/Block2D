The first release of Block2D game

Block2D consolidate set of 2D tetris-like games (Tetris/Pentix etc).
Added (optional) the abilities to flip shapes (vertical and horizontal).
GUI represent settings for visualization and gameplay.
The same settings can be made in a text configuration file ( conf.txt ).
It is possible to create a set of custom shapes in the text file ( shapes.txt ).

Written in 100% Java SE.
Compiled with java 8 (jdk 1.8.0_20 64-bit edition) on Windows 7 64-bit.
Successful tested on Windows 7 64-bit and Ubuntu Linux 12.04 64-bit with java 8 and java 7 respectively.

Simple tutorial zetacode project ( http://zetcode.com/tutorials/javagamestutorial/tetris/ ) served as basis for this game.
Pure Awt/Swing are used for creating GUI.

*   *   *

Start game

To find the Java Virtual Machine (jvm) with support java version 5 or higher. 
For example, download & install Java Runtime Environment (jre) from Oracle official site oracle.com

Then there are 2 alternatives:

1. Dowload (or download and extract) game byte-code from this repository files/bin (from arch/bin.zip). 
Execute the console command in downloaded (and extracted) folder Block2D:
 java -classpath ./bin block2D.Block2D conf.txt
Or execute ready-made bash/batch file run.sh/run.bat for Linux/Windows respectively.

2. Download (or download and extract) game jar-file from this repository files/jar (from arch/jar.zip).
Execute the console command in downloaded floder Block2D:
 java -jar block2D.jar
Or execute ready-made bash/batch file runjar.sh/runjar.bat for Linux/Windows respectively.

*   *   *

Build game

To find the Java Compiler and java se library with support java version 5 or higher. 
For example, download & install Java Development Kit (jdk) from Oracle official site oracle.com

Download (or download and extract) game source files from this repository files/src (from arch/src.zip).

Then there are 2 alternatives:

1. Create the game byte-code from source code. 
Execute the console command in downloaded floder Block2D:
 javac -sourcepath ./src -d ./bin ./src/block2D/Block2D.java
Or execute ready-made bash/batch file cmp.sh/cmp.bat for Linux/Windows respectively.

2. Create the game jar-file from source code.
Execute the console command in downloaded floder Block2D (with end dot!):
 jar -cmef .\src\manifest.mf block2D.Block2D block2D.jar -C .\bin .
Or execute ready-made bash/batch file crtjar.sh/crtjar.bat for Linux/Windows respectively.

Check the results using the previous item "Start game".

*   *   *

Gameplay

After game starting from the main menu will be offered:

 - press 'Enter' to start
 - press 'S' to config

If pressed 'Enter', the game  begins. Hot key list:

 'ENTER' = START 
 'ESCAPE' = STOP
 'P' = PAUSE
 'LEFT' = MOVE LEFT
 'RIGHT' = MOVE RIGHT
 'DOWN' = ROTATE RIGHT  
 'UP' = ROTATE LEFT  		
 'Z' = FLIP VERTICAL
 'X' = FLIP HORIZONTAL
 'SPACE' = DROP DOWN
 'D' = ONE LINE DOWN
 'S' = CALL
 
If pressed 'S', settings window appears:

 "Cols" = Column of board
 "Rows" = Rows of board
 "Init delay" = The initial delay (millisecond) before the appearance of the next shape
 "Level delay" = Delay difference (millisecond) between the game levels
 "Lines" = The number of full lines for next level
 "Allow flip" = Allow to vertical and horizontal flip shapes (additional to the classic tetris operations)
 "Gen flipped" = Autogeneration flipped shapes when a new set of shapes is created
 "Show next" = Show next shape
 "Shape distribution" = Probability distribution of the shape groups. 
Each group is associated with the probability weight (%).
Within a group, the probability distribution is uniform
 "Save" = Save settings to 'conf.txt' file and save shapes to 'shapes.txt' file
 "Load" = Load settings from 'conf.txt' file and load shapes from 'shapes.txt' file
 "Default" = Autogeneration default settings and set of shapes
 "Ok" = Start game with current settings

The same settings can be made in a text configuration file 'conf.txt'.
It is possible to create a set of custom shapes in the text file 'shapes.txt'.
At the start of the game these files 'conf.txt' and 'shapes.txt' are searched if they are not, 
default settings and set of shapes are created.


Custom shapes file 'shapes.txt' represented as follows:

...

"L_3",3,{255,128,0},{{0,0},{0,1},{1,0}}
"O_4",4,{255,255,0},{{0,0},{0,1},{1,0},{1,1}}
...

where mask line is:
"name", groupID, {red, green, blue}, {{X1,Y1}, ..., {Xn,Yn}}

"name" = String name of shape.
"groupID" = Integer id of shape group for specifying probability.
"{red, green, blue}" = Shape color.
"{{X1,Y1}, ..., {Xn,Yn}}" =  Shape coordinates are defined in the relative coordinate system, 
which will then be automatically centered.