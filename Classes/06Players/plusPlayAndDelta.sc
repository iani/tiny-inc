//  3 May 2016 21:46

+ Event {
	playAndDelta { | cleanup, mute |
		if (mute) { this.put(\type, \rest) };
		cleanup.update(this);
		this.play;
		[thisMethod.name, this].postln;
		this.subevents do: _.(this);
		^this.delta;
	}
}