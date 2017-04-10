# Welcome to the Jungle (v0.1)

## Agent
Any agent in the system. An agent is characterised by:
  	
  * by an identifier unique for the duration of the simluation
  * a position 


## LifeAgent
LifeAgent: anything that has a life and has energy. 

Interfaces implemented: 

  * Reproducable :
  * Consumable : can be consumed, every lifeagent is prone to being consumed

AliveAgents can always die, but do not necessarily age, the implementer can choose to implement the interface **Age** or not. 

## How consuming other LifeAgents works

We cannot trust the implementers of Consumer to follow proper logic and reasoning: Deer can be implemented such that the deer consumes ANY LifeAgent it is passed - including Wolves.

Whichever class calls 
```deer.consume(agents)``` has to ensure that the agents passed as param are consumable by the deer. Hence, the responsibility lies with the caller. 

