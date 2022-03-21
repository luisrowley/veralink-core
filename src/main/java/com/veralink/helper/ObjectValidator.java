package com.veralink.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ObjectValidator {
	public static boolean isJSONValid(String test) {
	    try {
	    	new JSONObject(test);
	    } catch (JSONException objException) {
	    	try {
	            new JSONArray(test);
	        } catch (JSONException arrayException) {
	            return false;
	        }
	    }
	    return true;
	}
}
