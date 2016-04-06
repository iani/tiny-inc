/*  5 Apr 2016 20:00
SynthPlayer and PatternTaskPlayer may have to return new object.

*/



+ Nil {
	addSource { | source, name = \player |
		^source.asPlayer (name);
	}
}

+ Function {
	asPlayer {
		^SynthPlayer (this)
	}

	addSelfToSynthPlayer { | player |
		[this, thisMethod.name, player].postln;
		^player.source = this;
	}

	addSelfToTaskPlayer {
		^SynthPlayer (this)
	}
}


+ Event {
	asPlayer { | name |
	^PatternTaskPlayer ().addPlayerFromEvent (this, name);
	}

	addSelfToSynthPlayer { | player, name = \player |
		^PatternTaskPlayer ().addPlayerFromEvent (this, name);
	}

	addSelfToTaskPlayer { | player name = \player |
		^player.addPlayerFromEvent (this, name);
	}
}

+ SynthPlayer {
	addSource { | source, name = \player|
		^source.addSelfToSynthPlayer(this, name);	
	}
}

+ PatternTaskPlayer {
	addSource { | source, name = \player |
		^source.addSelfToTaskPlayer(this, name);	
	}
}
