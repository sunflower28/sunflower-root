package com.sunflower.page;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.sunflower.api.InputPageDto;
import com.sunflower.api.PageDto;
import com.sunflower.api.PageResultDto;

import java.util.List;

/**
 * @author sunflower
 */
public final class PageConverter {

	private PageConverter() {

	}

	public <T> Page<T> request2Page(InputPageDto requestDto) {
		return requestDto == null ? new Page()
				: new Page<>(requestDto.getPageNum(), requestDto.getPageSize());
	}

	public <K> PageResultDto<K> response2Page(Pagination page, List<K> list,
			InputPageDto request) {
		PageDto<K> rPage = new PageDto<>();
		rPage.setList(list);
		rPage.setPageCount(page == null ? 0L : page.getPages());
		rPage.setPageSize(page == null ? request.getPageSize() : page.getSize());
		rPage.setTotal(page == null ? 0L : page.getTotal());
		rPage.setPageNum(page == null ? request.getPageNum() : page.getCurrent());
		return PageResultDto.success(rPage);
	}

}
