pf// INCOMPLETE!
+ Symbol {
	<@ { | readerName, inputName = \in |
		^SynthLink(this).addReader(SynthLink(readerName), \out, inputName, 1)
	}

	@> { | readerName, inputName = \in |
		^SynthLink(readerName).addWriter(SynthLink(this), \out, inputName, 1)
	}

	@ { | outputName, numChannels = 1 |
		^(writer: SynthLink(this), outputName: outputName, numChannels: numChannels)
	}
}


+ Event {
	@> { | readerName, inputName = \in |
		^this[\writer].addReader(
			SynthLink(readerName), this[\outputName], inputName, this[\numChannels]
		)
	}

	<@ {  | readerName, inputName = \in |
		^SynthLink(readerName).addWriter(
			this[\writer], this[\outputName], inputName, this[\numChannels]
		)
	}
}

