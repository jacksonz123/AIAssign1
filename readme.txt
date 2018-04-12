Author: 7671903 Jackson Zaloumis
Solution code for Robot Navigation Problem - Assignment 1

Features/Bugs/Missing
-===================-
USAGE:
	RobotNav <file> <method>
	RobotNav <file> <method> <defaultCosts>


RobotNav takes two or three  arguments.
The first refers to a text file which contains one Robot Navigation Problem.
The second refers to the method that you want to use to solve the problem in the above file. See Search Table for a list of valid methods.
The Third is a true/false that refers to whether you want to use the default costing (1 for each movement) or a custom one (Up=4,Down=1,Left=Right=2)

Search Table
Parameter	|Method Name
----------------+----------
BFS		|Breadth-first Search
DFS		|Depth-first Search
GBFS		|Greedy Best-first Search
AS		|A* Search
UCS		|Uniform Cost Search

Known Bugs:

Acknowledgements/Resources:
- nPuzzler: I extended the nPuzzler to use the above search methods to be able to understand how Searches work and are implemented in Java.
		This knowledge then allowed me to program a solution for RobotNavigation Problem.
- Lectures/Lecture Slides: Gave me knowledge to understand the theory of different search methods.
- Textbook: Gave me knowledge to understand the theory of different search methods.