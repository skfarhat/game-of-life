# Assignment Notes


## Next actions 

 	1- identify the interesting design decisions to talk about in the report 
 	2- what OOP language features help in the design. 
	3- reorganise this list and make sure eaach entry has a next action thing to do
	4- group the notes that need to be grouped together
	5- detail what each class in the system does 

## Design Decisions 

### Configuration
* There is a lot to be gained in being able to configure different aspects of the system. For (1) it makes the system more flexible and extensible, (2) it allows many more test cases to be conducted, (3) it allows for better edge case testing of the system as well. 
* the addition of elements like Water in the future which are likely to resemble Grass in behaviour pushed for creating different superclasses (Creature for Wolf & Deer, and Surface for Grass). 

### Agent vs LifeAgent
* **Agent** in the system. Each is characterised by an identifier unique to the duration of the simluation.  
* **LifeAgent** anything that has a life and has energy. AliveAgents can always die, but 
do not necessarily age, the implementer can choose to implement the interfaceAge or not. 

### LifeAgent - interface implemetations 
Simliarity between our agents is that they all: 

* (**TODO**) reproduce: difficulties with reproduce implementation in LifeAgent. *use of reflection*
* Age (whether positively or negatively): Grass grows (ages negatively, gains energy), Wolf and Deer (age positively, they lose energy). So this was dubbed as a mere configuration parameter settable by the user on start.  

#### Consume/Consumable 

Can't create superclasses for consume/consumable, (1) if we did  we would be abusing inheritance anyway, creating consume/consumable classes never was an option (or thought) anyway. They were going to be interfaces for sure. The question was who was going to implement what and where was that implementation going to reside. Let's start easy, Deer would implement both. It consumes Grass and is Consumed by Wolf. This meant the overriding of two methods in the Deer class: (TODO) list all of them (consumeBy, consume..)  
	Why this was annoying: 
	
	(1) a class implementor was in charge of how its class (lifeagent was to be consumed), in fact when implementing the class they could just not do anythin in the method making their class above all others in that it was consumed. This felt wrong.   
	(2) it is incovenient to have to force the programmer to repetitively implement methods they really don't want to bother with. It also makes it likely for errors and mistakes to be introduced, more code to test, more verbose..

Point (2) would have been easier to solve. We would have created default methods in our interfaces. When a developer creates new class (for instance Lion), they would just make their class implement Consumes (and maybe Consumable) and shouldbn't have to worry about implementing the methods. But if they chosen not to implement Lion.. they would be making their class immune to being consumed..  The design felt flawed. 

If Point (1) were to be taken seriously, we would have to impose an implementation of the consume, consumable interfaces on all new LifeAgents to be created. This means the interface implementations have to go up a level in the class hierarchy (up to the parent class). Point (2) would be solved automatically, since the subtyping class would not have to worry about implemeting them, but this would mean that all LifeAgent subtypes were both consuming and consumable agents. Actually, this turns out to be perfectly accurate, even conceptually in our universe everything is prone to be consumed by something else. In our current implementation, Wolf is not consumed by another agent, but iif were to add another, for instance "Lion" class, then perhaps wolves will have to become consumable. 

This promped further thought on what defines though consume/consumable relationships and this lead to the creation of the below classes: 

#### ConsumeRule(s) 

**ConsumeRule**: a simple class defining a relationship betwee two LifeAgent subclasses as *consumer --> consumable*. 

An example instance of this class for our purposes would be, e.g.

	ConsumeRule(consumer=Wolf, consumable=Deer)  
	ConsumeRule(consumer=Deer, consumable=Grass)
	
**ConsumeRules**: a data structure storing the consume rules in our system. Initially the data structure storing the ConsumeRules was a member of another class(LifeOptions). 

Methods relating to the data-structure had to be created in LifeOptions, this added bulkiness to the code, no separation, difficult testing and changes to the type of the data structure involved many changes to several parts of the code. Example: other parts using the ConsumeRules data structure had to write `Map<Class<?extends LifeAgent>, Class<?extends LifeAgent>>` this very quickly became arduous. ConsumeRules was createed and implements the Set interface.

#### ConsumeRules: Data structure

##### Set 
1. Set API: the functions to override make most sense for our purposes, add(ConsumeRule) makes more sense than put(Key, ConsumeRule)
2. Sets naturally imply no duplication which is useful and indicative -- and we have to implement equals() in ConsumeRule to enforce no duplication. 

Interestingly enough, under the hood ConsumeRules is not implemented using any  Set subtype (e.g. HashSet). Instead it uses a combination of HashMap, ArrayList, and Set. 

Most common operations on it will be getting and filtering-by. While changes to it can occur at any point, it is not envisionned that this data struct will not change a lot. Since users will likely set the rules before starting the simulation, they can modify them at runtime but this is likely to be a few-off operation.

One of the most widely used methods in ConsumeRules is `getConsumableClassesForAgent(Class<?extends LifeAgent>)` which returns a list of classes that are consumable by the LifeAgent subclass type passed to us. 
If a set was used as the main data struct we would have to do O(n) when the method is called: iterate through all consume rules, and those that are consumable by the class passed as param. 

##### HashMap
To improve performance of this method we implement a HashMap with key a class that is a subclass of LifeAgent, and an Set containg its consumable classes. Every consuming agent, has a list of all the agents it can consume. 

	e.g. Map: key -> value
	Lion.class -> Set{Wolf.class, Deer.class}
	Wolf.class -> Set{Deer.class}
	Deer.class -> Set{Grass.class}

When `getConsumableClassesForAgent()` is called, the fetching is fetch is fast since it is get() on a hashmap O(1). 

##### List
*Why the need for List?*   
Since the ConsumeRules implements Set API, it needs to override the iterator() method, and also this method is expected to be used in our application. HashMaps don't have iterators, and while we can use entrySet() to then make an iterator, this is likely to be expensive. An improvement would be to use LinkedHashMap instead of hashmap -- this way, when we want to get the all the entries we have to iterate over the size of the map and not its capacity. But mind you, for each key we still have to iterate over every set. This is not very performant and won't scale well. 

The decision was to create another List that is in sync with HashMap (it stores the same ConsumeRule s). When ConsumeRules is queries for an iterator, it can thus easily return an iterator() and when the method `getConsumableClassesForAgent()` is called, an iterator on the Consumable classes can quickly be returned `map.get(consumerClass)`. 

## Language features that help  (2) 

Java features: 

* Garbage collection in Java is very helpful, there's less for the developer to think about and they can focus on doing effective design decisions. 
* Single inheritace is a feature i like in java. pushes the use for interfaces even more which have many beneefits and less complication consequences down the road.

----------------------------------------------------------------------------- 

## Organisation of the report: 

In the introduction we outline the organisation of the report.

####  1- Discuss initial ideas about the project. 
What was planned out and what had to change.In terms of drawing out interfaces and clases and the inheritance stuff and all.

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

#### 2- The motivation behind developing it the way we did. 
Why the need for such flexibility, and robustness. --> The aim was to build a library that could be easily usable by other developers to implement their own agent based simulation.

#### 3- OOP concepts that were used: 

(constructors, chainign, inheritance, interfaces, concurrency, implementations of toString())

  * Mention how in Object Orientation focuses on limiting the complexity needed at any level. 
  * Mention how a developer has limited focus and this is why complexity must be minimised -- from the book CodeComplete


#### 4- Design patterns employed: 
Facade, Singleton at the beginning but was ommitted.

  * facade:
    // abstraction or information hiding is manifested in the  interfac between RootController and COntrolPaneController 
    * While ControlPaneController needs access to some methods in RootController, it does not have full access to it through a 'this'
    * reference passed. This would have given ControlPaneController far too much control and power and made it less obvious to the programmer 
what that classs is/ or could be doing. Instead, an interface LifeStarter whose methods are already known is passed.
  * observable pattern: reason for not using java.util.Observable - the fact that it  has to extended is limiting and so our own implemetation was added. 
  * encapsulation: used abundandly

#### 5- Tools used in development. 

  * Git: Version control - single branch though as there were no other contributors to the project. Haha
  * Gradle: as a build management tool. Useful for fetching dependencies (there weren't any in this case) but also 
  * Jenkins: check Jenkinsfile not followed through  extensively -- git hooks were used instead to check the builds before making commits. Given there were no other developes, there wasn' a lot of merging and continuous integration that required the tool to be extensively used.  


### Known limitations: 

* while Life was eventually implemented in such a manner as to make many "Life" instances spwanable and running concurrently. This doesn't work yet. A major known limitation is the implementation of statistics is dependent on LifeAgent static. So statistics fetched will pertain to all life instances. 

## Things to talk about: 

* the Object methods: 
  * equals override and  why: 
  * hashcode overide and why ? : in Point2D
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
* Surface vs Creature. Surface doesn't move. Only one surface per cell. Surface does not die when consumed, it loses energy.  
* Note on super/sub classes is that most often we find that subtypes have to explicitly call the constructors of their parent (super) types. This is because constructors are not inherited in Java. 
* The creation of those superclasses allows future developers to create new agents that behave in a similar manner to wolves on one hand, or to grass on the other. For example, we envision an agent water to be very simmilar to Grass in that there can only be one instance of Water in a single cell . 
* Reflection for creating classes -- see reproduce() function in LifeAgent.
* Immutability: Point2D -- can mention the fact that there was a bug related to being able to change the pos of a point after it's creation. Cells were confused as to their actual position. The bug lasted for 3 days. Making the class immutable as well as so other changes solved the bug. To much power can be trouble sometimes.
* No explicit use of shadowing. It is not the best practice in Java. -- check the internet fo claims that it is not the best. 
    **LifeCell was first subclassing Cell but then the issue of member hiding appeared as one of the Unit Tests was failing. It appeared that the subclass had two 'agents' one for itself and one belonging to its parent class.**
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
* Method overloading is very common in our case: a method with the same name but different parameters
* TODO: check anonymous inner classes
* Lambda expressions: 
  * Next action: check if using streams and laambda expressions goes against object oriented programming.
  * If lambdas are not oop ffriendly, remove a lot, keep some and then talk about the ones we kept
* Iterators: 
* Delegation  uses in our code
