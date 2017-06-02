
+ Symbol {
	asSynthPlayer {
		
	}

	asPatternPlayer {
		^Registry (\patterns, this, {
			EventPattern().play;
		});
	}

	play {
		var player;
		player = this.asPatternPlayer;
		if (player.isPlaying) { ^player } { ^player.play };
	}
}

+ Object {
	+> { | adverb, symbol |
		^symbol.asPatternPlayer.addKey (adverb, this);
	}
}

+ Function {
	+> { | adverb, symbol |
		if (adverb.isNil) {
			^symbol.asSynthPlayer.playFunction (this);
		}{
			^symbol.asPatternPlayer.addKey (adverb, Pfunc (this));
		}
	}
}

+ Event {
	+> { | symbol |
		[symbol, this].postln;
		^symbol.asPatternPlayer.addEvent (this);
	}
}

+ EventStreamPlayer {
	addKey { | key, object |
		stream.event [key] = object.asStream;
	}

	addEvent { | argEvent |
		var event;
		event = stream.event;
		argEvent keysValuesDo: { | key, value |
			event [key] = value.asStream;
		}
	}
}