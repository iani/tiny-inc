+ Object {
	+> { | linkName playerName = \player |
		^this.addToSynthLink(SynthLink(linkName), playerName).start
	}
}

+ Symbol {
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
	asPlayer { ^SynthPlayer (FunctionSynthSource (this)) }
}

+ Event {

	addToSynthLink { | synthLink, name |
		^synthLink.addEventAsTaskPlayerFilter(this, name);
	}
	
	+>> { | linkName |
		^SynthLink(linkName).addEventAsTaskPlayerSource(this)
	}
}