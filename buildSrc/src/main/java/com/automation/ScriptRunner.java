package com.automation;

import com.sap.hybris.hac.HybrisAdministrationConsole;
import com.sap.hybris.hac.scripting.Script;
import com.sap.hybris.hac.scripting.ScriptResult;

class ScriptRunner {

	public static ScriptResult execute(HybrisAdministrationConsole hac, Script script) {

		try {
			return hac.scripting().execute(script);

		} catch (org.springframework.web.client.HttpClientErrorException e) {
			// 404
			System.err.println("Error communication with hac - check hac_settings in gradle.properties");
			throw e;

		} catch (org.springframework.web.client.ResourceAccessException e) {
			// connection error
			System.err.println("Server not running or invalid hac url - check hac_settings in gradle.properties");
			throw e;
		}
	}
}
