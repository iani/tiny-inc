+ SimpleNumber {
	+> { | linkName param = \amp |
		^linkName.asSynthLink.set(param, this).start
	}
}

+ Array {
	+> { | linkName |
		var synthLink;
		synthLink = SynthLink (linkName.asSymbol);
		this.keysValuesDo ({ | key, value |
			synthLink.set (key, value);
		});
		^synthLink;
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

	set { | param value |
		^SynthLink (this).set (param, value);
	}

	target_ { | target |
		^SynthLink (this).target = target;
	}
}

+ Function {
	+> { | linkName playerName = \player |
		^linkName.asSynthLink.addSource (this, playerName).start
	}

	asPlayer {
		^SynthPlayer (this);
		// ^synthplayer (functionsynthsource (this))
	}

	addToSelf {
		^SynthPlayer (this);		
	}
}

+ Event {
	asPlayer {
	   	^PatternPlayer (this);
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

	+> { | linkName, playerName |
		// var synthLink;
		[thisMethod.name, this, playerName].postln;
		if (playerName.isNil) {
			^SynthLink(linkName).addSource(this).start;	
		}{  // TODO: Make this a method of SynthLink
			^SynthLink (linkName).addFilterEvent (this, playerName).start;
			/* synthLink = SynthLink(linkName);
			if (synthLink.player.isKindOf (PatternPlayer).not) {
				synthLink.addSource (());
			};
			synthLink.player.addFilterEvent(this, playerName);
			^synthLink.start;
			*/
		};

	}
}

+ Array {
	+> { | name, server |
		SynthLink (name, server).restart (*this);
	}
}