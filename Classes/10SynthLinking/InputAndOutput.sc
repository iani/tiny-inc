IOBus {
	var <parameter; // name of input parameter
	var <bus;
	var <synthLinks;

	*new { | synthLink, numChannels = 1, param = \in |
		// only create if the writer does not have one at param
		var dict, instance;
		dict = synthLink.perform (this.accessor);
		instance =  dict [param];
		instance ?? {
			instance = this.newCopyArgs (param, Bus.audio(synthLink.server, numChannels));
			dict [param] = instance
		};
		^instance;
	}
}

Input : IOBus {
	var <readerNode; // the SynthLink that has this input
	var <writers;   // set of Outputs that write to this input

	*accessor { \inputs }

	addWriter { | writer, out = \out |
		/* TODO: option to branch off existing bus */
		writer.setOutput (out, this);
	}
}

Output : IOBus {
	var <writerNode; // the SynthLink that has this output
	var <readers;   // set of Inputs that read from this output

	*accessor { \outputs }
	addReader { | reader, in = \in |
		reader.setInput (in, this);
	}
}
