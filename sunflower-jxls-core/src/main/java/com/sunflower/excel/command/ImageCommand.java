package com.sunflower.excel.command;

import com.sunflower.excel.JxlsBuilder;
import com.sunflower.excel.JxlsImage;
import com.sunflower.excel.JxlsUtil;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.AreaRef;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.ImageType;
import org.jxls.common.Size;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * 插入图片
 * </p>
 * jx:image( src="byte[] | JxlsImage | 图片路径（相对图片目录或绝对绝对路径）",
 * lastCell="图片右下角单元格坐标，左上角坐为指令所在单元格" [,imageType="JPG"] [,size="auto | original"]
 * [,scaleX="1"] [,scaleY="1"] )
 *
 * @Author lnk
 * @Date 2018/1/23
 */
public class ImageCommand extends AbstractCommand {

	private byte[] imageBytes;

	private ImageType imageType = ImageType.PNG;

	private Area area;

	/**
	 * 图片源，可以byte[]、JxlsImage对象和图片路径
	 */
	private String src;

	/**
	 * 插入图片大小
	 * @see <a href=
	 * "http://poi.apache.org/apidocs/org/apache/poi/ss/usermodel/Picture.html">Picture.resize(scaleX,scaleY)</a>
	 */
	private String scaleX;

	private String scaleY;

	/**
	 * 自适应大小类型<br>
	 * auto 默认，自适应单元格大小<br>
	 * original 图片原大小
	 */
	private String size;

	public ImageCommand() {
	}

	public ImageCommand(String image, ImageType imageType) {
		this.src = image;
		this.imageType = imageType;
	}

	public ImageCommand(byte[] imageBytes, ImageType imageType) {
		this.imageBytes = imageBytes;
		this.imageType = imageType;
	}

	@Override
	public Command addArea(Area area) {
		if (!CollectionUtils.isEmpty(super.getAreaList())) {
			throw new IllegalArgumentException(
					"You can add only a single area to 'image' command");
		}
		this.area = area;
		return super.addArea(area);
	}

	@Override
	public String getName() {
		return "image";
	}

	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		if (area == null) {
			throw new IllegalArgumentException("No area is defined for image command");
		}
		Transformer transformer = getTransformer();
		Size sizeArea = area.getSize();

		try {
			JxlsImage img = getImage(context);
			if (img != null) {
				if (transformer instanceof PoiTransformer) {
					addImage(cellRef, context, (PoiTransformer) transformer, img);
				}
				else {
					// 获取图片显示区域是时候，多加一行和一列，获取完之后再恢复原来大小
					sizeArea.setWidth(sizeArea.getWidth() + 1);
					sizeArea.setHeight(sizeArea.getHeight() + 1);
					AreaRef areaRef = new AreaRef(cellRef, sizeArea);
					sizeArea.setWidth(sizeArea.getWidth() - 1);
					sizeArea.setHeight(sizeArea.getHeight() - 1);
					transformer.addImage(areaRef, img.getPictureData(),
							img.getJxlsImageType());
				}
			}
		}
		catch (Exception e) {
			Boolean ignoreImageMiss = (Boolean) context.getVar("_ignoreImageMiss");
			if (ignoreImageMiss == null || !ignoreImageMiss) {
				// 忽略图片读取失败，继续生成excel后面操作
				throw new IllegalArgumentException("出现异常，终止生成excel", e);
			}

		}
		// 恢复原有的样式
		area.applyAt(cellRef, context);
		return sizeArea;
	}

	private void addImage(CellRef cellRef, Context context, PoiTransformer transformer,
			JxlsImage img) {
		Workbook wb = transformer.getWorkbook();
		int pictureIdx = wb.addPicture(img.getPictureData(), img.getWorkbookImageType());
		Sheet sheet = wb.getSheet(cellRef.getSheetName());
		Drawing drawing = sheet.createDrawingPatriarch();
		CreationHelper helper = wb.getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(cellRef.getCol());
		anchor.setCol2(cellRef.getCol() + area.getSize().getWidth());
		anchor.setRow1(cellRef.getRow());
		anchor.setRow2(cellRef.getRow() + area.getSize().getHeight());
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		if (JxlsUtil.me().hasText(scaleX) || JxlsUtil.me().hasText(scaleY)) {
			double scale2X = 1d;
			double scale2Y = 1d;
			if (JxlsUtil.me().hasText(this.scaleX)) {
				Object scaleXObj = getTransformationConfig().getExpressionEvaluator()
						.evaluate(this.scaleX, context.toMap());
				scale2X = (double) scaleXObj;
			}
			if (JxlsUtil.me().hasText(this.scaleY)) {
				Object scaleXObj = getTransformationConfig().getExpressionEvaluator()
						.evaluate(this.scaleY, context.toMap());
				scale2Y = (double) scaleXObj;
			}

			pict.resize(scale2X, scale2Y);
		}
		else if (JxlsImage.IMAGE_SIZE_TYPE_ORIGINAL.equalsIgnoreCase(size)) {
			pict.resize();
		}
		else {
			pict.resize(1d);
		}
	}

	private JxlsImage getImage(Context context) throws IOException {
		if (imageBytes == null && src != null) {
			Object imgObj = getTransformationConfig().getExpressionEvaluator()
					.evaluate(src, context.toMap());
			if (imgObj != null) {
				if (imgObj instanceof byte[]) {
					imageBytes = (byte[]) imgObj;
				}
				else if (imgObj instanceof JxlsImage) {
					return (JxlsImage) imgObj;
				}
				else if (imgObj instanceof String) {
					String imgSrc = (String) imgObj;

					String imageRoot = (String) context.getVar("_imageRoot");
					// 判断是相对路径还是绝对路径
					if (JxlsUtil.me().isAbsolutePath(imgSrc)) {
						if (imageRoot.startsWith("classpath:")) {
							// 文件在jar包内
							String templateRoot = imageRoot.replaceFirst("classpath:",
									"");
							InputStream resourceAsStream = JxlsBuilder.class
									.getResourceAsStream(
											templateRoot + File.separator + imgSrc);
							return JxlsUtil.me().getJxlsImage(resourceAsStream, imgSrc);
						}
						else {
							// 相对路径就从模板目录获取文件
							return JxlsUtil.me()
									.getJxlsImage(imageRoot + File.separator + imgSrc);
						}
					}
					else {
						// 绝对路径
						return JxlsUtil.me().getJxlsImage(imgSrc);
					}
				}
			}
		}

		if (imageBytes != null) {
			JxlsImage img = new JxlsImage();
			img.setPictureData(imageBytes);
			if (imageType != null) {
				img.setPictureType(imageType.toString());
			}
			else {
				img.setPictureType("jpg");
			}
			return img;
		}

		throw new IllegalArgumentException("图片读取失败 " + JxlsUtil.me().getNotNull(src, ""));
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setImageType(String strType) {
		imageType = ImageType.valueOf(strType);
	}

	public String getScaleX() {
		return scaleX;
	}

	public void setScaleX(String scaleX) {
		this.scaleX = scaleX;
	}

	public String getScaleY() {
		return scaleY;
	}

	public void setScaleY(String scaleY) {
		this.scaleY = scaleY;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
