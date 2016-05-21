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

	start { | args, target, action |
		args !? { this.addKeys (args) };
		target !? { this.addKeys ([\target, target]) };
		action !? { this.addKeys ([\action, acton]) };
		if (player.isPlaying.not) { player.play }
	}

	stop {
		player.stop;
	}

	isPlaying { ^player.isPlaying }

	addSource { | source |
		^source.addSelfToPatternPlayer (this);
	}
	
	addFilterEvent { | inEvent, name = \player |
		if (player.isPlaying){
			event.addFilterEvent (inEvent, name);
			player.addFilterEvent (inEvent, name)
		}{
			inEvent.addNotifierOneShot (player, \playing, {
				this.addFilterEvent (inEvent, name);
			})
		}
	}
	
	addFilterFunc { | function, name = \player |
		if (player.isPlaying) {
			event.addFilterFunc (function, name);
			player.addFilterEvent (function, name)
		}{
			function.addNotifierOneShot (player, \playing, {
				this.addFilterFunc (function, name);
			})
		}
	}

	removeFilter { | name = \player |
		if (player.isPlaying) {
		event.removeFilter (name);
			player.event.removeFilter (name)
		}{
			name.addNotifierOneShot (player, \playing, {
				this.removeFilter (name);
			})
		}
	}

	addKeys { | keyValuePairs |
		if (player.isPlaying) {
			event.addKeys (keyValuePairs);
			player.event.addKeys (keyValuePairs)
		}{
			keyValuePairs.addNotifierOneShot (player, \playing, {
				this.addKeys (keyValuePairs);
			})
		};
	}

	addEvent { | inEvent |
		if (player.isPlaying) {
			player addEvent: inEvent;
			inEvent keysValuesDo: { | key value |
				event [key] = value;
			}
		}{
			inEvent.addNotifierOneShot (player, \playing, {
				this.addEvent (inEvent);
			})
		}
	}
}