package com.lcwd.electronic.store.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.lcwd.electronic.store.dtos.PageableResponse;


public class Helper {
	
	
	public static <T, U> PageableResponse<U> getPageableResponse(Page<T> page,Class<U> dto) {
		ModelMapper mapper = new ModelMapper();
		List<T> content = page.getContent();
		List<U> dtoList = content.stream()
				.map(c -> mapper.map(c, dto))
				.collect(Collectors.toList());
		
		PageableResponse<U> pageableResponse = new PageableResponse<>();
		pageableResponse.setContent(dtoList);
		pageableResponse.setTotalElements(page.getTotalElements());
		pageableResponse.setLastPage(page.isLast());
		pageableResponse.setPageNumber(page.getNumber());
		pageableResponse.setTotalPages(page.getTotalPages());
		return pageableResponse;

	}

}
