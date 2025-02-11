package com.movieflix.movieApi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.MoviePageResponse;

public interface MovieService {
	
	public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;
	
	
	public MovieDto getMovie(Integer movieId);
	
	
	public List<MovieDto> getAllMovies();
	
	
	public MovieDto update(Integer movieId, MultipartFile file, MovieDto movieDto) throws IOException;
	
	
	public String delete(Integer movieId) throws IOException ;
	
	
	public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);
	
	
	public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);

}
