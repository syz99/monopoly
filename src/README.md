# monopoly

Luis Fornes (laf30), Edward Hsieh (eh158), Matt Rose (mjr68), Connie Wu (cw342), Stephanie Zhang (slz6), Sam Zhang (syz5)

### Project Structure
```md
- backend contains all relevant pieces of the game (Card, Player, Dice)
- configuration contains all classes pertaining to input reading and game configuration
- controller contains all classes related to controllers of the game/views/model etc.
- engine contains the single driver class MonopolyDriver to run the game
- frontend contains all relevant View classes 
- testing contains testing files of the program
```

### Resources Used
```md
- office hours
- 307 course materials (examples, lectures, readings)
- stackoverflow
- junit, testfx, open-sourced images
```

### Starting the Application (code-side)
```md
On start of the javafx Application subclass engine.MonopolyDriver, the AbstractScreen subclass
frontend.screens.MainMenuScreen initializes a Scene and renders all the frontend components of the
"Main Menu" of the application onto this Scene which is then set on the main Stage object of the
javafx application. 

On click of the "PLAY: Normal Mode" button, the Scene is altered to contain a frontend.views.FormView
object where users enter information relevant to the game. On click of the start game button, that user
inputs are read in by the FormView and passed along to the controller.GameSetUpController to be read in
to initialize players/icons/playertype to a backend.board.StandardBoard object. GameSetUpController also reads in the 
related xml configuration data files for the standard game to pass along to the constructor of the 
StandardBoard object made. At this stage, an AbstractGameView subclass frontend.views.SplitScreenView
object is made that contains a frontend.views.SquareBoardView on the left half (representing the View of
a Monopoly game board) of the Screen and a frontend.views.BPaneOptionsView object on the right (representing
the View of the player controls).

Once these components are rendered, it is essentially game start. GameSetUpController creates a new 
controller.GameController that takes on the responsibility of controlling the actual game. From now on,
all players interact with buttons/controls located in the BPaneOptionsView and GameController "talks"
to the backend Model components to decide next how to proceed in the game and how to update future Views
to reflect any changes. The game contrinues in this loop until game end.
```
### Starting the Application (client-side)
```md
Once the application is running, simply click the button for PLAY to start a game.
Enter all necessary information based on the rules of the type of game started.
Play the game and find instructions for the game on the main menu.
```

### Assumptions
```md
- 
```

### Extra Features
```md
- Auctions/Trading
- Multiple Games at Once 
cheat keys
-
```

### Bugs / Known Issues
```md
- Current player not bolding on update of a player anymore
- InstructionsScreen not displaying after screen refactor
- Funds adding cheat key removed after not working
```

### Impressions
```md
Sam: 
This project has shown me a lot of how much can go into a game logistically
and technically. Logistically, the process of 'agile development' has
certainly been a benefit. Having sprints and demos each end-of-sprint
provided incentive to continue to meet development goals of the project on a
more consistent basis than if we had not used this agile mindset. Also, with 
such an involved project, I've learned how important communication and planning 
are for a successful project. While I think our team met and planned
extensively pre-coding, in retrospect even we could have improved how our 
planning went.

Technically, I've learned more about the importance of clean design. Our
project mostly employed an MVC structure. We deviated away from MVC in the
middle of our project, which, while not breaking the project in any way, made
our code much less navigatble / readable. Thus, it certainly became harder to
implement new features. After cleaning our design to strictly follow MVC
and encapsulation of classes, our game had a much cleaner "pipeline" for the
controller to talk between the backend models and frontend views. And from this,
I've definitely learned how to develop with more awareness of game design.
One mistake I made was choosing convenience of feature implementation over 
going the "longer" way and following our design goals. In the future, I think
it is definitely a much better investment to always follow design before
convenience for future ease of development. 

Generally, I've also learned more about data-driven development. While I did 
not work with the data/configuration side of the project, I've still been
able to see how important data-driven development is to make projects much
more flexible, generalizable, and even customizable (like in the case of
Monopoly and having a change in XML's be all you need to change the view /
rules of a custom game.
-------------------------------------------------------------------------------


```