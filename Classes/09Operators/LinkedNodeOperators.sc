+ Object {
	+> { | symbol |
		^(this ++> symbol).start
	}

	++> { | symbol |
		^SynthLink(symbol).player_(this.asPlayer)	
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
	asPlayer {
		var dur;
		dur = this [\dur] ? 1;
		this [\dur] = nil;
		^TaskPlayer (dur).pattern_ (this)
	}
}