LinkedBus {
	var <server;
	var <numChannels = 1;
	var <writers;
	var <readers;
	var <bus;

	*new { | server, numChannels = 1 |
		^this.newCopyArgs(server, numChannels).init;
	}

	init {
		this.makeBus;
		this.addNotifier(server, \booted, { this.makeBus });
	}

	makeBus {
		bus = Bus.audio(server, numChannels);
		
	}

	getReaders {
		// 		^readers collect: 
	}

	getWriters {

	}

}

BusLink {
	var <writer, <writerParam;
	var <reader, <readerParam;
	
	
}

BusCopyLink : BusLink {

}