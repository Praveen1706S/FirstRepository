package com.movieflix.movieApi.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.MoviePageResponse;
import com.movieflix.movieApi.exceptions.EmptyFileException;
import com.movieflix.movieApi.service.MovieService;

import utils.AppConstants;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
	
	
	private final MovieService movieService;
	
	
	
	
	public MovieController(MovieService movieService) {
		
		this.movieService = movieService;
	}

	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/add-movie")
	public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, @RequestPart String movieDto ) throws IOException, EmptyFileException{
		
		
		if(file.isEmpty()) {
			
			throw new EmptyFileException("File is empty! please send another file!");
		}
		MovieDto dto = convertToMovieDto(movieDto);
		
		return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
		
		
				
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<MovieDto> getMovieHandler(@PathVariable Integer movieId){
		
		return ResponseEntity.ok(movieService.getMovie(movieId));
	}
	
	
	
	@GetMapping("/all")
	public ResponseEntity<List<MovieDto>> getAllMovieHandler(){
		
		return ResponseEntity.ok(movieService.getAllMovies());
	}
	
	
	@PutMapping("/update/{movieId}")
	public ResponseEntity<MovieDto> updatMovieHandler(@PathVariable Integer movieId,
			                                           @RequestPart MultipartFile file,
			                                           @RequestPart String movieDtoObj) throws IOException{
		
		if(file.isEmpty()) {
			
			file =null;
		}
		
		MovieDto movieDto = convertToMovieDto(movieDtoObj);
					
		return ResponseEntity.ok(movieService.update(movieId, file, movieDto));
		
	}
	
	@DeleteMapping("delete/{movieId}")
	public ResponseEntity<String> deleteMovieHandler(@PathVariable Integer movieId) throws IOException{
		
		return ResponseEntity.ok(movieService.delete(movieId));
	}
	
	
	@GetMapping("/AllMoviesPage")
	public ResponseEntity<MoviePageResponse> getAllMoviesWithPagination(
			
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false ) Integer pageSize
			
			){
		
		
		return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
		
		
	}
	
	@GetMapping("/AllMoviesPageSort")
	public ResponseEntity<MoviePageResponse> getAllMoviesWithPaginationAndSorting(
			
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false ) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir
			
			){
		
		
		return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, dir));
		
		
	}
	
	
	// method to convert String to movieDto object 
	private MovieDto convertToMovieDto(String movieDtoObj) throws JsonMappingException, JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();  // convert String to object
		
		MovieDto movieDto = objectMapper.readValue(movieDtoObj, MovieDto.class);
		
		return movieDto;
		
	}

}
