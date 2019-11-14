package main.java.sda.web.daos.mapper;

import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;
import main.java.sda.web.views.KnowledgeView;
import main.java.sda.web.views.MessageView;
import main.java.sda.web.views.WordView;
import org.springframework.jdbc.core.RowMapper;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KnowledgeRowMapper implements RowMapper<KnowledgeView>
{

    @Override
    public KnowledgeView mapRow(ResultSet resultSet, int arg1) throws SQLException
    {

        KnowledgeView view = new KnowledgeView();
        view.setUuid(resultSet.getString("uuid"));
        view.setWord(resultSet.getString("word"));
        view.setDfXCategory(DfXCategory.getEnum(resultSet.getString("category")));
        view.setDfXSubCategory(DfXSubCategory.getEnum(resultSet.getString("subcategory"), false));
        view.setKnowledge_text(resultSet.getString("knowledge_text"));
        view.setModifyDate(resultSet.getDate("modify_date"));
        view.setOwnerUsername(resultSet.getString("username"));

        InputStream document = resultSet.getBinaryStream("knowledge_data");
            /*File file = new File(view.getWord().concat("_document.").concat("pdf"));

            java.nio.file.Files.copy(
                    document,
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            IOUtils.closeQuietly(document);*/

        view.setFileUpload(document);

        String synonym = resultSet.getString("syn_word");
        ArrayList<WordView> synonyms = new ArrayList<>();
        if(synonym != null && !synonym.isEmpty()) {
            WordView syn = new WordView(synonym);
            syn.setScore(resultSet.getInt("score"));
        }
        view.setSynonyms(synonyms);

        return view;

    }


}
