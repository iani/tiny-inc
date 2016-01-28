+ Button {
	nodeWatcher { | nodePlayer, addStates = true |
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
}