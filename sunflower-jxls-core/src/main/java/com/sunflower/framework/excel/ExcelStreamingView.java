package com.sunflower.framework.excel;

import com.sunflower.framework.excel.command.MergeCommand;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jxls.area.Area;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.formula.StandardFormulaProcessor;
import org.jxls.transform.TransformationConfig;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.http.ContentDisposition;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sunflower
 */
public class ExcelStreamingView extends AbstractView {
    private String templateName;
    private String fileName;

    public ExcelStreamingView(String templateName, String fileName) {
        this.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        this.templateName = templateName;
        this.fileName = fileName;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(this.getContentType());
        response.setHeader("Content-Disposition", ContentDisposition.builder("form-data").name("attachment").filename(this.fileName, Charset.forName("UTF-8")).build().toString());
        ServletOutputStream out = response.getOutputStream();
        PoiTransformer transformer = PoiTransformer.createSxssfTransformer(WorkbookFactory.create(this.getClass().getClassLoader().getResourceAsStream("template/" + this.templateName)), 100, true);
        transformer.setOutputStream(out);
        JexlExpressionEvaluator jexlExpressionEvaluator = new JexlExpressionEvaluator();
        JexlEngine jexlEngine = jexlExpressionEvaluator.getJexlEngine();
        jexlEngine.setSilent(true);
        TransformationConfig transformationConfig = new TransformationConfig();
        transformationConfig.setExpressionEvaluator(jexlExpressionEvaluator);
        transformer.setTransformationConfig(transformationConfig);
        Context context = PoiTransformer.createInitialContext();
        Set<String> set = model.keySet();

        for (String s : set) {
            context.putVar(s,model.get(s));
        }

        XlsCommentAreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
        List<Area> xlsAreaList = areaBuilder.build();

        for (Area area : xlsAreaList) {
            String sourceSheetName = area.getStartCellRef().getSheetName();
            CellRef targetCellRef = new CellRef("_" + sourceSheetName + "!A1");
            area.applyAt(targetCellRef, context);
            area.setFormulaProcessor(new StandardFormulaProcessor());
            area.processFormulas();
            if (!sourceSheetName.equalsIgnoreCase(targetCellRef.getSheetName())) {
                transformer.deleteSheet(sourceSheetName);
            }
        }

        transformer.write();
        out.flush();
        out.close();
    }
}
