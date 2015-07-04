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
package com.github.dandelion.datatables.core;

/**
 * <p>
 * All Dandelion bundles used in all the Dandelion-Datatables components.
 * </p>
 * 
 * @author Thibault Duchateau
 * @since 0.10.0
 */
public enum DatatableBundles {

   // Vendors
   DATATABLES("datatables"), 
   DATATABLES_COLREORDER("datatables-colreorder"), 
   DATATABLES_FIXEDHEADER("datatables-fixedheader"), 
   DATATABLES_SCROLLER("datatables-scroller"), 
   DATATABLES_RESPONSIVE("datatables-responsive"), 
   DATATABLES_BS2_RESPONSIVE("datatables-bootstrap2-responsive"),
   DATATABLES_BS3_RESPONSIVE("datatables-bootstrap3-responsive"),
   BOOTSTRAP_DATEPICKER("bootstrap-datepicker"),
   YADCF("yadcf"),
   JQUERY_DOWNLOAD("jquery-download"),

   // Dandelion
   DDL_DT("ddl-dt"),

   DDL_DT_AJAX_PIPELINING("ddl-dt-ajax-pipelining"),

   DDL_DT_THEME_BOOTSTRAP2("ddl-dt-theme-bootstrap2"),
   DDL_DT_THEME_BOOTSTRAP3("ddl-dt-theme-bootstrap3"), 
   DDL_DT_THEME_JQUERYUI("ddl-dt-theme-jqueryui"),
   DDL_DT_THEME_JQUERYUI_BLACKTIE("ddl-dt-theme-jqueryui-blacktie"),
   DDL_DT_THEME_JQUERYUI_BLITZER("ddl-dt-theme-jqueryui-blitzer"),
   DDL_DT_THEME_JQUERYUI_CUPERTINO("ddl-dt-theme-jqueryui-cupertino"),
   DDL_DT_THEME_JQUERYUI_DARKHIVE("ddl-dt-theme-jqueryui-darkhive"),
   DDL_DT_THEME_JQUERYUI_DOTLUV("ddl-dt-theme-jqueryui-dotluv"),
   DDL_DT_THEME_JQUERYUI_EGGPLANT("ddl-dt-theme-jqueryui-eggplant"),
   DDL_DT_THEME_JQUERYUI_EXCITEBIKE("ddl-dt-theme-jqueryui-excitebike"),
   DDL_DT_THEME_JQUERYUI_FLICK("ddl-dt-theme-jqueryui-flick"),
   DDL_DT_THEME_JQUERYUI_HOTSNEAKS("ddl-dt-theme-jqueryui-hotsneaks"), 
   DDL_DT_THEME_JQUERYUI_HUMANITY("ddl-dt-theme-jqueryui-humanity"), 
   DDL_DT_THEME_JQUERYUI_LEFROG("ddl-dt-theme-jqueryui-lefrog"), 
   DDL_DT_THEME_JQUERYUI_MINTCHOC("ddl-dt-theme-jqueryui-mintchoc"), 
   DDL_DT_THEME_JQUERYUI_OVERCAST("ddl-dt-theme-jqueryui-overcast"), 
   DDL_DT_THEME_JQUERYUI_PEPPERGRINDER("ddl-dt-theme-jqueryui-peppergrinder"), 
   DDL_DT_THEME_JQUERYUI_REDMOND("ddl-dt-theme-jqueryui-redmond"), 
   DDL_DT_THEME_JQUERYUI_SMOOTHNESS("ddl-dt-theme-jqueryui-smoothness"), 
   DDL_DT_THEME_JQUERYUI_SOUTHSTREET("ddl-dt-theme-jqueryui-southstreet"), 
   DDL_DT_THEME_JQUERYUI_START("ddl-dt-theme-jqueryui-start"), 
   DDL_DT_THEME_JQUERYUI_SUNNY("ddl-dt-theme-jqueryui-sunny"), 
   DDL_DT_THEME_JQUERYUI_SWANKYPURSE("ddl-dt-theme-jqueryui-swankypurse"), 
   DDL_DT_THEME_JQUERYUI_TRONTASTIC("ddl-dt-theme-jqueryui-trontastic"), 
   DDL_DT_THEME_JQUERYUI_UIDARKNESS("ddl-dt-theme-jqueryui-uidarkness"), 
   DDL_DT_THEME_JQUERYUI_UILIGHTNESS("ddl-dt-theme-jqueryui-uilightness"), 
   DDL_DT_THEME_JQUERYUI_VADER("ddl-dt-theme-jqueryui-vader"),

   DDL_DT_SORTING_ALT_STRING("ddl-dt-sorting-alt-string"), 
   DDL_DT_SORTING_ANTI_THE("ddl-dt-sorting-anti-the"), 
   DDL_DT_SORTING_CHINESE_STRING("ddl-dt-sorting-chinese-string"), 
   DDL_DT_SORTING_DATE_DE("ddl-dt-sorting-date-de"), 
   DDL_DT_SORTING_DATE_EU("ddl-dt-sorting-date-eu"), 
   DDL_DT_SORTING_DATE_EURO("ddl-dt-sorting-date-euro"), 
   DDL_DT_SORTING_DATE_UK("ddl-dt-sorting-date-uk"), 
   DDL_DT_SORTING_FILESIZE("ddl-dt-sorting-filesize"), 
   DDL_DT_SORTING_IP("ddl-dt-sorting-ip"), 
   DDL_DT_SORTING_NATURAL("ddl-dt-sorting-natural"), 
   DDL_DT_SORTING_PERSIAN("ddl-dt-sorting-persian"), 
   DDL_DT_SORTING_SCIENTIFIC("ddl-dt-sorting-scientific"), 
   DDL_DT_SORTING_SIGNED_NUM("ddl-dt-sorting-signed-num"), 
   DDL_DT_SORTING_TURKISH_STRING("ddl-dt-sorting-turkish-string"),

   DDL_DT_PAGING_INPUT("ddl-dt-paging-input"), 
   DDL_DT_PAGING_SCROLLING("ddl-dt-paging-scrolling"), 
   DDL_DT_PAGING_EXTJS("ddl-dt-paging-extjs"), 
   DDL_DT_PAGING_LISTBOX("ddl-dt-paging-listbox"), 
   DDL_DT_PAGING_BOOTSTRAP_SIMPLE("ddl-dt-paging-bootstrap-simple"), 
   DDL_DT_PAGING_BOOTSTRAP_FULL("ddl-dt-paging-bootstrap-full"), 
   DDL_DT_PAGING_BOOTSTRAP_FULL_NUMBERS("ddl-dt-paging-bootstrap-full-numbers");

   private String bundleName;

   private DatatableBundles(String bundleName) {
      this.bundleName = bundleName;
   }

   public String getBundleName() {
      return bundleName;
   }

   public void setBundleName(String bundleName) {
      this.bundleName = bundleName;
   }
}