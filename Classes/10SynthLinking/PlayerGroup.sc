PlayerGroup {
	// A PlayerGroup contains and handles all Groups that are used 
	// by all SynthLinks on one Server.
	var <server, <groups;
	*new { | server, rank = 0 |
		// Only one PlayerGroup per server
		server ?? { server = Server.default };
		^Registry(this, server, { this.newCopyArgs(server, []).init })
		.getGroup(rank);
	}

	init {
		ServerTree.add({ this.remakeGroups }, server);
	}

	remakeGroups {
		groups = { this.makeGroup } ! groups.size;
		SynthLink.all (server) do: _.resetGroup (groups);
		postf ("% made % Groups on %\n", this, groups.size, server);
	}

	makeGroup { ^Group(server, \addToTail) }

	getGroup { | rank |
		rank - groups.size + 1 max: 0 do: {
			groups = groups add: this.makeGroup
		};
		^groups[rank];
	}
}