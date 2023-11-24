package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Faq;
import com.tsherpa.pro05.per.FaqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {

    @Autowired
    private FaqMapper faqMapper;

    public List<Faq> getList () { return faqMapper.getList(); }
}
