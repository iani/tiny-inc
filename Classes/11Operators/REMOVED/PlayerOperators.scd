+ Object {
	+> { | linkName playerName = \player |
		^linkName.asSynthLink
		// .player_ (this.asPlayer(playerName)).start
		.addSource(this, playerName).start
	}
}

+ Symbol {
	restart { | ... args |
		this.asSynthLink.restart(*args); 
	}

	start { | ... args |
		this.asSynthLink.start(*args);
	}

	asSynthLink { | server |
		^SynthLink(this, server)
	}

	stop { | server |
		^SynthLink(this, server).stop
	}

	asPlayer {
		^SynthPlayer (SynthDefSource (this))
	}
}

+ Function {
	asPlayer {
		^SynthPlayer (this);
		// ^SynthPlayer (FunctionSynthSource (this))
	}

	addToSelf {
		^SynthPlayer (this);		
	}
}

+ Event {
	asPlayer { | playerName = \player |
	   	^PatternTaskPlayer ().addPlayerFromEvent (this, playerName);
	}

	addToSelf { | taskPlayer, playerName = \player|
		/*
		var eventPlayer;
		eventPlayer =  EventPlayer(playerName);
		eventPlayer.pattern = this;
		eventPlayer addTo: taskPlayer;
		^taskPlayer;
		*/
		^taskPlayer.addPlayerFromEvent (this, playerName);
		
	}
	
	addToSynthLink { | synthLink, name |
		^synthLink.addEventAsTaskPlayerFilter(this, name);
	}

	+> { | linkName, playerName = \player |
		[thisMethod.name, this, playerName].postln;
		^SynthLink(linkName).addSource(this, playerName).start;	
	}
	
	+>> { | linkName |
		^SynthLink(linkName).addEventAsTaskPlayerSource(this)
	}
}

+ Array {
	+> { | name, server |
		SynthLink (name, server).restart (*this);
	}
}