package com.example.explorecalijpa.web;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.explorecalijpa.business.TourRatingService;
import com.example.explorecalijpa.model.TourRating;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Tour Rating Controller
 *
 * Created by Mary Ellen Bowman
 */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
  private TourRatingService tourRatingService;

  public TourRatingController(TourRatingService tourRatingService) {
    this.tourRatingService = tourRatingService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@PathVariable(value = "tourId") int tourId, @RequestBody @Valid RatingDto dto) {
    tourRatingService.createNew(
        tourId,
        dto.getCustomerId(),
        dto.getScore(),
        dto.getComment());
  }

  @GetMapping
  public List<RatingDto> getAll(@PathVariable("tourId") int tourId) {
    List<TourRating> tourRatings = tourRatingService.lookupRatings(tourId);
    return tourRatings.stream().map(RatingDto::new).toList();
  }

  @GetMapping("/average")
  public Map<String, Double> getAverage(@PathVariable("tourId") int tourId) {
    return Map.of("average", tourRatingService.getAverageScore(tourId));
  }

  @PostMapping
  public RatingDto update(
      @PathVariable("tourId") int tourId,
      @RequestBody @Valid RatingDto dto) {
    return new RatingDto(
        tourRatingService.update(
            tourId,
            dto.getCustomerId(),
            dto.getScore(),
            dto.getComment()));
  }

  @DeleteMapping
  public void delete(
      @PathVariable("tourId") int tourId,
      @RequestParam("customerId") int customerId) {
    tourRatingService.delete(tourId, customerId);
  }

  @PatchMapping
  public void updateSome(
      @PathVariable("tourId") int tourId,
      @RequestBody RatingDto dto) {
    tourRatingService.updateSome(
        tourId,
        dto.getCustomerId(),
        Optional.ofNullable(dto.getScore()),
        Optional.ofNullable(dto.getComment()));
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String return404(NoSuchElementException e) {
    return e.getMessage();
  }
}
