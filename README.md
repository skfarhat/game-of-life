# Welcome to the Jungle

## Agent
Any agent in the system. An agent is characterised by:
  * by an identifier unique for the duration of the simluation
  * a position 


## LifeAgent
LifeAgent: anything that has a life and has energy. 

Interfaces implemented: 
  * Reproducable :
  * Consumable   : can be consumed, every lifeagent is prone to being consumed

AliveAgents can always die, but 
do not necessarily age, the implementer can choose to implement the interface **Age** or not. 

## How consuming other LifeAgents works

We cannot trust the implementers of Consumer to follow proper logic and reasoning: Deer.java can be implemented such that
the deer consumes ANY LifeAgent it is passed (including Wolves..).

And so it must be that whichever class calls consume() on a LifeAgent, also has to ensure the LifeAgents() passed are
consumable.

** An implementation of Consumable that makes me go mad**
If we do not design it like this then implementers will have the power of doing the below:
`
public void consume(LifeAgent prey) {
    if (prey instanceof Grass) {
        prey.die();
    }
    else if (prey instanceof Lion) {
        prey.die(); // <== this would be absurd!!
    }
}
`
