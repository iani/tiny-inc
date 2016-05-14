// 14 May 2016 15:20


+ Object {
	// make available to UGen, PureUGen, and Array
	out { | outName = \out outValue = 0 |
		^Out.ar (outName.kr (outValue), this)
	}
	
	duh { | gateName = \gate gateValue = 1 fadeTimeName = \fadeTime fadeTimeValue = 0.02 |
		// TODO: add levelScale arg?
		^this // * GraphBuilder.makeFadeEnv
		*
		EnvGen.kr (
			Env.asr,
			gateName.kr (gateValue),
			timeScale: fadeTimeName.kr (fadeTimeValue),
			doneAction: 2 // TODO: Make 2 an arg value
		);
		
	}

	duho { // TODO: add args
		^this.duh.out;
	}

	duuh { | posName = \pos posValue = 0 |
		^Pan2.ar (this, posName.kr (posValue)).duh
	}

	duuho { // TODO: Add args
		^this.duuh.out;
	}
}