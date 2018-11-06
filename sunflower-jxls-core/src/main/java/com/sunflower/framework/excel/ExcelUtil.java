package com.sunflower.framework.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jxls.common.Context;
import org.jxls.jdbc.JdbcHelper;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.ReaderConfig;
import org.jxls.reader.XLSReadStatus;
import org.jxls.reader.XLSReader;
import org.jxls.util.JxlsHelper;

public class ExcelUtil {

	private static final String url = "jdbc:mysql://rm-uf66f3bf757glz761o.mysql.rds.aliyuncs.com:3306?useUnicode=true&characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull";

	private static final String username = "select_user";

	private static final String password = "Xuxue_select";

	public ExcelUtil() {
	}

	public static OutputStream jdbcWrite(InputStream is, OutputStream os)
			throws IOException, SQLException {
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://rm-uf66f3bf757glz761o.mysql.rds.aliyuncs.com:3306?useUnicode=true&characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull",
				"select_user", "Xuxue_select");
		Context context = new Context();
		JdbcHelper jdbcHelper = new JdbcHelper(conn);
		context.putVar("jdbc", jdbcHelper);
		JxlsHelper.getInstance().processTemplate(is, os, context);
		return os;
	}

	public static Map<String, Object> read(String templateName, Map<String, Object> map,
			InputStream in) throws Exception {
		InputStream inputXML = ExcelUtil.class.getClassLoader()
				.getResourceAsStream("template/" + templateName);
		XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
		ReaderConfig instance = ReaderConfig.getInstance();
		instance.setSkipErrors(true);
		Map<String, Object> beans = new HashMap();
		Set<String> set = map.keySet();

		for (String key : set) {
			beans.put(key, map.get(key));
		}

		XLSReadStatus readStatus = mainReader.read(in, beans);
		if (!readStatus.isStatusOK()) {
			return Collections.emptyMap();
		}
		else {
			Map<String, Object> result = new HashMap();
			Set<String> set2 = map.keySet();

			for (String key : set2) {
				result.put(key, map.get(key));
			}
			return result;
		}
	}

}
