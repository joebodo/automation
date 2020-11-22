/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package automation;

import static java.util.Arrays.asList;
import static org.apache.http.HttpStatus.SC_OK;
import static org.jsoup.Jsoup.parse;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicNameValuePair;
import org.gradle.api.GradleException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HybrisHacHttpClient extends AbstractHacClient {

	private static final String STACKTRACE = "stacktraceText";

	public Map executeGroovyScript(final Project project, final String content, final boolean isCommitMode)
			throws GradleException {

		final List<BasicNameValuePair> params = asList(new BasicNameValuePair("scriptType", "groovy"),
				new BasicNameValuePair("commit", String.valueOf(isCommitMode)),
				new BasicNameValuePair("script", content));

		final String actionUrl = getHostHacURL(project) + "/console/scripting/execute";

		final HttpResponse response = post(project, actionUrl, params, true);
		final StatusLine statusLine = response.getStatusLine();

		if (statusLine.getStatusCode() != SC_OK || response.getEntity() == null) {
			throw new GradleException("[" + statusLine.getStatusCode() + "] " + statusLine.getReasonPhrase());
		}

		try {
			final Document document = parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");

			final Elements fsResultStatus = document.getElementsByTag("body");
			if (fsResultStatus == null) {
				throw new GradleException("No data in response");
			}
			return new Gson().fromJson(fsResultStatus.text(), HashMap.class);

		} catch (final IOException e) {
			throw new GradleException(actionUrl, e);
		}
	}
}
