AbstractSource {
	var <source;
	*new { | source ... args |
		^this.newCopyArgs(source).init(*args);
	}
}