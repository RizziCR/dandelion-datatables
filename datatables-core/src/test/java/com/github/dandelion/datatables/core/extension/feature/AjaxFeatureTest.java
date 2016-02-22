package com.github.dandelion.datatables.core.extension.feature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import com.github.dandelion.core.asset.generator.js.JsFunction;
import com.github.dandelion.core.web.AssetRequestContext;
import com.github.dandelion.datatables.core.extension.AbstractExtensionTest;
import com.github.dandelion.datatables.core.extension.Extension;
import com.github.dandelion.datatables.core.generator.DTConstants;
import com.github.dandelion.datatables.core.option.CallbackType;
import com.github.dandelion.datatables.core.option.DatatableOptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public class AjaxFeatureTest extends AbstractExtensionTest {

	@Test
	public void shoud_load_the_extension_with_the_given_ajax_source() {
		DatatableOptions.AJAX_SOURCE.setIn(table.getTableConfiguration().getOptions(), "/ajaxSource");
		
		extensionProcessor.process(new HashSet<Extension>(Arrays.asList(new AjaxFeature())));

		Map<String, String> ajaxParams = new HashMap<String, String>();
		ajaxParams.put("url", DatatableOptions.AJAX_SOURCE.valueFrom(table.getTableConfiguration().getOptions()));
		ajaxParams.put(DTConstants.DT_S_AJAXDATAPROP, "");
		
		assertThat(AssetRequestContext.get(table.getTableConfiguration().getRequest()).getBundles(true)).hasSize(0);
		assertThat(mainConfig).contains(
				entry(DTConstants.DT_DEFER_RENDER, true),
				entry(DTConstants.DT_S_AJAX_SOURCE, ajaxParams));
		assertThat(mainConfig.get(CallbackType.INIT.getName()).toString())
		.isEqualTo(new JsFunction("oTable_fakeId.columns.adjust().draw();", CallbackType.INIT.getArgs()).toString());
	}
}
