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

	addSelfToSynthPlayer { | player, name = \player |
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
		[this, thisMethod.name].postln;
		postf ("player % addPlayerFromEvent args: %, %\n", this, name);
		^player.addPlayerFromEvent (this, name);
	}
}

+ SynthPlayer {
 	addSource { | source, name = \player|
		//		"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
		// postf ("%, % isPlaying: %\n", this, thisMethod.name, this.isPlaying);
		//"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
		// "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
		// "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!".postln;
		this.stopIfPlaying;
		^source.addSelfToSynthPlayer(this, name);	
	}
}

+ PatternTaskPlayer {
	addSource { | source, name = \player |
		[this, thisMethod.name].postln;
		^source.addSelfToTaskPlayer(this, name);	
	}
}
