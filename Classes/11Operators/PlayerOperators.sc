+ Object {
	+> { | linkName playerName = \player |
		^linkName.asSynthLink
		.player_ (this.asPlayer(playerName)).start
	}
}

+ Symbol {
	restart { | ... args |
		this.asSynthLink.restart(args); 
	}

	start { | ... args |
		this.asSynthLink.start(args);
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
}

+ Event {
	asPlayer { | playerName = \player |
		^PatternTaskPlayer ().addPlayerFromEvent (this, playerName);
	}
	addToSynthLink { | synthLink, name |
		^synthLink.addEventAsTaskPlayerFilter(this, name);
	}
	
	+>> { | linkName |
		^SynthLink(linkName).addEventAsTaskPlayerSource(this)
	}
}