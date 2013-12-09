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
package com.github.dandelion.datatables.thymeleaf.processor.attr.export;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.ProcessorResult;

import com.github.dandelion.datatables.core.export.ExportConf;
import com.github.dandelion.datatables.thymeleaf.processor.AbstractTableAttrProcessor;
import com.github.dandelion.datatables.thymeleaf.util.Utils;

/**
 * Attribute processor applied to the <code>tbody</code> tag for the following
 * attributes :
 * <ul>
 * <li>dt:csv:url</li>
 * <li>dt:xml:url</li>
 * <li>dt:xls:url</li>
 * <li>dt:xlsx:url</li>
 * <li>dt:pdf:url</li>
 * </ul>
 * 
 * @author Thibault Duchateau
 * @since 0.8.13
 */
public class TheadExportLinkUrlAttrProcessor extends AbstractTableAttrProcessor {

	public TheadExportLinkUrlAttrProcessor(IAttributeNameProcessorMatcher matcher) {
		super(matcher);
	}

	@Override
	public int getPrecedence() {
		return 7999;
	}

	@Override
	protected ProcessorResult doProcessAttribute(Arguments arguments, Element element, String attributeName) {

		String attrValue = Utils.parseElementAttribute(arguments, element.getAttributeValue(attributeName), null, String.class);
		String exportFormat = attributeName.split(":")[1].toLowerCase().trim();
		
		ExportConf conf = null;
		
		if (table.getTableConfiguration().getExportConfiguration().get(exportFormat) != null) {
			conf = table.getTableConfiguration().getExportConfiguration().get(exportFormat);
		}
		else{
			conf = new ExportConf(exportFormat);
			table.getTableConfiguration().getExportConfiguration().put(exportFormat, conf);
		}
		
		conf.setCustom(true);
		conf.setUrl(attrValue);
			
		return ProcessorResult.ok();
	}
}