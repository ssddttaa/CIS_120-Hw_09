=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: sshaik
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
 is an appropriate use of the concept. You may copy and paste from your proposal
 document if you did not change the features you are implementing.

  1.2D Array

    - What specific feature of your game will be implemented using this concept?

      The function of this 2D array will be creating a 2D array that will store
      all of the mines. 

    - Why does it make sense to implement this feature with this concept? 
      Justify why this is a non-trivial application of the concept in question.

      This is the most optimal data structure because the game is in the form of
      a grid, and I will constantly need to access a certain mine at a certain 
      location, therefore the constant time access of an array will allow for
      this to be done efficiently in comparison to all other data structures.

  2.
  - What specific feature of your game will be implemented using this concept?

  I will be using I/O in order to store the high scores.

  - Why does it make sense to implement this feature with this concept?
  Justify why this is a non-trivial application of the concept in question.
  
  This is not a trivial application of the concept because the only way for the 
  game to be able to store the scores between sessions is to have the 
  information stored somewhere, and in this instance, on the computer. 
  This is also non-trivial because of the unique file format and the invariants 
  that this file format obeys. For instance, the file is stored such that it is
   ordered; in other words, the scores are sorted from top to bottom in order of
   highest score (which is lowest time) to lowest score (which is highest time).
   The file format itself is simply that every odd line contains a username, 
   and every even line contains a score associated with that username.
  
  

  3.Implementing Recursive Algorithms

- What specific feature of your game will be implemented using this concept?

  I will be implementing a recursive algorithm when revealing all of the blank 
  squares that are adjacent to the blank square that was clicked.

- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.

  This is because as soon as I reveal a blank square I need to check whether or 
  not any of the surrounding 8 squares are also blank. If so, then I need to 
  reveal those blank squares as well. Therefore, when revealing a blank square, 
  I feed the surrounding squares into the recursive function that then 
  recursively reveals all of the blank squares surrounding it. This is not a 
  trivial application of recursion because the recursive nature of nature of 
  this algorithm allows me to make the minimum possible number of checks for 
  blank squares, to only check the 8 squares surrounding the blank tile that
  was clicked rather than checking every square on the board.


  4. Inheritance / Subtyping
- What specific feature of your game will be implemented using this concept?
  I used inheritance / subtyping for the different type of buttons, as there 
  were mines, number buttons (buttons that displayed how many mines surround it)
  , and blank buttons  (squares that have no mines around each) each with 
  different functionality.

- Why does it make sense to implement this feature with this concept? Justify
  why this is a non-trivial application of the concept in question.
  Using inheritance/subtyping in this situation is crucial because of the 
  differing functionality of each of the buttons. Although all three types 
  (mine buttons, blank buttons, and number buttons) require some of the same 
  fields (how many mines surround it, whether or not it is clicked, whether or 
  not it is flagged), each square has a very different functionality associated 
  with it. For instance, the blank square tile, when clicked, must perform a 
  different action, namely, it must reveal all of the other blank squares 
  surrounding it. When a mine is clicked, it must cause the game to come to an 
  end. When the number button is clicked it must change its label to be a number
  so that the user knows how many mines surround it. Because of this differing 
  functionality among the different buttons, inheritance / sub typing is 
  essential. Also, this allows us to catch errors more easily. For instance, 
  if a number button is initialized, yet has 0 surrounding mines, then it can 
  throw an exception to let me know that somewhere the number button was 
  initialized incorrectly. Also, these different buttons have different 
  appearances after they are clicked, thus inheritance / sub typing allows for
  that to happen more easily.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
Game class
  
  This class handles the GUI of the game. For instance, it handles whether or 
  not the start screen should be showing, displaying the settings, instructions,
   or high scores frame. This class also handles the reset and going back to 
   the main menu once the user starts playing the game, because the reset has 
   to do with destroying the game frame itself, and therefore should be handled 
   in this game class, and the main menu button requires that the frame be 
   destroyed and the starting frame be displayed, which again, should be handled
    by this Game class as it deals with the setting up of the GUI of the game.

HighScores class
  This class simply reads a text file with all of the scores, parses them, and 
  coverts them into an array list of score objects (which just contain a 
  username and a score associated with it). This class also handles validating 
  the input into the high scores leaderboard.

MinesweeperDifficulty enum
  This enum simply stored the different difficulties and their sizes.

MinesweeperFrame class
  This class extended the JFrame class and served as the frame for which 
  minesweeper is actually played upon. This stores the associated minesweeper 
  panel and,generally, handles how the game should look once the player starts 
  playing.

MinesweeperPanel class
  This class handles all of the logic of the game itself. This class handles 
  whether or not the game has begun, the timer (score) aspect of the game, 
  whether or not the user has clicked a mine, setting up the game itself, etc.

Score class
  This is simply a small class used to score the username and score that was 
  read from the text file.

MineButton class
  This abstract class served as the parent class of all of the different types 
  of buttons. It contains the properties that are relevant to all of the 
  buttons, such as whether it is flagged, whether it is clicked, how many mines 
  surround it, etc, however, still requires that each class handle how their 
  appearance changes after they have been clicked differently.

Mine Class
  This is a subtype of the abstract MineButton class that serves to change its 
  appearance when it is clicked.

NumberButton Class
  This is also a subtype of the abstract MineButton class which must change its 
  appearance based on its click as it must add a label to display how many mines
  surround it.

Blank button class
  Similarly, this class is a subtype of the abstract mine button class as it 
  must change its appearance to be invisibly after being clicked.

- Revisit your proposal document. What components of your plan did you end up
  keeping? What did you have to change? Why?

From my proposal to the current game, I kept two concepts exactly the same. 
These two concepts was that of 2D arrays, as I still had to use them to 
effectively model the game state, and that of implementing recursive algorithms,
as I still had to use a recursive algorithm to reveal all of the blank squares
adjacent to the blank square that was clicked.

However, there were much of the code that I had to change. For instance, I 
removed that I was going to store the game state using I/O. Although I did end 
up still recording the high scores in a text document for this concept, I simply
did not have enough time to be able to store the game state using I/O and then 
effectively parse that data so the user could resume from where they left off. 
Another aspect of the plan that I changed was that I would no longer use complex
 search simply because I did not have enough time to implement this, 
 and was also already using inheritance / subtyping for my buttons. Therefore, 
 I changed the core concept that I was using from complex search to 
 inheritance/subtyping.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  The main stumbling block I had with the game was in recursively revealing all 
  of the blank squares. This is because I had to modify the button classes 
  themselves to store what mines were surrounding them so this revelation of 
  blank squares would be done efficiently. However, because of this I also had 
  to change how the squares were being initialized, as they would need to now 
  not only have their properties initialized, but also what mines surrounded 
  them. In addition, when debugging the recursive algorithm, I realized an error
   with how I was storing my buttons in the 2dimensional array (stored with the
    x and y coordinates flipped). Therefore, although this was a major stumbling
     block, this aspect of the game being implemented led me to catch many 
     errors in the code as well as to refactor the code to make it cleaner.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

In this game, the private state is well encapsulated mainly because of 
how separate each class was. Since each class handled almost all of what it 
needed to, independently of information from other classes, there was no need 
to even have getters and setters for many of the private fields in the classes.
This is because the game class handled all of the setting up of the GUI, the
minesweeperFrame set up specifically the frame of the minesweeper game in 
which the user will play on, and the minesweeper panel handled all of the 
game’s functionality. There was very little need for any of these classes to 
communicate with each other, therefore, no need to generate getters and 
setters for private variables thus preserving the private state of these 
classes. However, if there is one area in which the separation of 
functionality is not completely clear, it is between the buttons and the 
minesweeper panel class. This is because since the minesweeper panel class 
handled all of the logic, it required me to put the code that handled a click 
on the button to be placed in the minesweeper panel class rather than in the 
button class itself. However, the separation of concerns here is still present
to a degree, as the buttons served primarily, instead, to handle how their 
appearance changed as a result of their click rather than how the game logic
changed as a result of their click. If given the chance to refactor, 
I would try to have more of the event handling code in the actual button 
classes themselves rather than in the minesweeper panel class.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
Mine image: https://www.cs.utexas.edu/~mitra/csSummer2015/cs312/assgn/assgn7/mine.jpg (in full disclosure, I realized after creating the game that this is icon was part of a programming assignment at a university, however, if you look at how I implemented my code and how they implemented their template of the code to be filled in by their students, it is very different).

Minesweeper flag icon:
http://dougx.net/sweeper/flag.png


