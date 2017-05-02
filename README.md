# Welcome to the Jungle

## Configuration options

#### LifeOptions
* maxIterations
* grid dimensions (columns/rows)
* consumeRules 
	* Wolf.class -> [Deer.class]
	* Deer.class -> [Grass.class] 
* map (key=Class\<LifeAgent\> => value=LifeAgentOptions)	

#### LifeAgentOptions

|                  | type   | range       | description |
|------------------|--------|-------------|-------------|
| ageBy            | int    | ]-inf; inf[ |             |
| initialEnergy    | int    | [0; inf[    |             |
| reproductionRate | double | [0; 1]      |             |
| energyGained     | int    | [0; inf[    |             |
| energyLost       | int    | [0; inf[    |             |
Example: 

	Wolf.class  -> { ageBy:1, initialCount:5, initialEnergy:10, reproductionRate:0.1, energyGain:3, energyLoss:2 }
	Deer.class  -> { ageBy:1, initialCount:15, initialEnergy:5, reproductionRate:0.15, energyGain:2, energyLoss:2 }
	Grass.class -> { ageBy:-5, initialCount:15, initialEnergy:5, reproductionRate:1.0, energyGain:3, energyLoss:5 }

## Agent
Any agent in the system. An agent is characterised by:
  	
  * an ID (String) unique for the duration of the simluation
  * a position (Point2D)


## LifeAgent

Interfaces: 

* Reprdoduce 
* Consumes 
* Consumable 

## Surface 
A cell can have a **one** Surface type associated with it, or none. Surfaces examples: 

* Grass
* Water (*not implemented*)

### Configuration parameters 


Example: 

|       |     ageBy    | initialEnergy | reproductionRate | initialCount | energyGained | energyLost |
|-------|:------------:|:-------------:|:----------------:|:------------:|:------------:|:----------:|
| Grass | grassAge < 0 |    grassE0    |      1           |    grassI0   |  grassEGain  | grassELost |
| Deer  |    deerAge   |     deerE0    |       deerR      |    deerI0    |   deerEGain  |  deerELost |
| Wolf  |    wolfAge   |     wolfE0    |       wolfR      |    wolfI0    |   wolfEGain  |  wolfELost |

## Consumes/Consumable

Implementations in Life: 

1. **Fixed energy gain**: consuming agents will gain a fixed energy upon consuming a consumable. The value of this fixed energy is determined by Life and in general should be configurable by the user.  
2. **Consumable's energy**: consuming agents will gain the energy of their consumable. 
3. **Capped consumable's energy**: consuming agents will gain the energy of their consumable but only up to a certain cap that is defined by Life. The value of this cap should be made configurable by the user but at the time of writing is not (it's a local method constant). 
4. **Not Implemented yet**   
  Consumable type energy : the consuming agent will gain an energy determined by the type of the agent it consumes  
e.g. wolf eats any deer and gains +5, wolf drinks water gains +1

**Implementation detail** 

We cannot trust the implementers of Consumer to follow proper logic and reasoning: Deer can be implemented such that the deer consumes ANY LifeAgent it is passed - including Wolves.

Whichever class calls 
```deer.consume(core.agents)``` has to ensure that the agents passed as param are consumable by the deer. Hence, the responsibility lies with the caller.


## Grass
*Instructions*: When a grass agent is chosen (say at (x,y)), it will execute the following steps:

    (1) increase its energy level by R_grass, and
    (2) randomly choose a neighbouring square (x0,y0) and, if (x0,y0) has no grass, generate a new grass agent (with a default grass configuration) at (x0,y0).

### Implementation
 This means that in the implementation of the reproduce method in Grass, the grass has to increase its own energy first, then choose a new cell to form another agent.


## Development 


|                       |                                  |
|-----------------------|----------------------------------|
| ./gradlew build       | compile all                      |
| ./gradlew test        | run tests                        |
| ./gradlew jacoco      | generate coverage                |
| ./gradlew fatjar      | create Jar with all dependencies |
| ./gradlew myJavadocs  | generate Java Docs for project   |



## OOP notes

LifeAgentStats: example of class that doesn't expose more than it needs to. field setters
are not allowed, only incrementors, because that's all that is needed. Of course, this can be hacked around,
but still.

Passing Unmodifiable Maps to classes that have no business changing out content.


Immutability: Point2D

Generic constructors (called by others, e.g. LifeAgent)

Design Patterns: Observer- Observable (gui)

Code Coverage:
