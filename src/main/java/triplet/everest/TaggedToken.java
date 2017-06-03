/**
 * Copyright (C) 2015 Saud Alashri - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Email: salashri at asu.edu
 */

package triplet.everest;

public class TaggedToken {
	String tag;
	String token;

	public TaggedToken(String newTag, String newToken) {
		tag = newTag;
		token = newToken;
	}
	
	public String toString() {
		return "[" + tag + "]" + token;
	}
}
