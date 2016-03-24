// INCOMPLETE!
+ Symbol {
	@> { | readerName, inputName = \in |
		^SynthLink(this).connect2Reader(SynthLink(readerName), \out, inputName, 1)
	}

	<@ { | readerName, inputName = \in |
		^SynthLink(readerName).connect2Writer(SynthLink(this), \out, inputName, 1)
	}

	@ { | outputName, numChannels = 1 |
		^(writer: SynthLink(this), outputName: outputName, numChannels: numChannels)
	}
}


+ Event {
	@> { | readerName, inputName = \in |
		^this[\writer].connect2Reader(
			SynthLink(readerName), this[\outputName], inputName, this[\numChannels]
		)
	}

	<@ {  | readerName, inputName = \in |
		^SynthLink(readerName).connect2Writer(
			this[\writer], this[\outputName], inputName, this[\numChannels]
		)
	}
}

