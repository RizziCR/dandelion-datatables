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
package com.github.dandelion.datatables.core.extension.theme;

import com.github.dandelion.core.asset.generator.js.JsSnippet;
import com.github.dandelion.datatables.core.DatatableBundles;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.extension.feature.PagingType;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Bootstrap v2 DataTables theme.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.7.1
 * @see DatatableOptions#CSS_THEME
 * @see DatatableOptions#CSS_THEMEOPTION
 */
public class Bootstrap2Theme extends AbstractExtension {

   @Override
   public String getExtensionName() {
      return "bootstrap2";
   }

   @Override
   public void setup(HtmlTable table) {

      addBundle(DatatableBundles.DDL_DT_THEME_BOOTSTRAP2);

      Boolean paging = DatatableOptions.FEATURE_PAGEABLE.valueFrom(table.getTableConfiguration());
      if (paging != null && paging == true) {
         addBundle(DatatableBundles.DDL_DT_PAGING_BOOTSTRAP_SIMPLE);
         if (DatatableOptions.FEATURE_PAGINGTYPE.valueFrom(table.getTableConfiguration()) == null) {
            addParameter(DTConstants.DT_PAGINGTYPE, PagingType.BOOTSTRAP_SIMPLE.toString());
         }
      }

      addParameter(DTConstants.DT_AS_STRIPE_CLASSES, new JsSnippet("[]"));
   }
}