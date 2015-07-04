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
package com.github.dandelion.datatables.core.export;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.html.HtmlColumn;
import com.github.dandelion.datatables.core.html.HtmlTable;
import com.github.dandelion.datatables.core.option.DatatableOptions;

/**
 * <p>
 * Builder used to create instances of {@link HtmlTable}. This builder is mainly
 * used as an export utility and for testing.
 * </p>
 * <p>
 * For example, considering the following simple {@code Person} class:
 * </p>
 * 
 * <pre>
 * public class Person {
 * 	private Long id;
 * 	private String firstName;
 * 	private String lastName;
 * 	private String mail;
 * 	private Date birthDate;
 * 
 * 	// Accessors...
 * }
 * </pre>
 * <p>
 * The builder allows to create fully configured instance of {@link HtmlTable}
 * as follows:
 * </p>
 * 
 * <pre>
 * HtmlTable table = new HtmlTableBuilder&lt;Person&gt;().newBuilder(&quot;yourTableId&quot;, persons, request).column()
 * 		.fillWithProperty(&quot;id&quot;).title(&quot;Id&quot;).column().fillWithProperty(&quot;firstName&quot;).title(&quot;Firtname&quot;).column()
 * 		.fillWithProperty(&quot;lastName&quot;).title(&quot;Lastname&quot;).column().fillWithProperty(&quot;mail&quot;).title(&quot;Mail&quot;).column()
 * 		.fillWithProperty(&quot;birthDate&quot;, &quot;{0,date,dd-MM-yyyy}&quot;).title(&quot;BirthDate&quot;).build();
 * </pre>
 * <p>
 * where:
 * </p>
 * <ul>
 * <li>{@code yourTableId} is the HTML id that has be assigned to the
 * {@code table} tag</li>
 * <li>{@code persons} is a collection of {@code Person}</li>
 * <li>{@code request} is the current {@link HttpServletRequest}</li>
 * </ul>
 * 
 * @param <T>
 *            Type of the data used to build the table.
 * 
 * @author Thibault Duchateau
 * @since 0.9.0
 */
public class HtmlTableBuilder<T> {

   private static Logger logger = LoggerFactory.getLogger(HtmlTableBuilder.class);

   public ColumnStep newBuilder(String id, List<T> data, HttpServletRequest request) {
      return new Steps<T>(id, data, request);
   }

   public ColumnStep newBuilder(String id, List<T> data, HttpServletRequest request, ExportConf exportConf) {
      return new Steps<T>(id, data, request, exportConf);
   }

   public static interface ColumnStep {
      FirstContentStep column();
   }

   public static interface FirstContentStep {
      SecondContentStep fillWithProperty(String propertyName);

      SecondContentStep fillWithProperty(String propertyName, String pattern);

      SecondContentStep fillWithProperty(String propertyName, String pattern, String defaultContent);

      SecondContentStep fillWith(String content);
   }

   public static interface SecondContentStep {
      SecondContentStep andProperty(String propertyName);

      SecondContentStep andProperty(String propertyName, String pattern);

      SecondContentStep andProperty(String propertyName, String pattern, String defaultContent);

      SecondContentStep and(String content);

      BuildStep title(String title);
   }

   public static interface TitleStep extends ColumnStep {
      ColumnStep title(String title);
   }

   public static interface BuildStep {
      HtmlTable build();

      FirstContentStep column();
   }

   private static class Steps<T> implements ColumnStep, FirstContentStep, SecondContentStep, BuildStep {

      private String id;
      private List<T> data;
      private LinkedList<HtmlColumn> headerColumns = new LinkedList<HtmlColumn>();
      private HttpServletRequest request;
      private HttpServletResponse response;
      private ExportConf exportConf;

      public Steps(String id, List<T> data, HttpServletRequest request) {
         this(id, data, request, null);
      }

      public Steps(String id, List<T> data, HttpServletRequest request, ExportConf exportConf) {
         this.id = id;
         this.data = data;
         this.request = request;
         this.exportConf = new ExportConf(request);
         if (exportConf != null) {
            this.exportConf.mergeWith(exportConf);
         }
      }

      // Table configuration

      public Steps<T> column() {
         HtmlColumn column = new HtmlColumn(true, "");
         headerColumns.add(column);
         return this;
      }

      public Steps<T> title(String title) {
         headerColumns.getLast().getColumnConfiguration().getConfigurations().put(DatatableOptions.TITLE, title);
         return this;
      }

      /**
       * Add a new column to the table and complete it using the passed
       * property. Convenient if you need to display a single property in the
       * column.
       * 
       * @param property
       *           name of the (possibly nested) property of the bean which is
       *           part of the collection being iterated on.
       */
      public Steps<T> fillWithProperty(String property) {
         return fillWithProperty(property, null, "");
      }

      public Steps<T> fillWithProperty(String property, String pattern) {
         return fillWithProperty(property, pattern, "");
      }

      public Steps<T> fillWithProperty(String property, String pattern, String defaultContent) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(property, pattern, "", defaultContent));
         return this;
      }

      public Steps<T> fillWith(String content) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(null, null, content, null));
         return this;
      }

      public Steps<T> andProperty(String property) {
         return andProperty(property, null, "");
      }

      public Steps<T> andProperty(String property, String pattern) {
         return andProperty(property, pattern, "");
      }

      public Steps<T> andProperty(String property, String pattern, String defaultContent) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(property, pattern, null, defaultContent));

         return this;
      }

      public Steps<T> and(String content) {
         if (headerColumns.getLast().getColumnConfiguration().getColumnElements() == null) {
            headerColumns.getLast().getColumnConfiguration().setColumnElements(new ArrayList<ColumnElement>());
         }
         headerColumns.getLast().getColumnConfiguration().getColumnElements()
               .add(new ColumnElement(null, null, content, null));
         return this;
      }

      public HtmlTable build() {
         HtmlTable table = new HtmlTable(id, request, response);

         table.getTableConfiguration().getExportConfiguration().put(exportConf.getFormat(), exportConf);

         if (data != null && data.size() > 0) {
            DatatableOptions.INTERNAL_OBJECTTYPE.setIn(table.getTableConfiguration(), data.get(0).getClass()
                  .getSimpleName());
         }
         else {
            DatatableOptions.INTERNAL_OBJECTTYPE.setIn(table.getTableConfiguration(), "???");
         }

         table.addHeaderRow();

         for (HtmlColumn column : headerColumns) {
            String title = DatatableOptions.TITLE.valueFrom(column.getColumnConfiguration());
            if (StringUtils.isNotBlank(title)) {
               column.setContent(new StringBuilder(title));
            }
            else {
               column.setContent(new StringBuilder(""));
            }
            table.getLastHeaderRow().addColumn(column);
         }

         if (data != null) {

            for (T o : data) {

               table.addRow();
               for (HtmlColumn column : headerColumns) {

                  String content = "";
                  for (ColumnElement columnElement : column.getColumnConfiguration().getColumnElements()) {

                     if (StringUtils.isNotBlank(columnElement.getPropertyName())) {
                        try {
                           Object tmpObject = PropertyUtils
                                 .getNestedProperty(o, columnElement.getPropertyName().trim());

                           if (StringUtils.isNotBlank(columnElement.getPattern())) {
                              MessageFormat messageFormat = new MessageFormat(columnElement.getPattern());
                              content += messageFormat.format(new Object[] { tmpObject });
                           }
                           else {
                              content += String.valueOf(tmpObject);
                           }
                        }
                        catch (Exception e) {
                           logger.warn("Something went wrong with the property {}. Check that an accessor method for this property exists in the bean.");
                           content += columnElement.getDefaultValue();
                        }
                     }
                     else if (columnElement.getContent() != null) {
                        content += columnElement.getContent();
                     }
                     else {
                        content += columnElement.getDefaultValue();
                     }
                  }

                  table.getLastBodyRow().addColumn(String.valueOf(content));
               }
            }
         }

         return table;
      }
   }
}