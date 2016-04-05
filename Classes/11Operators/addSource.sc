

+ Nil {
	addSource { | source, name = \player |
		^source.asPlayer (source, name);
	}

}

+ Function {
	asPlayer {
		^SynthPlayer (this)
	}
}


+ Event {
	asPlayer { | name |
	^PatternTaskPlayer ().addPlayerFromEvent (this, name);
	}
}

+ SynthPlayer {
	//	asPlayer
	
}
