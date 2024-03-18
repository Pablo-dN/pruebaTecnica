package org.example.avoris.controller;

import org.example.avoris.dto.SearchIdResponse;
import org.example.avoris.dto.SearchRequest;
import org.example.avoris.dto.SearchResultResponse;
import org.example.avoris.exception.SearchException;
import org.example.avoris.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

  private final SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @PostMapping
  public ResponseEntity<?> search(@Valid @RequestBody SearchRequest request) {
    try {
      String searchId = searchService.processSearchRequest(request);
      return ResponseEntity.ok(new SearchIdResponse(searchId));
    } catch (SearchException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred during search: " + e.getMessage());
    }
  }

  @GetMapping("/count")
  public ResponseEntity<?> getCount(@RequestParam String searchId) {
    try {
      SearchResultResponse result = searchService.getSearchRequestBySearchId(searchId);
      if (result == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(result);
    } catch (SearchException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Error occurred during search: " + e.getMessage());
    }
  }
}
