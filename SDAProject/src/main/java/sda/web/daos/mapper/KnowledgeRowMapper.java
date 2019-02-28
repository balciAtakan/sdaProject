package main.java.sda.web.daos.mapper;

import main.java.sda.web.util.DfXSubCategory;
import org.springframework.jdbc.core.RowMapper;
import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.views.KnowledgeView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KnowledgeRowMapper implements RowMapper<KnowledgeView> {

    @Override
    public KnowledgeView mapRow(ResultSet resultSet, int arg1) throws SQLException {

        KnowledgeView view = new KnowledgeView();
        view.setUuid(resultSet.getString("uuid"));
        view.setWord(resultSet.getString("word"));
        view.setDfXCategory(DfXCategory.getEnum(resultSet.getString("category")));
        view.setDfXSubCategory(DfXSubCategory.getEnum(resultSet.getString("subcategory"),false));
        view.setKnowledge_text(resultSet.getString("knowledge_text"));
        view.setModifyDate(resultSet.getDate("modify_date"));
        view.setOwnerUsername(resultSet.getString("username"));
        ///view.setFileUpload(resultSet.getBinaryStream("knowledge_data"));

        return view;

    }


}
