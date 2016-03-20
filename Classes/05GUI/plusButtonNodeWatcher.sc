+ Function {
	button { | states, action, server |
		^Button().nodePlayer(this, states, action, server)
	}
}
+ Button {
	simpleNodePlayer { | nodePlayer, addStates = true |
		nodePlayer.addListener(this, {
			{ this.value = 1; }.defer;
		}, {
			{ this.value = 0; }.defer;
		});
		if (nodePlayer.isPlaying) {
			this.value = 1;
		}{
			this.value = 0;
		};
	
		if (addStates) {
			this.states = [["start"], ["stop"]];
		}
	}

	nodePlayer { | source, states, action, server |
		source = source.asPlayer(server);
		source.addListener(this, {
			{ this.value = 1; }.defer;
		}, {
			{ this.value = 0; }.defer;
		});
		if (source.isPlaying) {
			this.value = 1;
		}{
			this.value = 0;
		};
		
		this.states = states ?? { [["start"], ["stop"]] };
		this.action = action ?? {{ | me |
			if (me.value > 0) {
				source.start;
			}{
				source.stop;
			}
		}}
	}
}