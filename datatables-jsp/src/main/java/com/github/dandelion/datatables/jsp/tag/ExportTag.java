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
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dandelion.core.util.EnumUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.core.util.UrlUtils;
import com.github.dandelion.core.web.WebConstants;
import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.core.export.ExportConf.Orientation;
import com.github.dandelion.datatables.core.export.ExportUtils;
import com.github.dandelion.datatables.core.export.HttpMethod;

/**
 * <p>
 * JSP tag used to configure an export type in the current table.
 * 
 * <p>
 * Note that this tag will be processed only once, at the first iteration.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * &lt;datatables:table id="myTableId" data="${persons}" row="person" export="xls">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="FirstName" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" display="html">
 *       &lt;a href="mailto:${person.mail}">${person.mail}&lt;/a>
 *    &lt;/datatables:column>
 *    &lt;datatables:column title="Mail" property="mail" display="xls" />
 *    &lt;datatables:export type="xls" autoSize="true" cssClass="btn" label="XLS export!" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @see ExportConf
 */
public class ExportTag extends TagSupport {
   private static final long serialVersionUID = -3453884184847355817L;

   // Logger
   private static Logger logger = LoggerFactory.getLogger(ExportTag.class);

   /**
    * Tag attributes
    */
   private String fileName;
   private String fileExtension;
   private String type;
   private String label;
   private String cssStyle;
   private String cssClass;
   private Boolean includeHeader;
   private Boolean autoSize;
   private String url;
   private String method;
   private String orientation;
   private String mimeType;
   private boolean escapeXml = true; // Whether XML characters should be escaped

   /**
    * {@inheritDoc}
    */
   public int doStartTag() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
      if (parent != null) {
         return SKIP_BODY;
      }

      throw new JspException("The tag 'export' must be inside the 'table' tag.");
   }

   /**
    * {@inheritDoc}
    */
   public int doEndTag() throws JspException {

      HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
      HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

      AbstractTableTag parent = (AbstractTableTag) findAncestorWithClass(this, AbstractTableTag.class);

      // The tag is evaluated only once, at the first iteration
      if (parent.isFirstIteration()) {

         String format = type.toLowerCase().trim();

         // Export URL build
         ExportConf conf = null;

         if (parent.getTable().getTableConfiguration().getExportConfiguration().get(format) != null) {
            conf = parent.getTable().getTableConfiguration().getExportConfiguration().get(format);
         }
         else {
            conf = new ExportConf(format);
            parent.getTable().getTableConfiguration().getExportConfiguration().put(format, conf);
         }

         // Default mode (export using filter)
         StringBuilder exportUrl = null;
         if (StringUtils.isBlank(url)) {
            exportUrl = UrlUtils.getCurrentUri(request);
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_TYPE, "f");
            conf.setHasCustomUrl(false);
         }
         // Custom mode (export using controller)
         else {
            exportUrl = new StringBuilder(url.trim());
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_TYPE, "c");
            conf.setHasCustomUrl(true);
         }

         if (StringUtils.isNotBlank(fileName)) {
            conf.setFileName(fileName.trim());
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_NAME, this.fileName);
         }

         if (StringUtils.isNotBlank(fileExtension)) {
            conf.setFileExtension(fileExtension);
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_EXTENSION, this.fileExtension);
         }

         if (StringUtils.isNotBlank(label)) {
            conf.setLabel(StringUtils.escape(this.escapeXml, this.label.trim()));
         }
         if (StringUtils.isNotBlank(cssClass)) {
            conf.setCssClass(new StringBuilder(cssClass.trim()));
         }
         if (StringUtils.isNotBlank(cssStyle)) {
            conf.setCssStyle(new StringBuilder(cssStyle.trim()));
         }

         if (StringUtils.isNotBlank(method)) {
            HttpMethod httpMethod = null;
            try {
               httpMethod = HttpMethod.valueOf(this.method.toUpperCase().trim());
            }
            catch (IllegalArgumentException e) {
               StringBuilder sb = new StringBuilder();
               sb.append("'");
               sb.append(this.method);
               sb.append("' is not a valid HTTP method. Possible values are: ");
               sb.append(EnumUtils.printPossibleValuesOf(HttpMethod.class));
               throw new JspException(sb.toString());
            }

            conf.setMethod(httpMethod);
         }

         if (StringUtils.isNotBlank(orientation)) {
            Orientation orientationEnum = null;
            try {
               orientationEnum = Orientation.valueOf(this.orientation.toUpperCase().trim());
            }
            catch (IllegalArgumentException e) {
               StringBuilder sb = new StringBuilder();
               sb.append("'");
               sb.append(this.orientation);
               sb.append("' is not a valid orientation. Possible values are: ");
               sb.append(EnumUtils.printPossibleValuesOf(Orientation.class));
               throw new JspException(sb.toString());
            }

            conf.setOrientation(orientationEnum);
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_ORIENTATION, orientation);
         }

         if (StringUtils.isNotBlank(mimeType)) {
            conf.setMimeType(mimeType.trim());
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_MIME_TYPE, mimeType.trim());
         }

         if (includeHeader != null) {
            conf.setIncludeHeader(includeHeader);
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_HEADER, includeHeader);
         }

         if (autoSize != null) {
            conf.setAutoSize(autoSize);
            UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_AUTOSIZE, autoSize);
         }

         // Finalizes the export URL
         UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_ID, parent.getTable().getId());
         UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_FORMAT, format);
         UrlUtils.addParameter(exportUrl, ExportUtils.DDL_DT_REQUESTPARAM_EXPORT_IN_PROGRESS, "y");
         UrlUtils.addParameter(exportUrl, WebConstants.DANDELION_ASSET_FILTER_STATE, false);
         conf.setUrl(UrlUtils.getProcessedUrl(exportUrl, request, response));

         logger.debug("Export configuration for the type {} has been updated", format);
      }

      return EVAL_PAGE;
   }

   public void setFileName(String fileName) {
      this.fileName = fileName;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public void setCssStyle(String cssStyle) {
      this.cssStyle = cssStyle;
   }

   public void setCssClass(String cssClass) {
      this.cssClass = cssClass;
   }

   public void setIncludeHeader(Boolean includeHeader) {
      this.includeHeader = includeHeader;
   }

   public void setAutoSize(Boolean autoSize) {
      this.autoSize = autoSize;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public void setOrientation(String orientation) {
      this.orientation = orientation;
   }

   public void setFileExtension(String fileExtension) {
      this.fileExtension = fileExtension;
   }

   public void setMimeType(String mimeType) {
      this.mimeType = mimeType;
   }

   public void setEscapeXml(boolean escapeXml) {
      this.escapeXml = escapeXml;
   }
}