package com.movieflix.movieApi.dto;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDto,
		  						
								Integer pageNumber,
								
								Integer pageSize,
								
								long totalElements,
								
								int totalpages,
								
								boolean isLast
		
		
		                     ) {

}
