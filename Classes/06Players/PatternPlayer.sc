/*  6 May 2016 21:33 */

PatternPlayer {
	var <event;
	var <player;

	*new { | event |
		^this.newCopyArgs (event).init;
	}

	init {
		player = EventPattern (event).asEventStreamPlayer;
	}

	play {
		if (player.isPlaying.not) { player.play }
	}

	stop {
		player.stop;
	}

	isPlaying { ^player.isPlaying }
}