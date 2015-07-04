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

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Feature used in accordance with the <code>sortType</code> column attribute to
 * help DataTables to configure the sort on a column.
 * </p>
 * <p>
 * See <a
 * href="http://datatables.net/plug-ins/sorting">http://datatables.net/plugins
 * /sorting</a> for an overview of all available sorting functions.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 * @see DatatableOptions#SORTTYPE
 */
public class SortingFeature extends AbstractExtension {

   public static final String SORTING_FEATURE_NAME = "sorting";

   @Override
   public String getExtensionName() {
      return SORTING_FEATURE_NAME;
   }

   @Override
   public void setup(HtmlTable table) {

      for (HtmlColumn column : table.getLastHeaderRow().getColumns()) {
         String sortTypeString = DatatableOptions.SORTTYPE.valueFrom(column.getColumnConfiguration());
         if (StringUtils.isNotBlank(sortTypeString)) {
            SortType sortType = SortType.findByName(sortTypeString);
            if (sortType != null) {
               addBundle(sortType.getBundle());
            }
         }
      }
   }
}