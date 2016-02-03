/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nutch.index.solrwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.hadoop.mapred.JobConf;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.net.MalformedURLException;

public class SolrUtils {

  public static Logger LOG = LoggerFactory.getLogger(SolrUtils.class);

  public static HttpSolrClient getCommonsHttpSolrServer(JobConf job)
      throws MalformedURLException {
	  HttpClientBuilder client = HttpClientBuilder.create();
//
//    // Check for username/password
//    if (job.getBoolean(SolrConstants.USE_AUTH, false)) {
//      String username = job.get(SolrConstants.USERNAME);
//
//      LOG.info("Authenticating as: " + username);
//
//      AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT,
//          AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
//
//      client.getState().setCredentials(
//          scope,
//          new UsernamePasswordCredentials(username, job
//              .get(SolrConstants.PASSWORD)));
//
//      HttpClientParams params = client.getParams();
//      params.setAuthenticationPreemptive(true);

//      client.setParams(params);
//    }

    String serverURL = job.get(SolrConstants.SERVER_URL);
    final HttpSolrClient hsc = new HttpSolrClient(serverURL,client.build());
    return hsc;
  }

  public static String stripNonCharCodepoints(String input) {
    StringBuilder retval = new StringBuilder();
    char ch;

    for (int i = 0; i < input.length(); i++) {
      ch = input.charAt(i);

      // Strip all non-characters
      // http://unicode.org/cldr/utility/list-unicodeset.jsp?a=[:Noncharacter_Code_Point=True:]
      // and non-printable control characters except tabulator, new line and
      // carriage return
      if (ch % 0x10000 != 0xffff && // 0xffff - 0x10ffff range step 0x10000
          ch % 0x10000 != 0xfffe && // 0xfffe - 0x10fffe range
          (ch <= 0xfdd0 || ch >= 0xfdef) && // 0xfdd0 - 0xfdef
          (ch > 0x1F || ch == 0x9 || ch == 0xa || ch == 0xd)) {

        retval.append(ch);
      }
    }

    return retval.toString();
  }
}
