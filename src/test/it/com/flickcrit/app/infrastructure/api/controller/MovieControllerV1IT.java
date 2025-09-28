package com.flickcrit.app.infrastructure.api.controller;

import com.flickcrit.app.domain.exception.EntityNotFoundException;
import com.flickcrit.app.domain.model.movie.MovieId;
import com.flickcrit.app.infrastructure.api.model.common.PageResponse;
import com.flickcrit.app.infrastructure.api.model.movie.MovieCreateRequest;
import com.flickcrit.app.infrastructure.api.model.movie.MovieDto;
import com.flickcrit.app.infrastructure.api.port.MoviePort;
import com.flickcrit.app.infrastructure.api.port.TopRatingPort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieControllerV1.class)
public class MovieControllerV1IT extends BaseControllerIT {

    @MockitoBean
    private MoviePort portMock;

    @MockitoBean
    private TopRatingPort topRatingPort;

    @Test
    void whenGetMoviesExpectMoviesPage() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(10, 100);
        MovieDto movieDto = createMovieBuilder().build();

        when(portMock
            .getMovies(any()))
            .thenReturn(PageResponse.of(List.of(movieDto)));

        // when / then
        mockMvc.perform(get("/api/v1/movies?page=10&size=100")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items[0].id").value(movieDto.id()))
            .andExpect(jsonPath("$.items[0].isan").value(movieDto.isan().toString()))
            .andExpect(jsonPath("$.items[0].title").value(movieDto.title()))
            .andExpect(jsonPath("$.items[0].year").value(movieDto.year()));

        verify(portMock).getMovies(pageRequest);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenValidIdWhenGetMovieThenReturnMovie() throws Exception {
        // given
        MovieDto movieDto = createMovieBuilder().build();
        MovieId movieId = MovieId.of(15L);

        when(portMock
            .getMovie(any(MovieId.class)))
            .thenReturn(movieDto);

        // when / then
        mockMvc.perform(get("/api/v1/movies/" + movieId.value())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(movieDto.id()))
            .andExpect(jsonPath("$.isan").value(movieDto.isan().toString()))
            .andExpect(jsonPath("$.title").value(movieDto.title()))
            .andExpect(jsonPath("$.year").value(movieDto.year()));

        verify(portMock).getMovie(movieId);
        verifyNoMoreInteractions(portMock);
    }

    @Test
    void givenInvalidIdWhenGetMovieThenReturn404() throws Exception {
        // given
        MovieId movieId = MovieId.of(15L);

        when(portMock
            .getMovie(any(MovieId.class)))
            .thenThrow(new EntityNotFoundException("Movie not found"));

        // when / then
        mockMvc.perform(get("/api/v1/movies/{id}", movieId.value())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

        verify(portMock).getMovie(movieId);
        verifyNoMoreInteractions(portMock);
    }

    @Nested
    @WithMockUser(roles = "ADMIN")
    class MoviesAdminTests {
        @Test
        void givenValidMovieWhenCreateMovieThenReturnCreated() throws Exception {
            // given
            MovieDto movieDto = createMovieBuilder().build();
            MovieCreateRequest request = createMovieCreateRequestBuilder().build();

            when(portMock
                .createMovie(any()))
                .thenReturn(movieDto);

            // when / then
            mockMvc.perform(post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movieDto.id()))
                .andExpect(jsonPath("$.isan").value(movieDto.isan().toString()))
                .andExpect(jsonPath("$.title").value(movieDto.title()))
                .andExpect(jsonPath("$.year").value(movieDto.year()));

            verify(portMock).createMovie(request);
            verifyNoMoreInteractions(portMock);
        }

        @Test
        void givenInvalidMovieWhenCreateMovieThenReturnBadRequest() throws Exception {
            // given
            MovieCreateRequest invalidRequest = MovieCreateRequest.builder().build();

            // when / then
            mockMvc.perform(post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

            verifyNoInteractions(portMock);
        }

        @Test
        void givenValidMovieWhenUpdateMovieThenReturnOk() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);
            MovieDto movieDto = createMovieBuilder().build();
            MovieCreateRequest request = createMovieCreateRequestBuilder().build();

            when(portMock.updateMovie(any(MovieId.class), any()))
                .thenReturn(movieDto);

            // when / then
            mockMvc.perform(put("/api/v1/movies/{id}", movieId.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(movieDto.id()))
                .andExpect(jsonPath("$.isan").value(movieDto.isan().toString()))
                .andExpect(jsonPath("$.title").value(movieDto.title()))
                .andExpect(jsonPath("$.year").value(movieDto.year()));

            verify(portMock).updateMovie(movieId, request);
            verifyNoMoreInteractions(portMock);
        }

        @Test
        void givenNonExistingMovieWhenUpdateMovieThenReturn404() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);
            MovieCreateRequest request = createMovieCreateRequestBuilder().build();

            when(portMock
                .updateMovie(any(), any()))
                .thenThrow(new EntityNotFoundException("Movie not found"));

            // when / then
            mockMvc.perform(put("/api/v1/movies/{id}", movieId.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

            verify(portMock).updateMovie(movieId, request);
            verifyNoMoreInteractions(portMock);
        }

        @Test
        void givenInvalidMovieWhenUpdateMovieThenReturnBadRequest() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);
            MovieCreateRequest invalidRequest = MovieCreateRequest.builder().build();

            // when / then
            mockMvc.perform(put("/api/v1/movies/{id}", movieId.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

            verifyNoInteractions(portMock);
        }

        @Test
        void givenValidIdWhenDeleteMovieThenReturnNoContent() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);

            doNothing().when(portMock).deleteMovie(any(MovieId.class));

            // when / then
            mockMvc.perform(delete("/api/v1/movies/{id}", movieId.value()))
                .andExpect(status().isNoContent());

            verify(portMock).deleteMovie(movieId);
            verifyNoMoreInteractions(portMock);
        }

        @Test
        void givenNonExistingIdWhenDeleteMovieThenReturn404() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);

            doThrow(new EntityNotFoundException("Movie not found"))
                .when(portMock).deleteMovie(any(MovieId.class));

            // when / then
            mockMvc.perform(delete("/api/v1/movies/{id}", movieId.value()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

            verify(portMock).deleteMovie(movieId);
            verifyNoMoreInteractions(portMock);
        }
    }

    @Nested
    @WithMockUser(roles = "USER")
    class MoviesRestrictedAreaAccessTest {

        @Test
        void givenValidMovieWhenCreateMovieAsNonAdminThenExpect403() throws Exception {
            MovieCreateRequest request = createMovieCreateRequestBuilder().build();
            mockMvc
                .perform(post("/api/v1/movies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON));

            verifyNoInteractions(portMock);
        }

        @Test
        void givenValidMovieWhenUpdateMovieAsNonAdminThenExpect403() throws Exception {
            // given
            MovieId movieId = MovieId.of(15L);
            MovieCreateRequest request = createMovieCreateRequestBuilder().build();
            mockMvc
                .perform(put("/api/v1/movies/{id}", movieId.value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
            verifyNoInteractions(portMock);
        }

        @Test
        void givenValidIdWhenDeleteMovieThenReturnNoContent() throws Exception {
            MovieId movieId = MovieId.of(15L);
            mockMvc
                .perform(delete("/api/v1/movies/{id}", movieId.value()))
                .andExpect(status().isForbidden());

            verifyNoInteractions(portMock);
        }
    }

    private MovieDto.MovieDtoBuilder createMovieBuilder() {
        return MovieDto.builder()
            .id(10L)
            .isan(UUID.randomUUID())
            .title("Title " + UUID.randomUUID())
            .year(2024);
    }

    private MovieCreateRequest.MovieCreateRequestBuilder createMovieCreateRequestBuilder() {
        return MovieCreateRequest.builder()
            .title("Movie Title")
            .year(2025);
    }
}
