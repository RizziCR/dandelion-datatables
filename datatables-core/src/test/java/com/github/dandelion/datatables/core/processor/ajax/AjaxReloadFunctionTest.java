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
package com.github.dandelion.datatables.core.processor.ajax;

import org.junit.Test;

import com.github.dandelion.core.DandelionException;
import com.github.dandelion.core.option.DefaultOptionProcessingContext;
import com.github.dandelion.core.option.Option;
import com.github.dandelion.core.option.OptionProcessingContext;
import com.github.dandelion.core.option.OptionProcessor;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.MapEntry;
import com.github.dandelion.datatables.core.extension.feature.AjaxReloadFeature;
import com.github.dandelion.datatables.core.option.DatatableOptions;
import com.github.dandelion.datatables.core.option.processor.ajax.AjaxReloadFunctionProcessor;
import com.github.dandelion.datatables.core.processor.TableProcessorBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AjaxReloadFunctionTest extends TableProcessorBaseTest {

	@Override
	public OptionProcessor getProcessor() {
		return new AjaxReloadFunctionProcessor(true);
	}

	@Test
	public void should_register_a_feature_when_a_reload_function_exists() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_RELOAD_FUNCTION, "myReloadFunction");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("myReloadFunction");
		assertThat(tableConfiguration.getInternalExtensions()).hasSize(1);
		assertThat(new AjaxReloadFeature()).isIn(tableConfiguration.getInternalExtensions());
	}

	@Test
	public void should_register_a_feature_and_multiple_bundles_when_a_reload_function_exists() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_RELOAD_FUNCTION,
				"bundle1, bundle2#myReloadFunction");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
		
		assertThat(entry.getValue()).isEqualTo("myReloadFunction");
		assertThat(tableConfiguration.getInternalExtensions()).hasSize(1);
		assertThat(new AjaxReloadFeature()).isIn(tableConfiguration.getInternalExtensions());
		assertThat(AssetRequestContext.get(tableConfiguration.getRequest()).getBundles(true)).contains("bundle1",
				"bundle2");
	}

	@Test(expected = DandelionException.class)
	public void should_throw_an_exception_when_the_function_is_malformatted() {
		entry = new MapEntry<Option<?>, Object>(DatatableOptions.AJAX_RELOAD_FUNCTION, "#myReloadFunction");
      OptionProcessingContext pc = new DefaultOptionProcessingContext(entry, request, processor.isBundleGraphUpdatable());
		processor.process(pc);
	}
}