/*

 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.core.extension.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.CallbackType;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Feature that is always enabled when server-side processing has been
 * activated.
 * </p>
 * <p>
 * Removing the fnAddjustColumnSizing will cause strange column's width at each
 * interaction with the table (paging, sorting, filtering ...)
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.8.2
 * @see DatatableOptions#AJAX_SERVERSIDE
 */
public class ServerSideFeature extends AbstractExtension {

   public static final String SERVER_SIDE_FEATURE_NAME = "serverSide";

   @Override
   public String getExtensionName() {
      return SERVER_SIDE_FEATURE_NAME;
   }

   @Override
   public void setup(HtmlTable table) {
      Map<String, String> ajaxParams = new HashMap<String, String>();
      ajaxParams.put("url", DatatableOptions.AJAX_SOURCE.valueFrom(table.getTableConfiguration().getOptions()));
      ajaxParams.put(DTConstants.DT_S_AJAXDATAPROP, "data");

      String extraParams = DatatableOptions.AJAX_PARAMS.valueFrom(table.getTableConfiguration().getOptions());
      if (StringUtils.isNotBlank(extraParams)) {
         StringBuilder paramObject = new StringBuilder("oTable_").append(table.getId()).append("_params");
         StringBuilder js = new StringBuilder();
         js.append(paramObject).append(".ajax = ");
         js.append(extraParams);
         js.append("();\n");
         for (Entry<String, String> ajaxParam : ajaxParams.entrySet()) {
            js.append(paramObject).append(".ajax.").append(ajaxParam.getKey()).append(" = '")
                  .append(ajaxParam.getValue());
            js.append("';\n");
         }
         appendToAfterStartDocumentReady(js.toString());
      }
      else {
         addParameter(DTConstants.DT_S_AJAX_SOURCE, ajaxParams);
      }

      addCallback(CallbackType.INIT, "oTable_" + table.getId() + ".columns.adjust().draw();");
   }
}