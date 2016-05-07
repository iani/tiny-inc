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

	addSelfToPatternPlayer { | previousPlayer |
		previousPlayer.stop; // always replace previous player with new synth
		^SynthPlayer (this)
	}
}


+ Event {
	asPlayer { | name |
		//		^PatternTaskPlayer ().addPlayerFromEvent (this, name);
	}

	addSelfToSynthPlayer { | previousPlayer |
		previousPlayer.release; // replace synth with new pattern
   		^PatternPlayer (this)
		// ^
	}

	addSelfToPatternPlayer { | player name = \player |
		//		[this, thisMethod.name].postln;
		// postf ("player % addPlayerFromEvent args: %, %\n", this, name);
		//		^player.addFilterEvent (this, name);
		^player.addEvent (this);
	}
}

+ SynthPlayer {
 	addSource { | source, name = \player|
		this.release;  // always release: new source replaces previous sound
		^source.addSelfToSynthPlayer(this, name);	
	}
}

