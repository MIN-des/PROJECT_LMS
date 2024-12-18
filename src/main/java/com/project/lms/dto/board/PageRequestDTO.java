package com.project.lms.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO { //페이징 처리 DTO

  @Builder.Default
  private int page = 1;

  @Builder.Default
  private int size = 10;

  private String type;
  private String keyword;

  public String[] getTypes() { // type 문자열 배열로 반환해주는 기능
    if (type == null || type.isEmpty())
      return null;

    return type.split("");
  }

  public Pageable getPageable(String... props) { // 페이징 처리
    return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
  }

  private String link;

  public String getLink() {
    if (link == null) {
      StringBuilder builder = new StringBuilder();
      builder.append("page=" + this.page);
      builder.append("&size=" + this.size);

      if (type != null && type.length() > 0) {
        builder.append("&type=" + type);
      }

      if (keyword != null) {
        try {
          builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }
      }

      link = builder.toString();
    }

    return link;
  }
}
