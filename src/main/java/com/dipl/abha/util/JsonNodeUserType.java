package  com.dipl.abha.util;

import com.fasterxml.jackson.databind.JsonNode;


public class JsonNodeUserType extends JSONUserType<JsonNode> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonNodeUserType() {
		super(JsonNode.class);
	}
}
