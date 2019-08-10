package main.java.sda.web.daos.mapper;

import main.java.sda.web.views.DfxView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DfxMapper implements RowMapper<DfxView>
{

    @Override
    public DfxView mapRow(ResultSet rs, int arg1) throws SQLException
    {

        return new DfxView(rs.getString("uuid"), rs.getString("cat_name"), rs.getString("sub_category"));

    }


}
