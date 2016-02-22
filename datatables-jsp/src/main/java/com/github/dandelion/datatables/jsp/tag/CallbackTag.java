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
package com.github.dandelion.datatables.jsp.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.github.dandelion.core.util.EnumUtils;
import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.option.Callback;
import com.github.dandelion.datatables.core.option.CallbackType;
import com.github.dandelion.datatables.core.util.ProcessorUtils;

/**
 * <p>
 * JSP tag used to add a DataTables callback.
 * </p>
 * <p>
 * Note that this tag will be processed only once, at the first iteration.
 * </p>
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>
 * &lt;script>
 *    function myInitCallback(oSettings, json) {
 *      // some stuff
 *    }
 * &lt;/script>
 * ...
 * &lt;datatables:table id="myTableId" data="${persons}">
 *    &lt;datatables:column title="Id" property="id" />
 *    &lt;datatables:column title="Firstname" property="firstName" />
 *    &lt;datatables:column title="LastName" property="lastName" />
 *    &lt;datatables:column title="City" property="address.town.name" />
 *    &lt;datatables:column title="Mail" property="mail" />
 *    &lt;datatables:callback type="init" function="myInitCallback" />
 * &lt;/datatables:table>
 * </pre>
 * 
 * @author Thibault Duchateau
 * @since 0.8.9
 */
public class CallbackTag extends TagSupport {

   private static final long serialVersionUID = -3453884184847355817L;

   /**
    * Type of callback.
    */
   private String type;

   /**
    * Function to be called as callback.
    */
   private String function;

   /**
    * The current request.
    */
   private HttpServletRequest request;

   @Override
   public int doStartTag() throws JspException {

      this.request = (HttpServletRequest) this.pageContext.getRequest();

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);
      if (parent != null) {
         return SKIP_BODY;
      }

      throw new JspException("The tag 'callback' must be inside the 'table' tag.");
   }

   @Override
   public int doEndTag() throws JspException {

      TableTag parent = (TableTag) findAncestorWithClass(this, TableTag.class);

      // The tag is evaluated only once, at the first iteration
      if (parent.isFirstIteration()) {

         CallbackType callbackType = null;
         try {
            callbackType = CallbackType.valueOf(this.type.toUpperCase().trim());
         }
         catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("'");
            sb.append(this.type);
            sb.append("' is not a valid callback type. Possible values are: ");
            sb.append(EnumUtils.printPossibleValuesOf(CallbackType.class));
            throw new JspException(sb.toString(), e);
         }

         this.function = ProcessorUtils.getValueAfterProcessingBundles(this.function, this.request);

         // The callback has already been registered
         if (parent.getTable().getTableConfiguration().hasCallback(callbackType)) {
            parent.getTable().getTableConfiguration().getCallback(callbackType)
                  .appendCode((callbackType.hasReturn() ? "return " : "") + this.function + "("
                        + StringUtils.join(callbackType.getArgs(), ",") + ");");
         }
         // The callback hasn't been registered yet
         else {
            parent.getTable().getTableConfiguration()
                  .registerCallback(new Callback(callbackType, (callbackType.hasReturn() ? "return " : "") + function
                        + "(" + StringUtils.join(callbackType.getArgs(), ",") + ");"));
         }
      }

      return EVAL_PAGE;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setFunction(String function) {
      this.function = function;
   }
}