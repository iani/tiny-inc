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

	duho { | gateName = \gate gateValue = 1 fadeTimeName = \fadeTime fadeTimeValue = 0.02 outName = \out outValue = 0 | 
		^this.duh (gateName, gateValue, fadeTimeName, fadeTimeValue)
		.out (outName, outValue);
	}

	duuh { | gateName = \gate gateValue = 1 fadeTimeName = \fadeTime fadeTimeValue = 0.02 posName = \pos posValue = 0 |
		^Pan2.ar (this, posName.kr (posValue)).duh (gateName, gateValue, fadeTimeName, fadeTimeValue)
	}

	duuho { | gateName = \gate gateValue = 1 fadeTimeName = \fadeTime fadeTimeValue = 0.02 posName = \pos posValue = 0 outName = \out outValue = 0 |
		// TODO: Add args
		^this.duuh (gateName, gateValue, fadeTimeName, fadeTimeValue).out (outName, outValue);
	}
}