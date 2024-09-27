# ChuzzGen
 
ChuzzGen - Chess Puzzle Generator

------------------------------------------------------------

Pre-requisites

- Java Development Kit (JRE: 1.8)
- Stockfish 1.16 (download from here: https://stockfishchess.org/download/)

------------------------------------------------------------

Setting up the Stockfish engine

1) Go to 'https://stockfishchess.org/download/' and download the correct version for your machine
2) Copy the Stockfish executable (.exe) to 'ChuzzGen/' directory
3) Change the variable 'STOCKFISH_VERSION' of '/ChuzzGen/chuzzgen/Board.java' to 
   '/[STOCKFISH_EXECUTABLE]', where 'STOCKFISH_EXECUTABLE' is the name of the Stockfish .exe file

------------------------------------------------------------

How to run

1) Navigate to '/ChuzzGen/chuzzgen/' directory in a Terminal window
2) Type 'javac *.java' in the Terminal to compile all .java files
3) Navigate up one directory to 'ChuzzGen/'
4) Run the program by typing 'java chuzzgen.Main'

------------------------------------------------------------

For a technical description of how generation works and analysis from a computational creativity
standpoint, check out the [paper](ChuzzGen_paper.pdf).
