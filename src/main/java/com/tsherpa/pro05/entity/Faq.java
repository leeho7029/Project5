package com.tsherpa.pro05.entity;

import lombok.Data;

@Data
public class Faq {
  private int fno;
  private String question;
  private String author;
  private String answer;
  private int cnt;
}
