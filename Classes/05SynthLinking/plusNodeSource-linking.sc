+ NodePlayer {
	
	canAddReader { | reader |
		^this !== reader and: {
			reader.allReadersRecursively.includes(this).not
		};
	}

	allReadersRecursively { | readers |
		var allReaders;
		readers ?? { readers = Set() };
		allReaders = this.allReaders;
		if (allReaders.size == 0) {
			^readers;
		}{
			readers addAll: allReaders;
			allReaders do: _.allReadersRecursively(readers);
			^readers;
		};
	}
	
	allReaders { ^outputs collect: _.getReaders; }
	
	addWriter { | writer inParam = \in outParam = \out numChannels = 1 |
		writer.addReader(this, inParam, outParam, numChannels);
	}

	addReader { | reader inParam = \in outParam = \out numChannels = 1 |
		var busLink, writerBus, readerBus;
		if (this.canAddReader(reader).not) {
			postf(
				"% cannot add % as reader: No cycles permitted\n",
				this, reader
			);
			^this;
		};
		writerBus = this.getOutput(outParam);
		readerBus = reader.getInput(inParam);
		case
		{ writerBus.isNil and: { readerBus.isNil } } {
			writerBus = LinkedBus(numChannels);
			this.setOutput(writerBus, outParam);
			reader.setInput(writerBus, inParam);
		}
		{ writerBus.isNil and: { readerBus.notNil } } {
			
		}
		{ writerBus.notNil and: { readerBus.isNil } } {

		}
		{ writerBus.notNil and: { readerBus.notNil } } {

		}
		// TODO: complete this
	}

	getOutput { | param |  ^outputs[param] }
	getInput { | param |  ^inputs[param] }

	setOutput { | param |
		//		outputs[param] = 
	}

	setInput { | param |

	}
	
	removeWriter { | param, writer |

	}

	removeReader { | param, reader |

	}

}