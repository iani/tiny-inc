+ Object {
	+> { | linkName playerName = \player |
		var lp;
		lp = linkName.asSynthLink;
		lp.addSource(this, playerName);
		// .player_ (this.asPlayer(playerName)).start
		// [this, thisMethod.name, lp, lp.isPlaying].postln;
		//		[ "lp.player.process", lp.player.process].postln;
		//if (lp.isPlaying.not) { lp.start };
		lp.start;
		^lp;
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
		// ^synthplayer (functionsynthsource (this))
	}

	addToSelf {
		^SynthPlayer (this);		
	}
}

+ Event {
	asPlayer { | playername = \player |
	   	^PatternTaskPlayer ().addPlayerFromEvent (this, playername);
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
		^SynthLink(linkName).addEventAsTaskPlayerSource(this).start;
	}
}

+ Array {
	+> { | name, server |
		SynthLink (name, server).restart (*this);
	}
}