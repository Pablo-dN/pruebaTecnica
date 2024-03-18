package org.example.avoris.dto;

import java.util.Objects;

public class SearchResultResponse {
    private String searchId;
    private SearchRequest request;
    private int count;

    public SearchResultResponse() {
    }

    public String getSearchId() {
      return searchId;
    }

    public void setSearchId(String searchId) {
      this.searchId = searchId;
    }

    public SearchRequest getRequest() {
      return request;
    }

    public void setRequest(SearchRequest request) {
      this.request = request;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null || getClass() != obj.getClass())
        return false;
      SearchResultResponse other = (SearchResultResponse) obj;
      return Objects.equals(searchId, other.searchId) &&
          Objects.equals(request, other.request) &&
          count == other.count;
    }
  }
