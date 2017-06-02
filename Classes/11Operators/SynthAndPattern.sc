
+ Symbol {
	asPatternPlayer { ^(Registry (\patterns, this, { EventPattern().play })).play }
}

+ Object {
	+> { | symbol, adverb |
	   	^symbol.asPatternPlayer.addKey (adverb, this);
	}
}

+ Function {
	+> { | symbol, adverb |
		
		if (adverb.isNil) {
			^symbol.asSynthPlayer.playFunction (this);
		}{
			^symbol.asPatternPlayer.addKey (adverb, Pfunc (this));
		}
	}
}

+ Event {
	+> { | symbol, adverb | ^symbol.asPatternPlayer.addEvent (this) }
}

+ EventStreamPlayer {
	addKey { | key, object | stream.event [key] = object.asStream }

	addEvent { | argEvent |
		var event;
		event = stream.event;
		argEvent keysValuesDo: { | key, value |
			event [key] = value.asStream;
		}
	}
}