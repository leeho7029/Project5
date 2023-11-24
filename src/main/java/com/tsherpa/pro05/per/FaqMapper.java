package com.tsherpa.pro05.per;

import com.tsherpa.pro05.entity.Faq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaqMapper {

    List<Faq> getList();
}
