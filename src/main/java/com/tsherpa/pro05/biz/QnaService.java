package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.Qna;
import com.tsherpa.pro05.per.QnaMapper;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QnaService {

    @Autowired
    private QnaMapper qnaMapper;

    public List<Qna> getList(Page page) { return qnaMapper.getList(page); }
    public List<Qna> noAnswerList(Page page) { return qnaMapper.noAnswerList(page); }
    public int noAnswerCount(Page page) { return qnaMapper.noAnswerCount(page);}
    public int getCount(Page page) { return qnaMapper.getCount(page);}
    public Qna qnaDetail(int qno) { return qnaMapper.qnaDetail(qno); }
    public Qna questionDetail(int par) { return qnaMapper.questionDetail(par); }
    public Qna answerDetail(int par) { return qnaMapper.answerDetail(par); }
    @Transactional
    public void questionInsert(Qna qna) {
        qnaMapper.questionInsert(qna);
        qnaMapper.parUpdate(qna);
    }
    public void answerInsert(Qna qna) { qnaMapper.answerInsert(qna); }
    public void parUpdate(Qna qna) { qnaMapper.parUpdate(qna); }
    public void qnaEdit(Qna qna) { qnaMapper.qnaEdit(qna); }
    public void qnaDelete(int qno) { qnaMapper.qnaDelete(qno); }

}

