AbstractPlayer {
	var <process, <source;

	start { this.makeProcess }
	stop { if (this.isPlaying) { this.prStop } }
	isPlaying { ^process.notNil; }
	source_ { | argSource |
		this.setSource(argSource);
		this.changed(\source)
	}

	setSource { | argSource |
		source = argSource.asSource;
	}

	prStop { process.stop }
}
