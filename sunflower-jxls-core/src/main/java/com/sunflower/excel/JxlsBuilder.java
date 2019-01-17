package com.sunflower.excel;

import com.sunflower.excel.command.ImageCommand;
import com.sunflower.excel.command.KeepCommand;
import com.sunflower.excel.command.MergeCommand;
import org.jxls.area.Area;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.command.GridCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.formula.FormulaProcessor;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生成excel辅助类
 * </p>
 *
 * @Author sunflower
 * @Date 2018/1/22
 */
public abstract class JxlsBuilder {

	static {
		// 注册 jx 命令
		XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
		XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
		XlsCommentAreaBuilder.addCommandMapping("keep", KeepCommand.class);
		XlsCommentAreaBuilder.addCommandMapping("grid", GridCommand.class);
	}

	private JxlsHelper jxlsHelper = JxlsHelper.getInstance();

	private Transformer transformer;

	private Context context;

	private InputStream in;

	private OutputStream out;

	private File inFile;

	private File outFile;

	private Map<String, Object> funcs;

	private String imageRoot = JxlsConfig.getImageRoot();

	private boolean ignoreImageMiss = false;

	private String[] removeSheetNames;

	private JxlsBuilder() {
		context = new Context();
		funcs = new HashMap<>();
		funcs.put("jxu", JxlsUtil.me());
	}

	private JxlsBuilder(InputStream in) {
		this();
		this.in = in;
	}

	private JxlsBuilder(File inFile) {
		this();
		if (!inFile.exists()) {
			throw new IllegalArgumentException("模板文件不存在：" + inFile.getAbsolutePath());
		}
		if (!inFile.getName().toLowerCase().endsWith("xls")
				&& !inFile.getName().toLowerCase().endsWith("xlsx")) {
			throw new IllegalArgumentException("不支持非excel文件：" + inFile.getName());
		}
		this.inFile = inFile;

	}

	/**
	 * @param in 模板文件流
	 * @return
	 */
	public static JxlsBuilder getBuilder(InputStream in) {
		return new AbstractJxlsBuilderImpl(in);
	}

	/**
	 * @param templateFile 模板文件地址
	 * @return
	 */
	public static JxlsBuilder getBuilder(File templateFile) {
		return new AbstractJxlsBuilderImpl(templateFile);
	}

	/**
	 * @param filePath 模板文件路径，可以是绝对路径，也可以是模板存放目录的文件名
	 * @return
	 */
	public static JxlsBuilder getBuilder(String filePath) {
		// 判断是相对路径还是绝对路径
		if (JxlsUtil.me().isAbsolutePath(filePath)) {
			if (JxlsConfig.getTemplateRoot().startsWith("classpath:")) {
				// 文件在jar包内
				String templateRoot = JxlsConfig.getTemplateRoot()
						.replaceFirst("classpath:", "");
				InputStream resourceAsStream = JxlsBuilder.class
						.getResourceAsStream(templateRoot + "/" + filePath);
				return new AbstractJxlsBuilderImpl(resourceAsStream);
			}
			else {
				// 相对路径就从模板目录获取文件
				return new AbstractJxlsBuilderImpl(new File(
						JxlsConfig.getTemplateRoot() + File.separator + filePath));
			}
		}
		else {
			// 绝对路径
			return new AbstractJxlsBuilderImpl(new File(filePath));
		}
	}

	public JxlsHelper getJxlsHelper() {
		return jxlsHelper;
	}

	/**
	 * 生成excel文件
	 * @return
	 * @throws Exception
	 */
	public JxlsBuilder build() throws Exception {
		getTransformer();

		if (JxlsUtil.me().hasText(imageRoot)) {
			context.putVar("_imageRoot", imageRoot);
		}
		context.putVar("_ignoreImageMiss", ignoreImageMiss);

		JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) jxlsHelper
				.createExpressionEvaluator(null);
		evaluator.getJexlEngine().setFunctions(funcs);
		evaluator.getJexlEngine().setSilent(JxlsConfig.getSilent());

		transform();
		return this;

	}

	private void transform() throws Exception {
		jxlsHelper.getAreaBuilder().setTransformer(transformer);
		List<Area> xlsAreaList = jxlsHelper.getAreaBuilder().build();

		for (Area xlsArea : xlsAreaList) {
			xlsArea.applyAt(new CellRef(xlsArea.getStartCellRef().getCellName()),
					context);
			if (jxlsHelper.isProcessFormulas()) {
				FormulaProcessor fp;
				if (jxlsHelper.isUseFastFormulaProcessor()) {
					fp = new FastFormulaProcessor();
				}
				else {
					fp = new StandardFormulaProcessor();
				}
				xlsArea.setFormulaProcessor(fp);
				xlsArea.processFormulas();
			}
		}
		if (JxlsUtil.me().notEmpty(removeSheetNames)) {
			for (String sheetName : removeSheetNames) {
				transformer.deleteSheet(sheetName);
			}
		}
		transformer.deleteSheet("Sheet");
		transformer.write();
	}

	public Transformer getTransformer() throws IOException {
		if (transformer == null) {
			if (out == null && outFile == null) {
				throw new RuntimeException("请指定文件输出位置");
			}
			if (out == null) {
				out = Files.newOutputStream(outFile.toPath());
			}

			transformer = jxlsHelper.createTransformer(in, out);
		}
		return transformer;
	}

	/**
	 * 指定输出流
	 * @param out
	 * @return
	 */
	public JxlsBuilder out(OutputStream out) {
		this.out = out;
		return this;
	}

	/**
	 * 指定输出文件
	 * @param outFile
	 * @return
	 */
	public JxlsBuilder out(File outFile) {
		this.outFile = outFile;
		return this;
	}

	/**
	 * 指定输出文件绝对路径
	 * @param outPath
	 * @return
	 */
	public JxlsBuilder out(String outPath) {
		this.outFile = new File(outPath);
		return this;
	}

	/**
	 * 添加数据
	 * @param name
	 * @param value
	 * @return
	 */
	public void putVar(String name, Object value) {
		context.putVar(name, value);
	}

	/**
	 * 添加数据
	 * @param map
	 * @return
	 */
	public JxlsBuilder putAll(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			putVar(entry.getKey(), entry.getValue());
		}
		return this;
	}

	/**
	 * 删除数据
	 * @param name
	 * @return
	 */
	public JxlsBuilder removeVar(String name) {
		context.removeVar(name);
		return this;
	}

	/**
	 * 获取数据
	 * @param name
	 * @return
	 */
	public Object getVar(String name) {
		return context.getVar(name);
	}

	/**
	 * 添加自定义工具对象
	 * @param name
	 * @param function
	 * @return
	 */
	public JxlsBuilder addFunction(String name, Object function) {
		funcs.put(name, function);
		return this;
	}

	/**
	 * 设置图片根路径
	 * @param root
	 * @return
	 */
	public JxlsBuilder imageRoot(String root) {
		this.imageRoot = root;
		return this;
	}

	/**
	 * 设置是否忽略图片错误<br>
	 * true 忽略图片错误，如果图片读取失败时不终止运行，继续生成excel false 默认，不忽略图片错误，如果图片读取失败终止生成excel
	 * @param ignoreImageMiss
	 */
	public JxlsBuilder ignoreImageMiss(boolean ignoreImageMiss) {
		this.ignoreImageMiss = ignoreImageMiss;
		return this;
	}

	public File getInFile() {
		return inFile;
	}

	public File getOutFile() {
		return outFile;
	}

	/**
	 * 生成excel后删除指定表格
	 * @param sheetNames
	 * @return
	 */
	public JxlsBuilder removeSheet(String... sheetNames) {
		this.removeSheetNames = sheetNames;
		return this;
	}

	private static class AbstractJxlsBuilderImpl extends JxlsBuilder {

		public AbstractJxlsBuilderImpl(InputStream in) {
			super(in);
		}

		public AbstractJxlsBuilderImpl(File inFile) {
			super(inFile);
		}

	}

}
