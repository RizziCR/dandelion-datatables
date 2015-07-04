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
package com.github.dandelion.datatables.core.extension.plugin;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.Parameter;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Java implementation of the DataTables Scroller plugin.
 * </p>
 * 
 * @author Thibault Duchateau
 */
public class ScrollerPlugin extends AbstractExtension {

   public static final String SCROLLER_PLUGIN_NAME = "scroller";

   @Override
   public String getExtensionName() {
      return SCROLLER_PLUGIN_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      addBundle(DatatableBundles.DATATABLES_SCROLLER);

      String dom = DatatableOptions.FEATURE_DOM.valueFrom(table.getTableConfiguration());
      Boolean jqueryUiEnabled = DatatableOptions.FEATURE_JQUERYUI.valueFrom(table.getTableConfiguration());

      if (StringUtils.isNotBlank(dom)) {
         addParameter(DTConstants.DT_DOM, "S", Parameter.Mode.APPEND);
      }
      else {
         if (jqueryUiEnabled != null && jqueryUiEnabled) {
            addParameter(DTConstants.DT_DOM, "<\"H\"lfr>t<\"F\"ip>S");
         }
         else {
            addParameter(DTConstants.DT_DOM, "frtiS");
         }
      }
   }
}