package com.movieflix.movieApi.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.movieflix.movieApi.dto.MovieDto;
import com.movieflix.movieApi.dto.MoviePageResponse;
import com.movieflix.movieApi.entities.Movie;
import com.movieflix.movieApi.exceptions.MovieNotFoundException;
import com.movieflix.movieApi.repository.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        // 1. upload the file
        
        String uploadedFileName = fileService.uploadFile(path, file);

        // 2. set the value of field 'poster' as filename
        movieDto.setPoster(uploadedFileName);

        // 3. map dto to Movie object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // 4. save the movie object -> saved Movie object
        Movie savedMovie = movieRepository.save(movie);

        // 5. generate the posterUrl
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        // 6. map Movie object to DTO object and return it
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );

        return response;
    }
    
	@Override
	public MovieDto getMovie(Integer movieId) {
		
		// 1. check the data in db  and if exists, fetch the data of the given Id
		
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("Movie not found with id : " + movieId));
		
		
		// generate the poster url
		
		String posterUrl = baseUrl + "/file/" + movie.getPoster();
		
		// map to movieDto object and return it
		
		MovieDto response = new MovieDto(
		           
				movie.getMovieId(),
				movie.getTitle(),
				movie.getDirector(),
				movie.getStudio(),
				movie.getMovieCast(),
				movie.getReleaseYear(),
				movie.getPoster(),
				posterUrl
				
		); 
		
		
		return response;
		
		
	}

	@Override
	public List<MovieDto> getAllMovies() {
		
		
		// 1. fetch all data from DB
		List<Movie> movies = movieRepository.findAll();
		
		// 2. iterate through the movie list to generate the posterurl for each movie object and map to movieDto
		List<MovieDto> movieDtos = new ArrayList<MovieDto>();
		
		for(Movie movie : movies) {
			
			String posterUrl = baseUrl + "/file/" + movie.getPoster();
			
			MovieDto movieDto = new MovieDto(
			         movie.getMovieId(),
			         movie.getTitle(),
			         movie.getDirector(),
			         movie.getStudio(),
			         movie.getMovieCast(),
			         movie.getReleaseYear(),
			         movie.getPoster(),
			         posterUrl
			);
			
			movieDtos.add(movieDto);
		}
		
		return movieDtos;
	}

	@Override
	public MovieDto update(Integer movieId, MultipartFile file, MovieDto movieDto ) throws IOException {
		
		// 1. check if movie object exist with the given movieId
		
		Movie mv = movieRepository.findById(movieId).orElseThrow(() ->new MovieNotFoundException("Movie not Found with id : " + movieId));
		
		// 2. if file is null do nothing 
		// if file is not null, then delete the existing file associate with the records and upload the new file
		String fileName = mv.getPoster();
		
		if(file != null) {
			
			Files.deleteIfExists(Paths.get(path + File.separator + fileName));
			
			fileName = fileService.uploadFile(path, file);
		}
		
		// 3. set movieDto's poster value, according to step2
		 movieDto.setPoster(fileName);
		 
        // 4. map to movie object
		 Movie movie = new Movie(
				 mv.getMovieId(),
				 movieDto.getTitle(),
				 movieDto.getDirector(),
				 movieDto.getStudio(),
				 movieDto.getMovieCast(),
				 movieDto.getReleaseYear(),
				 movieDto.getPoster()
		  );
		 
		 // 5. save the movie object and return the saved movie object
		 
		 Movie updatedMovie = movieRepository.save(movie);
		 
		 // 6.generate  posterurl for it
		 
		 String posterUrl = baseUrl + "/file/" + mv.getPoster();
		 
		 // 7. map to movieDto and return it
		 
		 MovieDto response = new MovieDto(
		         movie.getMovieId(),
		         movie.getTitle(),
		         movie.getDirector(),
		         movie.getStudio(),
		         movie.getMovieCast(),
		         movie.getReleaseYear(),
		         movie.getPoster(),
		         posterUrl
		);
		 
		
		
		 return response;
		
		
	}

	@Override
	public String delete(Integer movieId) throws IOException {
	   
		// 1. check if movie object exist in the DB
		
		Movie mv = movieRepository.findById(movieId).orElseThrow(() -> new MovieNotFoundException("movie not found with id : " + movieId));
		
		Integer id = mv.getMovieId();
		// 2. delete the file associated with the movie object
		
		Files.deleteIfExists(Paths.get(path + File.separator + mv.getPoster()));
		
		// 3. delete movie object associated with it
		
		movieRepository.delete(mv);
		
		
		return "Movie deleted with id : " + movieId;
		
		
		
		
		
	}

	@Override
	public MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize) {
		
		 Pageable pageable = PageRequest.of(pageNumber, pageSize);
		 
		 Page<Movie> moviePages = movieRepository.findAll(pageable);
		 
		 List<Movie> movies = moviePages.getContent();
		 
		// 2. iterate through the movie list to generate the posterurl for each movie object and map to movieDto
			List<MovieDto> movieDtos = new ArrayList<MovieDto>();
			
			for(Movie movie : movies) {
				
				String posterUrl = baseUrl + "/file/" + movie.getPoster();
				
				MovieDto movieDto = new MovieDto(
				         movie.getMovieId(),
				         movie.getTitle(),
				         movie.getDirector(),
				         movie.getStudio(),
				         movie.getMovieCast(),
				         movie.getReleaseYear(),
				         movie.getPoster(),
				         posterUrl
				);
				
				movieDtos.add(movieDto);
			}
		
		
		return new MoviePageResponse(movieDtos, pageNumber, pageSize,  moviePages.getNumberOfElements(),moviePages.getTotalPages(), moviePages.isLast());
	}

	@Override
	public MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy,
			String dir) {
		
		
		Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				                                   : Sort.by(sortBy).descending();
		
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		 
		 Page<Movie> moviePages = movieRepository.findAll(pageable);
		 
		 List<Movie> movies = moviePages.getContent();
		 
		// 2. iterate through the movie list to generate the posterurl for each movie object and map to movieDto
			List<MovieDto> movieDtos = new ArrayList<MovieDto>();
			
			for(Movie movie : movies) {
				
				String posterUrl = baseUrl + "/file/" + movie.getPoster();
				
				MovieDto movieDto = new MovieDto(
				         movie.getMovieId(),
				         movie.getTitle(),
				         movie.getDirector(),
				         movie.getStudio(),
				         movie.getMovieCast(),
				         movie.getReleaseYear(),
				         movie.getPoster(),
				         posterUrl
				);
				
				movieDtos.add(movieDto);
			}
		
		
		return new MoviePageResponse(movieDtos, pageNumber, pageSize, moviePages.getNumberOfElements(), moviePages.getTotalPages(), moviePages.isLast());
	}

}
