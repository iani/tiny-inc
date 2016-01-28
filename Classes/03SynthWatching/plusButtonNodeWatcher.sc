+ Button {
	nodeWatcher { | nodePlayer |
		nodePlayer.addListener(this, { this.value = 1 }, { this.value = 0 });
	}
}