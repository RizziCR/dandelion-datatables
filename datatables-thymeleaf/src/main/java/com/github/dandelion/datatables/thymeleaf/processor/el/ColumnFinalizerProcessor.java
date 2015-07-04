/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
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
package com.github.dandelion.datatables.thymeleaf.processor.el;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.IElementNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.Option;
import com.github.dandelion.datatables.core.util.ConfigUtils;
import com.github.dandelion.datatables.thymeleaf.dialect.DataTablesDialect;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractElProcessor;

public class ColumnFinalizerProcessor extends AbstractElProcessor {

   public ColumnFinalizerProcessor(IElementNameProcessorMatcher matcher) {
      super(matcher);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getPrecedence() {
      return 8005;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("unchecked")
   protected ProcessorResult doProcessElement(Arguments arguments, Element element, HttpServletRequest request,
         HttpServletResponse response, HtmlTable htmlTable) {

      Map<Option<?>, Object> stagingConf = (Map<Option<?>, Object>) arguments
            .getLocalVariable(DataTablesDialect.INTERNAL_BEAN_COLUMN_LOCAL_CONF);
      Map<Option<?>, Extension> stagingExt = (Map<Option<?>, Extension>) arguments
            .getLocalVariable(DataTablesDialect.INTERNAL_BEAN_COLUMN_LOCAL_EXT);

      // Get the TH content
      String content = null;
      if (element.getFirstChild() instanceof Text) {
         content = ((Text) element.getFirstChild()).getContent().trim();
      }
      else {
         content = element.getChildren().toString();
      }

      // Init a new header column
      HtmlColumn htmlColumn = new HtmlColumn(true, content);

      // Applies the staging configuration against the current column
      // configuration
      ConfigUtils.applyStagingOptionsAndExtensions(stagingConf, stagingExt, htmlColumn);
      ConfigUtils.processOptions(htmlColumn, htmlTable);

      // Add it to the table
      if (htmlTable != null) {
         htmlTable.getLastHeaderRow().addHeaderColumn(htmlColumn);
      }

      // Let's clean the TR attributes
      if (element.hasAttribute(DataTablesDialect.DIALECT_PREFIX + ":data")) {
         element.removeAttribute(DataTablesDialect.DIALECT_PREFIX + ":data");
      }

      return ProcessorResult.ok();
   }
}