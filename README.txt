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
4) *Recompile* and run by following Method 2 in 'How to run' below

------------------------------------------------------------

How to run

Method 1:
1) Run the file 'ChuzzGen.jar' (you may have to wait a few seconds for initial generation)

Method 2 (FRESH COMPILATION VIA TERMINAL, RECOMMENDED IF ANY EDITS MADE TO SOURCE CODE):
1) Navigate to '/ChuzzGen/chuzzgen/' directory in a Terminal window
2) Type 'javac *.java' in the Terminal to compile all .java files
3) Navigate up one directory to 'ChuzzGen/'
4) Run the program by typing 'java chuzzgen.Main'