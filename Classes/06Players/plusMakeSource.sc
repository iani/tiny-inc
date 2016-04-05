/*  4 Apr 2016 16:40
Players that exist as sources in a SynthLink select what to do with new sources added
through an operator:

SynthPlayer.makeSource()
if Source is function, create new SynthPlayer
If it is Event, create new PatternTaskPlayer

In both cases return the receiver "asSource".

Function makes SynthPlayer
Event makes PatternTaskPlayer

TaskPlayer.makeSource()

if Source is function, create new SynthPlayer
If it is Event, add EventPlayer to PatternTaskPlayer under name 

What method name will do this, in which classes?

function:addTo -> returns SynthPlayer
event:addTo -> creates EventPlayer and puts it in PatternTaskPlayer

*/

+ Nil {
	makeSource { | source, name = \player |
			^source.asPlayer (name);
	}
}

+ SynthPlayer {
	makeSource { | source, name = \player |
			^this.source_(source);
	}
	
}

+ TaskPlayer {
	makeSource { | source, playerName = \player |
		^source.addToSelf (this, playerName)
	}
}
	





