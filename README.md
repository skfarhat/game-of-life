# Welcome to the Jungle (v0.1)

## Agent
Any agent in the system. An agent is characterised by:
  	
  * an ID (String) unique for the duration of the simluation
  * a position (Point2D)


## LifeAgent
LifeAgent: anything that has a life and has energy. 

Interfaces implemented: 

  * Reproducable :
  * Consumable : can be consumed, every lifeagent is prone to being consumed

AliveAgents can always die, but do not necessarily age, the implementer can choose to implement the interface **Age** or not. 

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
```deer.consume(agents)``` has to ensure that the agents passed as param are consumable by the deer. Hence, the responsibility lies with the caller. 


## Grass
*Instructions*: When a grass agent is chosen (say at (x,y)), it will execute the following steps:

    (1) increase its energy level by R_grass, and
    (2) randomly choose a neighbouring square (x0,y0) and, if (x0,y0) has no grass, generate a new grass agent (with a default grass configuration) at (x0,y0).

### Implementation
 This means that in the implementation of the reproduce method in Grass, the grass has to increase its own energy first, then choose a new cell to form another agent.
