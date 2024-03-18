package org.example.avoris.dto;

  public class SearchIdResponse {
    private final String searchId;

    public SearchIdResponse(String searchId) {
      this.searchId = searchId;
    }

    public String getSearchId() {
      return searchId;
    }
  }
