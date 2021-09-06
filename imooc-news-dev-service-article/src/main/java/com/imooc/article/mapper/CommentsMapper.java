package com.imooc.article.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface CommentsMapper extends MyMapper<Comments> {
    @Select("SELECT\n" +
            "        c.id as commentId,\n" +
            "        c.father_id as fatherId,\n" +
            "        c.comment_user_id as commentUserId,\n" +
            "        c.comment_user_nickname as commentUserNickname,\n" +
            "        c.article_id as articleId,\n" +
            "        c.content as content,\n" +
            "        c.create_time as createTime,\n" +
            "        f.comment_user_nickname as quoteUserNickname,\n" +
            "        f.content as quoteContent\n" +
            "    FROM\n" +
            "        comments c\n" +
            "    LEFT JOIN\n" +
            "        comments f\n" +
            "    ON\n" +
            "        c.father_id = f.id\n" +
            "    WHERE\n" +
            "        c.article_id = #{paramMap.articleId}\n" +
            "    ORDER BY\n" +
            "        c.create_time\n" +
            "    DESC")
    List<CommentsVO> queryArticleComments(@Param("paramMap") HashMap<String, String> map);
}