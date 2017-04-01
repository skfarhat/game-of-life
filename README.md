# Welcome to the Jungle

## How consuming other LifeAgents works

We cannot trust the implementers of Consumer to follow proper logic and reasoning: Deer.java can be implemented such that
the deer consumes ANY LifeAgent it is passed (including Wolves..).

And so it must be that whichever class calls consume() on a LifeAgent, also has to ensure the LifeAgents() passed are
consumable.

** An implementation of Consumable that makes me go mad**
If we do not design it like this then implementers will have the power of doing the below:

public void consume(LifeAgent prey) {
    if (prey instanceof Grass) {
        prey.die();
    }
    else if (prey instanceof Lion) {
        prey.die(); // <== this would be absurd!!
    }
}