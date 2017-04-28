# Assignment Notes

## Agent
Any agent in the system. Each is characterised by an identifier unique to the duration of the 
simluation.

## LifeAgent
LifeAgent: anything that has a life and has energy. AliveAgents can always die, but 
do not necessarily age, the implementer can choose to implement the interface **Age** or not. 

## Generics vs Inheritance
LifeCell was first subclassing Cell but then the issue of member hiding appeared as 
one of the Unit Tests was failing. It appeared that the subclass had two 'agents' one for itself and one belonging to its parent class.

// call many constructors from one constructor,
// implementing the hashcode method in Point2D class

// abstraction or information hiding is manifested in the  interfac between RootController and COntrolPaneController 

While ControlPaneController needs access to some methods in RootController, it does not have full access to it through a 'this'

reference passed. This would have given ControlPaneController far too much control and power and made it less obvious to the programmer 
what that classs is/ or could be doing. Instead, an interface LifeStarter whose methods are already known is passed. 

## Organisation of the report: 

In the introduction we outline the organisation of the report.

1- Discuss initial ideas about the project, what was planned out and what had to change.In terms of drawing out interfaces and clases and the inheritance stuff and all.

Classes that were in the plan and their thorough design paid off: 
  * LifeAgent, Agent.
  * Point2D
  * Cell, Grid
  * interfaces: Reproduce, Move, Consume

Things that weren't in the original plan: 
  * LifeOptions, LifeAgentOptions, ConsumeRules
  * LifeAgentStats
  * Creature and Surface
  * It was though only Consume would be useful

2- The motivation behind developing it the way we did. Why the need for such flexibility, and robustness. --> The aim was to build a library that could be easily usable by other developers to implement their own agent based simulation.

3- OOP concepts that were used: (constructors, chainign, inheritance, interfaces, concurrency, implementations of toString())
  * Mention how in Object Orientation focuses on limiting the complexity needed at any level. 
  * Mention how a developer has limited focus and this is why complexity must be minimised -- from the book CodeComplete


4- Design patterns employed: Facade, Singleton at the beginning but was ommitted.
  * facade
  * observable pattern: reason for not using java.util.Observable - the fact that it  has to extended is limiting and so our own implemetation was added. 
  * encapsulation: used abundandly

5- Tools used in development. 
  * Git: Version control - single branch though as there were no other contributors to the project. Haha
  * Gradle: as a build management tool. Useful for fetching dependencies (there weren't any in this case) but also 
  * Jenkins: check Jenkinsfile not followed through  extensively -- git hooks were used instead to check the builds before making commits. Given there were no other developes, there wasn' a lot of merging and continuous integration that required the tool to be extensively used.  


Known limitations: 
* while Life was eventually implemented in such a manner as to make many "Life" instances spwanable and running concurrently. This doesn't work yet. A major known limitation is the implementation of statistics is dependent on LifeAgent static. So statistics fetched will pertain to all life instances. 

## Things to talk about: 

* the Object methods: 
  * equals override and  why: 
  * hashcode overide and why ? 
  * toString()

* Constructors: 
  * Many of them with different possible input parameters. Generally the more specific constructors end up calling the most generic one. The benfit of this method is that if there was something that needed to be added or modified in the constructor, it would only need to happen on the one most generic constructor. The limitation is that calls to this() have to happen at the top of the constructor which can sometimes constitute a limitation. private init() functions are often the solution.
  * chaining is also very common in our code. Deer(Point2D p, Integer energy) -- mention the use of this() and super(), the differences
  * any downsides to this() and super() [TODO: check the internet]
* Exceptions: (Part 4 in the lectures) the difference between Checked and Unchecked exceptions and how me made use of them. [TODO: draw UML diagram of the inheritance between our exception classes]
    * talk about how there are many ways of reporting errors. returning boolean (true,false) but this is only useful when all we care about is telling the user when a method has failed (false is used). Another way of reporting errors is through integers. For example in languages like C especially, returning a zero integer is indiciation that the function called succeeded. Other error numbers returned will signify different things.
    * calls to throws() -- handleed exceptions and propagating them
    * RuntimeExceptions which are unchecked. -- TODO: read more online and detail why and when they are useful. In our case, we use them mostly with LifeImplementationException, some PositionIsOuOfBoundsException and some others. 
    * calls to finally? don't think we've used any 
    * Generally speaking, the Java way is to use Exceptions. Exceptions are powerful and flexbile and can as well be used to report the type of the error that occured. 
      * Pros of exceptions vs returning error codes: 
        * exception constuctors can passed a string describing the type of error that occured.
        * with inheritance in exceptions we make some errors be a general type of others. THis is more difficult (or hacky) to do with return codes.
      * Cons of expcetions:
        * they are slower ?  
        * TODO: check the internet 
* Where Hashmaps were used in our code: 
  * TODO: where in our code are we using HashMaps, and why did we choose to use Map instead of another data structure
  * What are the benefits of a map, its performance metrics O(1) insertion, find, remove as long as the hashing is effectiv. 
  * Where have we overridden hashcode()
  * The fact that Map is used s the declared type and Hashmap is the runtime implmetation. This allows us to easily switch from HashmAp to another type of map in the future without many modifications. IF we wanted to use a SortedMap?
  * Nested Classes: RUlesPane and RulePane but then they were untangled? why ?
* What kinds of static fatures and why ? 
* Why is LifeAgent(Point2D p, Integer energy) and not LifeAgent(Point2D p, int energy). Because the latter allows us to properly use reflection to create instances of LifeAGent, check implementation of reproduce.
* Arrays: most common implementation are ARrayLists at runtime, declared List for compile time. 
* Grid employs multidimensional array
* ConcurrentQueue for bridge between gui engine and the core 
* Data structures used and why. 
* Talk about Subtyping, Subclssing, the difference? We use this a lot, an example is in Life.java we define a List<Agent> of agents and then create subtypes of those agents. In making the code more object oriented and more flexible and configurable we found the need to differentiate between wolf and deer on one side, and grass on the other. The fundamental difference between them as far is the system is concerned is that there can only be one instance of  Grass type in a single cell in the system whereas we can have many wolf and deer instances. This led to the creation of the Creature and Surface classes. 
* Note on super/sub classes is that most often we find that subtypes have to explicitly call the constructors of their parent (super) types. This is because constructors are not inherited in Java. 
* The creation of those superclasses allows future developers to create new agents that behave in a similar manner to wolves on one hand, or to grass on the other. For example, we envision an agent water to be very simmilar to Grass in that there can only be one instance of Water in a single cell . 
* Reflection for creating classes -- see reproduce() function in LifeAgent.
* Immutability: Point2D -- can mention the fact that there was a bug related to being able to change the pos of a point after it's creation. Cells were confused as to their actual position. The bug lasted for 3 days. Making the class immutable as well as so other changes solved the bug. To much power can be trouble sometimes.
* No explicit use of shadowing. It is not the best practice in Java. -- check the internet fo claims that it is not the best. 
* Casting takes place. BUt abundance of it is not the best sign - designs  need to be reconsidered when there's a lot of casting 
* instanceof used many times. e.g. in Life we want to handle the actions of an AGnet differently between a Surface and a Creature and so instnaceof is used  
* Access modifiers: private, public, protected. 
  * When was protected used? No recollection.
  * Almost no field is set as public, all are private and have getters 
  * Encapsulating attributes: LifeAgentOptions. 
  * pivate methods are those that are only used in the class. and generally called by public methods. They cannot be tested - and they needn't be in general.
* list the interfaces that were used and the need for each one
* abstract classes: Agent and LifeAGent and why are they abstract? 
* briefly discuss each class and interface created in the project and mention how it is used externally. 
* are we using interface inheritance?
* inner classs: TODO: where do we have inner classes and why did we use them ?
* inner classes in methods: maybe mention the creation of a runnable with its implementation inside the method in RootController? This a inner class in a method.
  * We can also mention how we needed to distinguish between the different thises 
* TODO: check anonymous inner classes
* Lambda expressions: 
  * Next action: check if using streams and laambda expressions goes against object oriented programming.
  * If lambdas are not oop ffriendly, remove a lot, keep some and then talk about the ones we kept
* Iterators: 



## Next actions 
* reorganise this list and make sure eaach entry has a next action thing to do
* group the notes that need to be grouped together
