package com.sunflower.framework.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听类，可以自定义
 *
 * @author sunflower
 */
public class ExcelListener<T extends BaseRowModel> extends AnalysisEventListener<T> {

	/**
	 * 自定义用于暂时存储data。可以通过实例获取该值
	 */
	private List<T> dataList = new ArrayList<>();

	/**
	 * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
	 */
	@Override
	public void invoke(T object, AnalysisContext context) {
		// 数据存储到list，供批量处理，或后续自己业务逻辑处理。
		dataList.add(object);
		/*
		 * 如数据过大，可以进行定量分批处理 if(dataList.size()<=100){ dataList.add(object); }else {
		 * doSomething(); dataList = new ArrayList<Object>(); }
		 */
		doSomething();

	}

	/**
	 * 根据业务自行实现该方法
	 */
	private void doSomething() {
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		/*
		 * dataList.clear(); 解析结束销毁不用的资源
		 */
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

}