package com.sunflower.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ExcelUtil {

	private ExcelUtil() {
	}

	public static Map<String, Object> read(String templateName, Map<String, Object> map,
			InputStream in) throws IOException, SAXException, InvalidFormatException {
		InputStream inputXML = ExcelUtil.class.getClassLoader()
				.getResourceAsStream("template/" + templateName);
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		ReaderConfig instance = ReaderConfig.getInstance();
		instance.setSkipErrors(true);
		Map<String, Object> beans = new HashMap<>(map.size() * 2);
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			beans.put(entry.getKey(), entry.getValue());
		}

		XLSReadStatus readStatus = mainReader.read(in, beans);
		if (!readStatus.isStatusOK()) {
			return Collections.emptyMap();
		}
		else {
			Map<String, Object> result = new HashMap<>(map.size() * 2);

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				result.put(entry.getKey(), entry.getValue());
			}
			return result;
		}
	}

}
